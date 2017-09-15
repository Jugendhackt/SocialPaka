package me.simonbohnen.socialpaka;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Simon on 14.09.2017.
 * A dialog which asks the user to choose a slack user from a list of available ones
 */

public class SelectUserDialogFragment extends DialogFragment {
    String[] listitems;
    Context context;
    String title;

    void supplyArguments(ArrayList<String> matches, Context ncontext, String ntitle) {
        Log.d("Debug", Integer.toString(matches.size()));
        listitems = matches.toArray(new String[matches.size()]);
        context = ncontext;
        title = ntitle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setItems(listitems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userid = MainActivity.nameToUserID.get(listitems[i]);
                AccountDetailActivity.userID = userid;
                AccountDetailActivity.user = MainActivity.userInfo.get(userid);
                context.startActivity(new Intent(context, AccountDetailActivity.class));
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OcrDetectorProcessor.hasDetected = false;
    }
}
