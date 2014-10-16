package com.midi.saile_000.midiapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class NewSetListFragment extends DialogFragment {
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        SetListActivity setListActivity = (SetListActivity) getActivity();
        setListActivity.writeGroupsToFile();
        final File dataDir = new File(getActivity().getApplicationInfo().dataDir + "/sets");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.id.delete_setlist);
        final EditText editText = new EditText(getActivity());
        builder.setView(editText);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editTextString = editText.getText().toString();
                if (editTextString.isEmpty())
                    editTextString = "default";
                File newFile = new File(dataDir, editTextString);
                if(!newFile.exists())
                {
                    newFile.getParentFile().mkdirs();
                    try {
                        newFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        return builder.create();
    }
}
