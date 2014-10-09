package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;

public class MainActivity extends Activity {

    private MidiReceiver myMidiReceiver = null;

    private MidiReceiver getMidiReceiver() throws MidiUnavailableException {
        if (myMidiReceiver == null)
        {
            //Midi Receiver initialisieren
            myMidiReceiver = new MidiReceiver();
        }

        return myMidiReceiver;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MidiSystem.initialize(this);


        try {

            final ToggleButton getDeviceButton = (ToggleButton) findViewById(R.id.getDeviceButton);

            Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);

            final EditText programNumber = (EditText) findViewById(R.id.programNumber);

            final TextView myText = (TextView) findViewById(R.id.myText);

            getDeviceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        getMidiReceiver();
                    } catch (MidiUnavailableException e) {
                        myText.setText(e.getMessage());
                    }
                }
            });


            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        myMidiReceiver.kurzweilChangeProgram(Integer.parseInt(programNumber.getText().toString()));
                    } catch (InvalidMidiDataException e) {
                        myText.setText(e.getMessage());
                    }
                }
            });




        }
        catch (Exception e)
        {
            final TextView deviceList = (TextView) findViewById(R.id.myText);

            deviceList.setText(e.getMessage());
        }

        ;

        Button newActivity = (Button) findViewById(R.id.newActivity);
        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Library.class);
                startActivity(intent);
            }
        });




    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        MidiSystem.terminate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
