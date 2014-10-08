package com.midi.saile_000.midiapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by saile_000 on 07.10.2014.
 */
public class MidiProgramListAdapter extends BaseAdapter {

    private List<MidiProgram> midiProgramList = Collections.emptyList();

    private final Context context;

    public MidiProgramListAdapter (Context context)
    {
        this.context = context;
    }

    public void updateDataSet(List<MidiProgram> midiProgramList)
    {
        this.midiProgramList = midiProgramList;
    }
    @Override
    public int getCount() {
        return midiProgramList.size();
    }

    @Override
    public Object getItem(int i) {
        return midiProgramList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_programlist, parent, false);
        }
        TextView programNumber = (TextView) convertView.findViewById(R.id.programlist_programNumber);
        TextView programName = (TextView) convertView.findViewById(R.id.programlist_programName);

        MidiProgram midiProgram = (MidiProgram) getItem(i);

        programName.setText(midiProgram.name);

        programNumber.setText(""+midiProgram.number);



        return convertView;
    }
}
