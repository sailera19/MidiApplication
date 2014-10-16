package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;



public class Library extends Activity {
    private MidiReceiver myMidiReceiver = null;
    private List<MidiProgram> myMidiProgramList;
    private ListView myListView;
    private MidiProgramListAdapter myAdapter;
    private boolean firstResume = true;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;


    private MidiReceiver getMidiReceiver() throws MidiUnavailableException {
        if (myMidiReceiver == null)
        {
            //Midi Receiver initialisieren
            myMidiReceiver = new MidiReceiver();
        }

        return myMidiReceiver;
    }
    public void getProgramList ()
    {
        if (sharedPreferences == null) return;
        try {
            String assetString = sharedPreferences.getString("programList", "pc3k.csv");
            InputStream inputStream = getAssets().open(assetString);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            myMidiProgramList.clear();
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
            {
                String[] row = line.split(",");
                myMidiProgramList.add(new MidiProgram(Integer.parseInt(row[0].trim()), Integer.parseInt(row[1].trim()), Integer.parseInt(row[2].trim()), Integer.parseInt(row[3].trim()), row[4]));

            }
            myAdapter.updateDataSet(myMidiProgramList);
            myListView.setAdapter(myAdapter);
        } catch (IOException e) {

        }

    }

    private void midiAlert ()
    {
        DialogFragment dialogFragment = new MidiAlertFragment();
        dialogFragment.show(getFragmentManager(), "midiAlert");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_library);

        MidiSystem.initialize(this);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                getProgramList();
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        try {

            myAdapter = new MidiProgramListAdapter(this.getApplicationContext());

            myMidiProgramList = new ArrayList<MidiProgram>();

            myAdapter.updateDataSet(myMidiProgramList);

            myListView = (ListView) findViewById(R.id.programlistviewList);

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
                        midiAlert();
                    } catch (MidiUnavailableException e) {
                        midiAlert();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        midiAlert();
                    }

                }
            });

            getProgramList();


        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            midiAlert();
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
            Intent intent = new Intent(Library.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        MidiSystem.terminate();

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

}
