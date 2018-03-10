package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDetailsDialog extends DialogFragment {

    private EditText etDetails;
    private AddDetailsDialogListener listener;
    @Nullable
    private String title;
    @Nullable
    private String hint;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        hint = getArguments().getString("hint");
        id = getArguments().getInt("id");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.dialog_detail, null);
        etDetails = view.findViewById(R.id.et_add_details_dialog);
        etDetails.setHint(hint);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String details = etDetails.getText().toString();
                        listener.getDetails(details, id);
                    }
                });
        return builder.create();

    }

    /**
     * Activity has to implement AddDetailsDialogListener, otherwise ClassCastException will be
     * thrown.
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddDetailsDialogListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException();
        }
    }

    /**
     * Make use of observer pattern to pass data between Activity and dialog.
     */
    public interface AddDetailsDialogListener {
        void getDetails(String details, int id);
    }
}
