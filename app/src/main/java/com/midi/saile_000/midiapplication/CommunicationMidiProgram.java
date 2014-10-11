package com.midi.saile_000.midiapplication;

/**
 * Created by saile_000 on 11.10.2014.
 */
public class CommunicationMidiProgram {
    private static MidiProgram midiProgram;

    private static boolean used = true;

    public static MidiProgram getMidiProgram()
    {
        used = true;
        return midiProgram;
    }

    public static boolean isUsed ()
    {
        return used;
    }

    public static void setMidiProgram(MidiProgram newMidiProgram)
    {
        midiProgram = newMidiProgram;
        used = false;
    }


}
