package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("pc3k.csv")));
                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                {
                    String[] row = line.split(",");
                    myMidiProgramList.add(new MidiProgram(Integer.parseInt(row[0].trim()), Integer.parseInt(row[1].trim()), Integer.parseInt(row[2].trim()), Integer.parseInt(row[3].trim()), row[4]));

                }
                myAdapter.updateDataSet(myMidiProgramList);
                myListView.setAdapter(myAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }


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
