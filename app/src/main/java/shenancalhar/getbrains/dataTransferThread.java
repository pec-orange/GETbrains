package shenancalhar.getbrains;

import android.os.SystemClock;
import android.widget.ToggleButton;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shen an Calhar on 10.08.2017.
 */
public class dataTransferThread extends  Thread{

    public static final byte PACKET_MARKER = 0x18;
    public static final byte ROTATION_COMMAND = 0x24;

    private boolean isClosing = false;
    private FileOutputStream arduinoOutStream = null;
    private ToggleButton togButtonUp;
    private ToggleButton togButtonLeft;
    private ToggleButton togButtonDown;
    private ToggleButton togButtonRight;



    public dataTransferThread(FileOutputStream outStream, ToggleButton Up, ToggleButton Left, ToggleButton Down, ToggleButton Right) {
        super();
        arduinoOutStream = outStream;
        togButtonUp = Up;
        togButtonLeft = Left;
        togButtonDown = Down;
        togButtonRight = Right;
    }

    @Override
    public void run() {
        long startTime = SystemClock.currentThreadTimeMillis();
        long delay = 500;

        while (!isClosing) {
            if ((SystemClock.currentThreadTimeMillis() > startTime + delay)) {
                startTime = SystemClock.currentThreadTimeMillis();
                if (togButtonUp.isChecked()) {
                    System.out.println("STAND!!!");
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 0, (byte) 110);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 1, (byte) 110);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 2, (byte) 165);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 3, (byte) 150);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 4, (byte) 130);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 5, (byte) 140);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 6, (byte) 100);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 7, (byte) 50);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 8, (byte) 40);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 9, (byte) 120);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 10, (byte) 130);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 11, (byte) 140);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 12, (byte) 30);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 13, (byte) 30);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 14, (byte) 40);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 15, (byte) 100);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 16, (byte) 60);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 17, (byte) 20);
                } else if (togButtonDown.isChecked()) {
                    System.out.println("SIT!!!");
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 0, (byte) 110);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 1, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 2, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 3, (byte) 150);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 4, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 5, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 6, (byte) 100);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 7, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 8, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 9, (byte) 120);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 10, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 11, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 12, (byte) 30);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 13, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 14, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 15, (byte) 100);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 16, (byte) 90);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 17, (byte) 90);
                }
                else {
                    System.out.println("REST!!!");
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 0, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 1, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 2, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 3, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 4, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 5, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 6, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 7, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 8, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 9, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 10, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 11, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 12, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 13, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 14, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 15, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 16, (byte) 0);
                    sendCommand(PACKET_MARKER, ROTATION_COMMAND, (byte) 17, (byte) 0);
                }
            } //  if ((SystemClock.currentThreadTimeMillis() > startTime + delay))
        } // while (!isClosing)
    } // run

    public void sendCommand(byte packetID, byte commandID, byte targetID, byte value) {
        byte[] buffer = new byte[4];

        buffer[0] = packetID; // should be PACKET_MARKER
        buffer[1] = commandID; // ROTATION_COMMAND
        buffer[2] = targetID; // 0 - 17 = id of the servo motor
        buffer[3] = value; // actual angle in degrees
        if (arduinoOutStream != null) {
            try {
                arduinoOutStream.write(buffer);
            } catch (IOException e) {
                System.out.println("write failed: " + e);
            }
        }
    }

    public void closeThread() {
        isClosing = true;
    }
}
