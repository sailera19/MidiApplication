package com.midi.saile_000.midiapplication;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by saile_000 on 13.10.2014.
 */
public class MidiProgramGroupIterator implements ListIterator<MidiProgram> {
    private List<MidiProgramGroup> midiProgramGroups;
    private int[] currentPosition;

    public MidiProgramGroupIterator(List<MidiProgramGroup> midiProgramGroups)
    {
        this.midiProgramGroups = midiProgramGroups;
        currentPosition = new int[2];
        currentPosition[0] = 0;
        currentPosition[1] = 0;
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
    public void add(MidiProgram midiProgram) {
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
    public MidiProgram next() {
        if(!hasNext())
            return midiProgramGroups.get(currentPosition[0]).children.get(currentPosition[1]);

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
        int index = 0;
        for (int i = 0; i <= currentPosition[0]; i++)
        {
            for (int j = 0; j < currentPosition[1]; j++)
            {
                index++;
            }
        }
        return index+1;
    }

    @Override
    public MidiProgram previous() {
        System.out.println(""+currentPosition[0] + currentPosition[1]);
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
    public void set(MidiProgram midiProgram) {
        midiProgramGroups.get(currentPosition[0]).children.set(currentPosition[1], midiProgram);
    }
}
