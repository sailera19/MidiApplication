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

import java.util.LinkedList;
import java.util.List;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;


public class SetListActivity extends Activity {
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
        myAdapter.updateDataSet(myMidiProgramList);
        myListView.setAdapter(myAdapter);
    }

    private void midiAlert ()
    {
        DialogFragment dialogFragment = new MidiAlertFragment();
        dialogFragment.show(getFragmentManager(), "midiAlert");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);

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

            myMidiProgramList = new LinkedList<MidiProgram>();

            myMidiProgramList.add(new MidiProgram(0,0,0,0, "bla"));

            myAdapter.updateDataSet(myMidiProgramList);

            myListView = (ListView) findViewById(R.id.setListView);

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
            myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DialogFragment dialogFragment = new SetListAlertFragment();
                    dialogFragment.show(getFragmentManager(), "setListAlert");
                    return true;
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
        getMenuInflater().inflate(R.menu.set_list, menu);
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

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onResume()
    {
        super.onDestroy();

        if(!CommunicationMidiProgram.isUsed())
        {
            myMidiProgramList.add(CommunicationMidiProgram.getMidiProgram());
            getProgramList();
        }
    }
}
