package com.midi.saile_000.midiapplication;

import java.io.Serializable;

/**
 * Created by saile_000 on 07.10.2014.
 */
public class MidiProgram implements Serializable {
    int program, msb, lsb, number;
    String name;
    public MidiProgram(int msb,int lsb, int program, int number, String name)
    {
        if (msb > 127)
            msb = -1;
        if (lsb > 127)
            lsb = -1;
        if (program > 127)
            program = -1;

        this.msb = msb;
        this.lsb = lsb;
        this.program = program;
        this.name = name;
        this.number = number;
    }
    public MidiProgram(int msb, int lsb, int program)
    {
        if (msb > 127)
            msb = -1;
        if (lsb > 127)
            lsb = -1;
        if (program > 127)
            program = -1;

        this.msb = msb;
        this.lsb = lsb;
        this.program = program;
    }
}
