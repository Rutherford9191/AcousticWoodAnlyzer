package com.example.joshua.acoustics.GiutarTuner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.joshua.acoustics.GiutarTuner.util.PermissionUtils;

public class PermissionDialog extends DialogFragment {
    public static final String TAG = PermissionDialog.class.getSimpleName();

    public static PermissionDialog newInstance(){
        return new PermissionDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("GuitarTuner needs access your microphone");
        builder.setMessage("GuitarTuner cannot work without access to the microphone.");
        builder.setPositiveButton("Ask for permission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtils.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, TunerFragment.AUDIO_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        return builder.create();
    }

}
