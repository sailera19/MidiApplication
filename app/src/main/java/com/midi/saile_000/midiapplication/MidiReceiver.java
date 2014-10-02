package com.midi.saile_000.midiapplication;



import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiDevice;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;

/*
 * Created by Alexander on 13.09.2014.
 */
public class MidiReceiver  {

    MidiDevice myMidiDevice;
    static MidiDevice.Info[] deviceInfoList;

    Receiver myReceiver;

    public MidiReceiver (MidiDevice midiDevice) throws MidiUnavailableException {
            setMidiDevice(midiDevice);
        }
    public MidiReceiver () throws MidiUnavailableException {
        this(MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[0]));
    }
    public MidiReceiver (int i) throws MidiUnavailableException {
        this();
        try {
            setMidiDevice(MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[i]));
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            setMidiDevice(MidiSystem.getMidiDevice(MidiSystem.getMidiDeviceInfo()[0]));
        }
    }

    public void setMidiDevice (MidiDevice midiDevice) throws MidiUnavailableException {
            this.myMidiDevice = midiDevice;

            this.myMidiDevice.open();

            myReceiver = myMidiDevice.getReceiver();
        }

    public void setMidiDevice(int i) throws MidiUnavailableException
    {
        setMidiDevice(MidiSystem.getMidiDevice(deviceInfoList[i]));
    }




    public void sendMessage(MidiMessage a)
        {
             myReceiver.send(a, -1);
        }

    public void changeProgram (int program) throws InvalidMidiDataException
    {
        ShortMessage myMidiMessage = new ShortMessage();

        if (program>127)
        {
            program = 0;
        }

        myMidiMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 0, program, 0);

        sendMessage(myMidiMessage);
    }

    public void changeBank (int bank) throws InvalidMidiDataException {
        ShortMessage myMidiMessage = new ShortMessage();

        if(bank>127) {
            bank = 0;
        }

        myMidiMessage.setMessage(ShortMessage.CONTROL_CHANGE, 0, 0, bank);

        sendMessage(myMidiMessage);
    }

    public void kurzweilChangeProgram(int program) throws InvalidMidiDataException {
        int bank = program/128;

        program = program % 128;

        changeBank(bank);

        changeProgram(program);

    }

    public static String getDeviceList()
    {
        deviceInfoList = MidiSystem.getMidiDeviceInfo();
        String deviceList = "";

        for (int i = 0; i< deviceInfoList.length; i++)
        {
            deviceList+= "\n" + i + deviceInfoList[i].getName();
        }

        return deviceList;
    }

    public void sendNote(int key, int velocity) throws InvalidMidiDataException {
        ShortMessage myMidiMessage = new ShortMessage();

        myMidiMessage.setMessage(ShortMessage.NOTE_ON, 0, key, velocity);

        sendMessage(myMidiMessage);
    }



}
