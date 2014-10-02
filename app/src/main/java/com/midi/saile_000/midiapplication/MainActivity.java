package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;

public class MainActivity extends Activity {

    private MidiReceiver myMidiReceiver = null;

    private MidiReceiver getMidiReceiver() throws MidiUnavailableException {
        if (myMidiReceiver == null)
        {
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

            Button getDeviceListButton = (Button) findViewById(R.id.getDeviceListButton);

            Button getDeviceButton = (Button) findViewById(R.id.getDeviceButton);

            Button sendNoteButton = (Button) findViewById(R.id.sendNoteButton);

            Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);

            final TextView deviceList = (TextView) findViewById(R.id.deviceList);

            final EditText deviceNumber = (EditText) findViewById(R.id.deviceNumber);

            final EditText programNumber = (EditText) findViewById(R.id.programNumber);

            getDeviceListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        getMidiReceiver();
                        deviceList.setText(MidiReceiver.getDeviceList());
                    } catch (MidiUnavailableException e) {
                        deviceList.setText(e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                        deviceList.setText(e.getMessage());
                    }

                }
            });

            getDeviceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        myMidiReceiver.setMidiDevice(Integer.parseInt(deviceNumber.getText().toString()));
                    } catch (MidiUnavailableException e) {
                        deviceList.setText(e.getMessage());
                    }
                }
            });

            sendNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        myMidiReceiver.sendNote(36, 64);
                    } catch (InvalidMidiDataException e) {
                        deviceList.setText(e.getMessage());
                    }
                }
            });

            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        myMidiReceiver.kurzweilChangeProgram(Integer.parseInt(programNumber.getText().toString()));
                    } catch (InvalidMidiDataException e) {
                        deviceList.setText(e.getMessage());
                    }
                }
            });




        }
        catch (Exception e)
        {
            final TextView deviceList = (TextView) findViewById(R.id.deviceList);

            deviceList.setText(e.getMessage());
        }

        ;


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
