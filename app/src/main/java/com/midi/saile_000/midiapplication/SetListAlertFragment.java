package com.midi.saile_000.midiapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by saile_000 on 11.10.2014.
 */
public class SetListAlertFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("bla");
        builder.setItems(R.array.setListAlertItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //Intent intent = new Intent((SetListLibrary)getActivity().this, )
                        System.out.println("bla");


                } ;

            }
        });

        return builder.create();
    }

}
