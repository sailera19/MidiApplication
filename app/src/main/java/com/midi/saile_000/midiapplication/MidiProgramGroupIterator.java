package com.midi.saile_000.midiapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by saile_000 on 13.10.2014.
 */
public class MidiProgramGroupIterator implements ListIterator<ParcelableMidiProgram>, Parcelable {
    private List<MidiProgramGroup> midiProgramGroups;
    private int[] currentPosition;

    public MidiProgramGroupIterator(List<MidiProgramGroup> midiProgramGroups)
    {
        this.midiProgramGroups = midiProgramGroups;
        currentPosition = new int[2];
        currentPosition[0] = 0;
        currentPosition[1] = 0;
    }

    public MidiProgramGroupIterator(Parcel in) {
        this.midiProgramGroups = new LinkedList<MidiProgramGroup>();
        in.readTypedList(midiProgramGroups, MidiProgramGroup.CREATOR);
        currentPosition = new int[2];
        in.readIntArray(currentPosition);
    }

    public int getCurrentGroup ()
    {
        return currentPosition[0];
    }

    public int getCurrentIndex ()
    {
        return currentPosition[1];
    }

    public int getPosition ()
    {
        int index = 0;
        for (int i = 0; i <= currentPosition[0]; i++)
        {
            if (i==currentPosition[0]) {
                for (int j = 0; j < currentPosition[1]; j++) {
                    index++;
                }
            }
            else{
                for (int j = 0; j < midiProgramGroups.get(i).children.size(); j++)
                {
                    index++;
                }
            }

            index++;
        }
        return index;
    }

    @Override
    public void add(ParcelableMidiProgram midiProgram) {
        midiProgramGroups.get(midiProgramGroups.size()).children.get(midiProgramGroups.get(midiProgramGroups.size()).children.size());
    }

    @Override
    public boolean hasNext() {
        if(currentPosition[1]+ 1 < (midiProgramGroups.get(currentPosition[0]).children.size() - 1)|| (currentPosition[0]+1 < midiProgramGroups.size())&&(midiProgramGroups.get(currentPosition[0]+1).children.size() > 0))
          return true;
        else
            return false;
    }


    @Override
    public boolean hasPrevious() {
        if(currentPosition[1] > 0 || (currentPosition[1] == 0 && currentPosition[0] > 0 && midiProgramGroups.get(currentPosition[0]-1).children.size() > 0))
            return true;
        else
            return false;
    }

    @Override
    public ParcelableMidiProgram next() {
        if(!hasNext())
            return null;

        if(currentPosition[1] < midiProgramGroups.get(currentPosition[0]).children.size() -1) {
            currentPosition[1] = currentPosition[1] + 1;
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1]);
        }
        else {
            currentPosition[0] = currentPosition[0] + 1;
            currentPosition[1] = 0;
            return midiProgramGroups.get(currentPosition[0]).children.get(0);
        }
    }

    @Override
    public int nextIndex() {
        return getPosition()+1;
    }

    @Override
    public ParcelableMidiProgram previous() {
        if(!hasPrevious())
            return null;
        if(currentPosition[1]==0)
        {
            currentPosition[0] = currentPosition[0] - 1;

            currentPosition[1] = midiProgramGroups.get(currentPosition[0]).children.size() - 1;
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1]);
        }
        else
        {
            currentPosition[1] = currentPosition[1] - 1;
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1]);
        }
    }

    @Override
    public int previousIndex() {
        int index = 0;
        for (int i = 0; i <= currentPosition[0]; i++)
        {
            for (int j = 0; j < currentPosition[1]; j++)
            {
                index++;
            }
        }
        return index - 1;
    }

    @Override
    public void remove() {
        midiProgramGroups.get(currentPosition[0]).children.remove(currentPosition[1]);
    }

    @Override
    public void set(ParcelableMidiProgram midiProgram) {
        midiProgramGroups.get(currentPosition[0]).children.set(currentPosition[1], midiProgram);
    }

    public MidiProgram showCurrent()
    {
        return midiProgramGroups.get(getCurrentGroup()).children.get(getCurrentIndex());
    }

    public MidiProgram showNext()
    {
        if(!hasNext())
            return null;

        if(currentPosition[1] < midiProgramGroups.get(currentPosition[0]).children.size() -1) {
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1] + 1);
        }
        else {
            return midiProgramGroups.get(currentPosition[0]+1).children.get(0);
        }
    }

    public MidiProgram showPrevious()
    {
        if(!hasPrevious())
            return null;
        if(currentPosition[1]==0)
        {
            int previousGroup = currentPosition[0] - 1;

            int previousIndex = midiProgramGroups.get(currentPosition[0]).children.size() - 1;
            return midiProgramGroups.get(previousGroup).children.get(previousIndex);
        }
        else
        {
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1]-1);
        }
    }

    public String showCurrentGroup()
    {
        return midiProgramGroups.get(getCurrentGroup()).string;
    }

    public int showItemIndex ()
    {
        int index = 1;
        for (int i = 0; i <= currentPosition[0]; i++)
        {
            if (i==currentPosition[0]) {
                for (int j = 0; j < currentPosition[1]; j++) {
                    index++;
                }
            }
            else{
                for (int j = 0; j < midiProgramGroups.get(i).children.size(); j++)
                {
                    index++;
                }
            }
        }
        return index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(midiProgramGroups);
        parcel.writeIntArray(currentPosition);

    }

    public static final Parcelable.Creator<MidiProgramGroupIterator> CREATOR = new Parcelable.Creator<MidiProgramGroupIterator>()
    {
        public MidiProgramGroupIterator createFromParcel (Parcel in)
        {
            return new MidiProgramGroupIterator(in);
        }
        public MidiProgramGroupIterator[] newArray(int size)
        {
            return new MidiProgramGroupIterator[size];
        }
    };
}
