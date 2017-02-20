package com.kt.iot.mobile.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by NP1014425901 on 2015-03-13.
 */
public class AbstractFragment extends Fragment {

    private static final String TAG = AbstractFragment.class.getSimpleName();

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // created

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(this.getClass().getSimpleName(), "onAttach...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.getClass().getSimpleName(), "onCreate...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getSimpleName(), "onCreateView...");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // started

    @Override
    public void onStart() {
        super.onStart();
        Log.i(this.getClass().getSimpleName(), "onStart...");
    }

    // resumed

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.getClass().getSimpleName(), "onResume...");
    }

    // paused

    @Override
    public void onPause() {
        super.onPause();
        Log.i(this.getClass().getSimpleName(), "onPause...");
    }

    // stopped

    @Override
    public void onStop() {
        super.onStop();
        Log.i(this.getClass().getSimpleName(), "onStop...");
    }

    // destroyed

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(this.getClass().getSimpleName(), "onDestroyView...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getSimpleName(), "onDestroy...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(this.getClass().getSimpleName(), "onDetach...");
    }
}
