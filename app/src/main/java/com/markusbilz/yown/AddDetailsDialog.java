package com.markusbilz.yown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by mail on 28/02/2018.
 */

public class AddDetailsDialog extends AppCompatDialogFragment {

    private EditText etDetails;
    private AddDetailsDialogListener listener;
    private String title;
    private String hint;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        hint = getArguments().getString("hint");
        id = getArguments().getInt("id");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_detail, null);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String details = etDetails.getText().toString();
                        listener.getDetails(details, id);
                    }
                });
        etDetails = view.findViewById(R.id.et_add_details_dialog);
        etDetails.setHint(hint);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddDetailsDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException();
        }
    }

    public interface AddDetailsDialogListener {
        void getDetails(String details, int id);
    }
}
