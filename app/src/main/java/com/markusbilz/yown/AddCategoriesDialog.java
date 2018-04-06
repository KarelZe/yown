package com.markusbilz.yown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AddCategoriesDialog extends DialogFragment {

    private AddDetailsDialogListener listener;
    @Nullable
    private String title;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        id = getArguments().getInt("id");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] items = {getString(R.string.category_0), getString(R.string.category_1), getString(R.string.category_2), getString(R.string.category_3)};
        builder
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        listener.getDetails(items[item].toString(), id);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    /**
     * Activity has to implement AddCategoriesDialogListener, otherwise ClassCastException will be
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
