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
public class SetListGroupAlertFragment extends DialogFragment {

    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setItems(R.array.setListGroupAlertItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int groupPosition = getArguments().getInt("groupPosition");
                SetListActivity setListActivity = (SetListActivity) getActivity();
                switch (i) {
                    case 0:
                        setListActivity.newGroup(groupPosition+1, "Neue Gruppe");
                        setListActivity.getProgramList();
                        break;
                    case 1:
                        setListActivity.deleteGroup(groupPosition);
                        setListActivity.getProgramList();
                        break;
                    case 2:
                        setListActivity.changeGroupPosition(groupPosition, groupPosition-1);
                        setListActivity.getProgramList();
                        break;
                    case 3:
                        setListActivity.changeGroupPosition(groupPosition, groupPosition+1);
                        setListActivity.getProgramList();
                        break;
                    case 4:
                        DialogFragment dialogFragment = new SetListRenameGroupFragment();
                        dialogFragment.setArguments(getArguments());
                        dialogFragment.show(getFragmentManager(), "setListRenameGroupAlert");
                        break;
                    case 5:
                        Intent intent = new Intent(setListActivity, SetListLibrary.class);
                        intent.putExtra("groupPosition", groupPosition);
                        getActivity().startActivityForResult(intent, 1);


                } ;

            }
        });

        return builder.create();
    }

}
