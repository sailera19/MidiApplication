package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;


public class SetListActivity extends Activity {
    private MidiReceiver myMidiReceiver = null;
    private ArrayList<MidiProgramGroup> groups;
    private ExpandableListView myListView;
    private ExpandableMidiProgramListAdapter myAdapter;
    private boolean firstResume = true;
    private SharedPreferences defaultSharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    private MidiProgramGroupIterator myIterator;


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
        myAdapter.notifyDataSetChanged();
    }

    public void changeIndex (int i, int newI, int group, int newGroup)
    {
        if (newI<0)
            newI = 0;
        ParcelableMidiProgram midiProgram = groups.get(group).children.get(i);
        groups.get(group).children.remove(i);
        if(newI <= groups.get(newGroup).children.size())
            groups.get(newGroup).children.add(newI, midiProgram);
    }

    public void deleteItem(int i, int group)
    {
        groups.get(group).children.remove(i);
    }

    public void deleteGroup(int group)
    {
        groups.remove(group);
        myAdapter = new ExpandableMidiProgramListAdapter(this, groups);
        myListView.setAdapter(myAdapter);
        expandAll();
    }

    public void newGroup (int i, String name)
    {
        groups.add(i, new MidiProgramGroup(name));
        myListView.expandGroup(i);
    }
    public void renameGroup (int i, String name)
    {
        groups.get(i).string = name;
    }
    public void changeGroupPosition(int groupPosition, int newGroupPosition)
    {
        if (newGroupPosition<0)
            newGroupPosition = 0;
        MidiProgramGroup midiProgramGroup = groups.get(groupPosition);
        groups.remove(groupPosition);
        if(newGroupPosition <= groups.size())
            groups.add(newGroupPosition, midiProgramGroup);
    }

    private void midiAlert ()
    {
        DialogFragment dialogFragment = new MidiAlertFragment();
        dialogFragment.show(getFragmentManager(), "midiAlert");
    }

    public void getGroupFromFile()
    {
        try {

            String myFilePath = defaultSharedPreferences.getString("setlistFile", null);

            if (myFilePath == null)
            {
                defaultSharedPreferences.edit().putString("setlistFile", getApplicationInfo().dataDir+"/sets/default").commit();
                File myFile = new File(getApplicationInfo().dataDir+"/sets/default");
                myFile.getParentFile().mkdirs();
                myFile.createNewFile();
                groups = null;
                return;
            }

            File myFile = new File(myFilePath);

            if(!myFile.exists())
            {
                myFile.getParentFile().mkdirs();
                myFile.createNewFile();
            }

            FileInputStream fileInputStream = new FileInputStream(myFile);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ArrayList<MidiProgramGroup> newGroups = ((ArrayList<MidiProgramGroup>)objectInputStream.readObject());

            if(groups==null)
                groups = new ArrayList<MidiProgramGroup>();
            else
                groups.clear();

            groups.addAll(newGroups);

            objectInputStream.close();

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            groups = null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            groups = null;
        }
    }

    public void writeGroupsToFile()
    {
        File dataDir = new File(getApplicationInfo().dataDir);

        File myFile = new File(defaultSharedPreferences.getString("setlistFile", null));

        String title = getTitle() + myFile.getName();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(groups);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void expandAll ()
    {

        for(int i = 0; i < groups.size(); i++)
        {
            myListView.expandGroup(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);

        MidiSystem.initialize(this);


        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {

            getGroupFromFile();

            if (groups==null) {
                groups = new ArrayList<MidiProgramGroup>();
                groups.add(new MidiProgramGroup("default program"));
                groups.get(0).children.add(new ParcelableMidiProgram(0, 0, 0, 0, "default without action"));
            }


            myAdapter = new ExpandableMidiProgramListAdapter(this, groups);

            myListView = (ExpandableListView) findViewById(R.id.setListView);

            myListView.setAdapter(myAdapter);

            expandAll();


            myListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                    try {
                        getMidiReceiver().changeProgram(groups.get(i).children.get(i2));
                        TextView errorView = (TextView) findViewById(R.id.errorView);
                        errorView.setText("" + i);
                    } catch (InvalidMidiDataException e) {
                        midiAlert();
                    } catch (MidiUnavailableException e) {
                        midiAlert();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        midiAlert();
                    }
                    return true;
                }
            });



            myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (ExpandableListView.getPackedPositionType(l) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        long packedPos = ((ExpandableListView) adapterView).getExpandableListPosition(i);

                        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);
                        int childPosition = ExpandableListView.getPackedPositionChild(packedPos);

                        DialogFragment dialogFragment = new SetListItemAlertFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("group", groupPosition);
                        System.out.println("Group position:" + groupPosition);
                        bundle.putInt("index", childPosition);
                        System.out.println("index" + childPosition);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getFragmentManager(), "setListItemAlert");

                        // You now have everything that you would as if this was an OnChildClickListener()
                        // Add your logic here.

                        // Return true as we are handling the event.
                        return true;
                    } else if (ExpandableListView.getPackedPositionType(l) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                        long packedPos = ((ExpandableListView) adapterView).getExpandableListPosition(i);

                        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPos);

                        System.out.println("Group Position: " + groupPosition);

                        DialogFragment dialogFragment = new SetListGroupAlertFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("groupPosition", groupPosition);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getFragmentManager(), "setListGroupAlert");

                        return true;
                    } else return false;

                }
            });

            Button startLiveModeButton = (Button) findViewById(R.id.startlivebutton);

            startLiveModeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SetListActivity.this, SetListLiveActivity.class);
                    intent.putExtra("iterator", new MidiProgramGroupIterator(groups));
                    startActivity(intent);
                }
            });

            final TextView setListName = (TextView) findViewById(R.id.setlistname);

            onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    if(s.equals("setlistFile"))
                    {
                        System.out.println("Called");
                        getGroupFromFile();
                        getProgramList();
                        String setlistFile = sharedPreferences.getString("setlistFile", "default");
                        setlistFile = setlistFile.substring(setlistFile.lastIndexOf('/')+1);
                        setListName.setText(setlistFile);
                    }
                }
            };
            defaultSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);



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
            Intent intent = new Intent(SetListActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.new_setlist)
        {
            DialogFragment dialogFragment = new NewSetListFragment();
            dialogFragment.show(getFragmentManager(), "newSetListAlert");
        }
        else if(id == R.id.change_setlist)
        {
            DialogFragment dialogFragment = new ChangeSetListFragment();
            dialogFragment.show(getFragmentManager(), "changeSetListAlert");
        }
        else if(id == R.id.delete_setlist)
        {
            DialogFragment dialogFragment = new DeleteSetListFragment();
            dialogFragment.show(getFragmentManager(), "deleteSetListAlert");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        MidiSystem.terminate();

        writeGroupsToFile();

        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult");
        if(resultCode==RESULT_OK)
        {
            System.out.println("RESULT_OK");
            ParcelableMidiProgram midiProgram = data.getParcelableExtra("program");
            int groupPosition = data.getIntExtra("group", 0);
            int index = data.getIntExtra("index", -1);
            System.out.println("group " + groupPosition + " index " + index);
            if(index>=0)
                groups.get(groupPosition).children.add(index, midiProgram);
            else
                groups.get(groupPosition).children.add(midiProgram);

            getProgramList();
        }
    }
}
