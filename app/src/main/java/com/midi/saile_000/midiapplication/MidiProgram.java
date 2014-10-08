package com.midi.saile_000.midiapplication;

/**
 * Created by saile_000 on 07.10.2014.
 */
public class MidiProgram {
    int program, bank, number;
    String name;
    public MidiProgram(int number, int program, int bank, String name)
    {
        this.program = program;
        this.bank = bank;
        this.name = name;
        this.number = number;
    }
}
