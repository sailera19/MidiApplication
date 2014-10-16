package com.midi.saile_000.midiapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by saile_000 on 12.10.2014.
 */
public class SetListRenameGroupFragment extends DialogFragment {


    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.string.renamegroup);
        final EditText userInput = new EditText(getActivity());
        builder.setView(userInput);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                SetListActivity setListActivity = ((SetListActivity)getActivity());
                int groupPosition = getArguments().getInt("groupPosition");
                setListActivity.renameGroup(groupPosition, userInput.getText().toString());
                setListActivity.getProgramList();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        return builder.create();
    }

}
