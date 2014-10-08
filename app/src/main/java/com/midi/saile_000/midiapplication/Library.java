package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;


public class Library extends Activity {
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
        setContentView(R.layout.activity_library);
        MidiSystem.initialize(this);
        try {

            MidiProgramListAdapter myAdapter = new MidiProgramListAdapter(this.getApplicationContext());

            final List<MidiProgram> myMidiProgramList = new LinkedList<MidiProgram>();

            myMidiProgramList.add(new MidiProgram(1, 1, 0, "Piano"));

            myMidiProgramList.add(new MidiProgram(2, 1, 0, "blabla"));

            myMidiProgramList.add(new MidiProgram(3, 1, 0, "blubba"));


            myAdapter.updateDataSet(myMidiProgramList);

            ListView myListView = (ListView) findViewById(R.id.listView);

            myListView.setAdapter(myAdapter);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    myMidiProgramList.get(i);

                    try {
                        getMidiReceiver().changeProgram(myMidiProgramList.get(i));
                        TextView errorView = (TextView) findViewById(R.id.errorView);
                        errorView.setText("" + i);
                    } catch (InvalidMidiDataException e) {
                        TextView errorView = (TextView) findViewById(R.id.errorView);
                        errorView.setText(e.getMessage());
                    } catch (MidiUnavailableException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            TextView errorView = (TextView) findViewById(R.id.errorView);
            errorView.setText(e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.library, menu);
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
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        MidiSystem.terminate();
    }

}
