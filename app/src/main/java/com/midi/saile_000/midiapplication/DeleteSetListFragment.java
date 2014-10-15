package com.midi.saile_000.midiapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.Set;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class DeleteSetListFragment extends DialogFragment {
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {

        File dataDir = new File(getActivity().getApplicationInfo().dataDir + "/sets");
        final File[] files = dataDir.listFiles();
        if (files == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Zuerst neue Setlist erstellen");
            builder.setCancelable(true);
            builder.setNegativeButton(R.string.delete_setlist, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            return builder.create();
        }
        final String[] fileNames = new String[files.length];
        final String[] filePaths = new String[files.length];
        for (int i = 0; i < files.length; i++)
        {
            fileNames[i] = files[i].getName();
            filePaths[i] = files[i].getAbsolutePath();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.id.delete_setlist);
        builder.setItems(fileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SetListActivity setListActivity = (SetListActivity) getActivity();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(setListActivity);
                String currentFile = sharedPreferences.getString("setlistFile", null);
                if(fileNames[i] == currentFile || currentFile == null)
                {
                    if (fileNames.length > 0 || i != 0)
                        sharedPreferences.edit().putString("setlistFile", filePaths[0]).commit();
                    else if (fileNames.length > 0 || i == 0)
                        sharedPreferences.edit().putString("setlistFile", filePaths[1]).commit();
                    else
                        sharedPreferences.edit().putString("setlistFile", "default").commit();
                }
                files[i].delete();
            }
        });

        return builder.create();
    }
}
