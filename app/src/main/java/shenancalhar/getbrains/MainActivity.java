package shenancalhar.getbrains;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

// Generic Exploration Technology is an autonomous mobile robot
// designed for unknown area exploration and navigation.
// This module contains "brains" of the G.E.T.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toggleButtonLED_Click(View v){
        System.out.println("Button click!");
    }

    @Override
    public void onBackPressed() {
        System.out.println("exitButton click");
        //closeAccessory();
        // The safest way to close the application
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(0);
    }
}
