package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class ExpandableMidiProgramListAdapter extends BaseExpandableListAdapter {
    private final LinkedList<MidiProgramGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableMidiProgramListAdapter(Activity act, LinkedList<MidiProgramGroup> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public MidiProgram getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_programlist, parent, false);
        }
        TextView programNumber = (TextView) convertView.findViewById(R.id.programlist_programNumber);
        TextView programName = (TextView) convertView.findViewById(R.id.programlist_programName);

        MidiProgram midiProgram = getChild(groupPosition, childPosition);

        programName.setText(midiProgram.name);

        programNumber.setText(""+midiProgram.number);



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        MidiProgramGroup midiProgramGroup = groups.get(groupPosition);
        List<MidiProgram> list = midiProgramGroup.children;

        return list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_group, null);
        }
        CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(R.id.checkedTextView);
        MidiProgramGroup group = (MidiProgramGroup) getGroup(groupPosition);
        checkedTextView.setText(group.string);
        checkedTextView.setChecked(isExpanded);
        ExpandableListView expandableListView = (ExpandableListView) parent;
        expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
