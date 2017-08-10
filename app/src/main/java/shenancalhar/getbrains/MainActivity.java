package shenancalhar.getbrains;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

// Generic Exploration Technology is an autonomous mobile robot
// designed for unknown area exploration and navigation.
// This module contains "brains" of the G.E.T.
public class MainActivity extends AppCompatActivity implements  Runnable {

    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    private static final byte PACKET_MARKER = 0x18;
    private static final byte ROTATION_COMMAND = 0x24;

    // These are used to establish android - arduino communication channel
    UsbAccessory mAccessory;
    UsbManager mUsbManager;
    FileInputStream mInputStream;
    FileOutputStream mOutputStream;
    ParcelFileDescriptor mFileDescriptor;
    boolean mPermissionRequestPending;
    PendingIntent mPermissionIntent;
    dataTransferThread arduinoOutThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        registerReceiver(mUsbReceiver, filter);
        if (getLastNonConfigurationInstance() != null) {
            mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
            openAccessory(mAccessory);
        }

//        arduinoOutThread = new dataTransferThread(mOutputStream,
//                (ToggleButton)findViewById(R.id.toggleButtonUp),
//                (ToggleButton)findViewById(R.id.toggleButtonLeft),
//                (ToggleButton)findViewById(R.id.toggleButtonDown),
//                (ToggleButton)findViewById(R.id.toggleButtonRight));
//        arduinoOutThread.start();
    }

//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        if (mAccessory != null) {
//            return mAccessory;
//        } else {
//            return super.onRetainNonConfigurationInstance();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();

        if (mInputStream != null && mOutputStream != null) {
            return;
        }

        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUsbManager.hasPermission(accessory)) {
                openAccessory(accessory);
            } else {
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(accessory,
                                mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            System.out.println("mAccessory is null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeAccessory();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    public void run() {
        int ret = 0;
        byte[] buffer = new byte[16384];
        int i;

        while (true) { // read data
            try {
                ret = mInputStream.read(buffer);
            } catch (IOException e) {
                break;
            }

            i = 0;
            while (i < ret) {
                int len = ret - i;
                if (len >= 1) {
                    Message m = Message.obtain(mHandler);
                    int value = (int)buffer[i];
                    // &squot;f&squot; is the flag, use for your own logic
                    // value is the value from the arduino
                    m.obj = new arduinoMsgValue('f', value);
                    mHandler.sendMessage(m);
                }
                i += 1; // number of bytes sent from arduino
            }



        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            arduinoMsgValue t = (arduinoMsgValue) msg.obj;
            // this is where you handle the data you sent. You get it by calling the getReading() function
            TextView textView = (TextView) findViewById(R.id.textViewConsole);
            textView.setText("Flag: "+t.getFlag()+"; Reading: "+t.getReading()+"; Date: "+(new Date().toString()));
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openAccessory(accessory);
                    } else {
                        System.out.println("permission denied for accessory "
                                + accessory);
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null && accessory.equals(mAccessory)) {
                    closeAccessory();
                }
            }
        }
    };

    private void openAccessory(UsbAccessory accessory) {
        mFileDescriptor = mUsbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            mAccessory = accessory;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
            Thread thread = new Thread(null, this, "OpenAccessoryTest");
            thread.start();
            System.out.println("Accessory opened!");

            arduinoOutThread = new dataTransferThread(mOutputStream,
                    (ToggleButton)findViewById(R.id.toggleButtonUp),
                    (ToggleButton)findViewById(R.id.toggleButtonLeft),
                    (ToggleButton)findViewById(R.id.toggleButtonDown),
                    (ToggleButton)findViewById(R.id.toggleButtonRight));
            arduinoOutThread.start();
        } else {
            System.out.println("Accessory open failed.");
        }
    }


    private void closeAccessory() {
        try {
            arduinoOutThread.closeThread();
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        } catch (IOException e) {
        } finally {
            mFileDescriptor = null;
            mAccessory = null;
        }
    }

    public void sendCommand(byte packetID, byte commandID, byte targetID, byte value) {
        byte[] buffer = new byte[4];

        buffer[0] = packetID; // should be PACKET_MARKER
        buffer[1] = commandID; // ROTATION_COMMAND
        buffer[2] = targetID; // 0 - 17 = id of the servo motor
        buffer[3] = value; // actual angle in degrees
        if (mOutputStream != null) {
            try {
                mOutputStream.write(buffer);
            } catch (IOException e) {
                System.out.println("write failed: " + e);
            }
        }
    }

    public void toggleButtonUp_Click(View v){
        System.out.println("UP click!");
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButtonDown);
        toggleButton.setChecked(false);

    }

    public void toggleButtonDown_Click(View v){
        System.out.println("DOWN click!");
        ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButtonUp);
        toggleButton.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        System.out.println("exitButton click");
        closeAccessory();
        // The safest way to close the application
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(0);
    }
}
