package com.midi.saile_000.midiapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiSystem;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;

public class MainActivity extends Activity {

    private MidiReceiver myMidiReceiver = null;

    private MidiReceiver getMidiReceiver() throws MidiUnavailableException {
        if (myMidiReceiver == null)
        {
            //Midi Receiver initialisieren
            myMidiReceiver = new MidiReceiver();
        }

        return myMidiReceiver;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MidiSystem.initialize(this);


        final Button sendMessageButton = (Button) findViewById(R.id.sendMessageButton);

        final Button startLibrary = (Button) findViewById(R.id.startLibrary);

        final Button startSetList = (Button) findViewById(R.id.startSetlist);

        final EditText pcInput = (EditText) findViewById(R.id.PCInput);

        final EditText msbInput = (EditText) findViewById(R.id.MSBInput);

        final EditText lsbInput = (EditText) findViewById(R.id.LSBInput);

        final TextView pcText = (TextView) findViewById(R.id.PCText);

        final TextView msbText = (TextView) findViewById(R.id.MSBText);

        final TextView lsbText = (TextView) findViewById(R.id.LSBText);

        final TextView textViewIntro = (TextView) findViewById(R.id.textViewIntro);





        sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String msbOutput = msbInput.getText().toString();
                        if (msbOutput.isEmpty())
                            msbOutput = "-1";
                        String lsbOutput = lsbInput.getText().toString();
                        if (lsbOutput.isEmpty())
                            lsbOutput = "-1";
                        String pcOutput = pcInput.getText().toString();
                        if (pcOutput.isEmpty())
                            pcOutput = "-1";

                        int msb = Integer.parseInt(msbOutput);
                        int lsb = Integer.parseInt(lsbOutput);
                        int pc = Integer.parseInt(pcOutput);
                        getMidiReceiver().change(msb, lsb, pc);


                    } catch (Exception e) {
                        /**catch (InvalidMidiDataException e) {
 } catch (MidiUnavailableException e) {
 }**/
                    }
                }
            });






        Button newActivity = (Button) findViewById(R.id.startLibrary);
        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Library.class);
                startActivity(intent);
            }
        });




    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        MidiSystem.terminate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

}
