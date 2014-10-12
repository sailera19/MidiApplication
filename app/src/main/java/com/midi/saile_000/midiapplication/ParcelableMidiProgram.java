package com.midi.saile_000.midiapplication;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by saile_000 on 11.10.2014.
 */
public class ParcelableMidiProgram extends MidiProgram implements Parcelable {
    public ParcelableMidiProgram(int msb, int lsb, int program) {
        super(msb, lsb, program);
        this.number = 0;
        this.name = "";
    }

    public ParcelableMidiProgram(int msb, int lsb, int program, int number, String name)
    {
        this(msb, lsb, program);
        this.number = number;
        this.name = name;
    }
    public ParcelableMidiProgram(MidiProgram midiProgram)
    {
        this(midiProgram.msb, midiProgram.lsb, midiProgram.program, midiProgram.number, midiProgram.name);
    }
    private ParcelableMidiProgram (Parcel in)
    {
        this(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(msb);
        parcel.writeInt(lsb);
        parcel.writeInt(program);
        parcel.writeInt(number);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<ParcelableMidiProgram> CREATOR = new Parcelable.Creator<ParcelableMidiProgram>()
    {
        public ParcelableMidiProgram createFromParcel (Parcel in)
        {
            return new ParcelableMidiProgram(in);
        }
        public ParcelableMidiProgram[] newArray(int size)
        {
            return new ParcelableMidiProgram[size];
        }
    };
}
