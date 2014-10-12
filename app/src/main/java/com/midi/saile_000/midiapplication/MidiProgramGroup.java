package com.midi.saile_000.midiapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class MidiProgramGroup implements Serializable {
    public String string;
    public final List<MidiProgram> children = new ArrayList<MidiProgram>();
    public MidiProgramGroup(String string)
    {
        this.string = string;
    }
}
