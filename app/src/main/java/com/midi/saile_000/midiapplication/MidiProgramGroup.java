package com.midi.saile_000.midiapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class MidiProgramGroup implements Serializable, Parcelable{
    public String string;
    public final List<ParcelableMidiProgram> children = new ArrayList<ParcelableMidiProgram>();
    public MidiProgramGroup(String string)
    {
        this.string = string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public  MidiProgramGroup()
    {

    }

    public MidiProgramGroup (Parcel in)
    {
        in.readTypedList(children, ParcelableMidiProgram.CREATOR);
        string = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(children);
        parcel.writeString(string);

    }

    public static final Parcelable.Creator<MidiProgramGroup> CREATOR = new Parcelable.Creator<MidiProgramGroup>()
    {
        public MidiProgramGroup createFromParcel (Parcel in)
        {
            return new MidiProgramGroup(in);
        }
        public MidiProgramGroup[] newArray(int size)
        {
            return new MidiProgramGroup[size];
        }
    };

    @Override
    public String toString()
    {
        return string;
    }
}
