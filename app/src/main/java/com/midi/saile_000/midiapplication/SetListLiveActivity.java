package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;


public class SetListLiveActivity extends Activity {


    private TextView previousNumber;
    private TextView previousName;

    private TextView currentNumber;
    private TextView currentName;

    private TextView nextNumber;
    private TextView nextName;

    private Button nextButton;
    private Button previousButton;

    private MidiReceiver myMidiReceiver;

    MidiProgramGroupIterator  midiProgramGroupIterator;

    private boolean firstUsage = true;


    private MidiReceiver getMidiReceiver() throws MidiUnavailableException {
        if (myMidiReceiver == null)
        {
            //Midi Receiver initialisieren
            myMidiReceiver = new MidiReceiver();
        }

        return myMidiReceiver;
    }

    private void setPositionTexts ()
    {
        MidiProgram previous = midiProgramGroupIterator.showPrevious();

        MidiProgram current = midiProgramGroupIterator.showCurrent();

        MidiProgram next = midiProgramGroupIterator.showNext();

        int currentIndex = midiProgramGroupIterator.showItemIndex();

        if (previous!=null)
        {
            previousNumber.setText(""+(currentIndex - 1));
            previousName.setText(previous.name);
        }
        else
        {
            previousNumber.setText("0");
            previousName.setText("--------");
        }

        currentNumber.setText("" + currentIndex);
        currentName.setText(current.name);


        nextNumber.setText(""+(currentIndex + 1));
        if (next!=null)
        {
            nextName.setText(next.name);
        }
        else {
            nextName.setText("--------");
        }
    }

    private void midiAlert ()
    {
        DialogFragment dialogFragment = new MidiAlertFragment();
        dialogFragment.show(getFragmentManager(), "midiAlert");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list_live);

        MidiSystem.initialize(this);


        previousNumber = (TextView) findViewById(R.id.live_previous_number);
        previousName = (TextView) findViewById(R.id.live_previous_name);

        currentNumber = (TextView) findViewById(R.id.live_current_number);
        currentName = (TextView) findViewById(R.id.live_current_name);

        nextNumber = (TextView) findViewById(R.id.live_next_number);
        nextName = (TextView) findViewById(R.id.live_next_name);

        nextButton = (Button) findViewById(R.id.live_nextbutton);
        previousButton = (Button) findViewById(R.id.live_previousbutton);

        Intent intent = getIntent();

        midiProgramGroupIterator = intent.getParcelableExtra("iterator");

        previousNumber.setText("0");
        previousName.setText("----");


        currentNumber.setText("0");
        currentName.setText("----");

        nextNumber.setText("" + midiProgramGroupIterator.getCurrentIndex());
        nextName.setText(midiProgramGroupIterator.showCurrent().name);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidiProgram midiProgram;
                if(firstUsage) {
                    firstUsage = false;
                    setPositionTexts();
                    midiProgram = midiProgramGroupIterator.showCurrent();
                }
                else {
                    midiProgram = midiProgramGroupIterator.next();
                    if (midiProgram == null) return;
                    setPositionTexts();
                }
                try {

                    getMidiReceiver().change(midiProgram.msb, midiProgram.lsb, midiProgram.program);
                } catch (InvalidMidiDataException e) {
                    midiAlert();
                } catch (MidiUnavailableException e) {
                    midiAlert();
                } catch (ArrayIndexOutOfBoundsException e) {
                    midiAlert();
                }

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidiProgram midiProgram = midiProgramGroupIterator.previous();

                if(midiProgram==null) return;

                setPositionTexts();
                try {
                    getMidiReceiver().changeProgram(midiProgram);
                } catch (InvalidMidiDataException e) {
                    midiAlert();
                } catch (MidiUnavailableException e) {
                    midiAlert();
                } catch (ArrayIndexOutOfBoundsException e) {
                    midiAlert();
                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.set_list_live, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        MidiSystem.terminate();
    }

}
