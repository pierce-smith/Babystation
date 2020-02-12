package com.piercesmith.babystation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChildDialog extends AppCompatDialogFragment {
    EditText cName;
    ChildDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.child_dialog, null);

        builder.setView(v)
                .setTitle("Enter a child's name to see their logs.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update data
                        String name = cName.getText().toString();
                        if (name.equals("")) {
                            Toast.makeText(getContext(), "No name entered.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            listener.displayChild(name);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss dialog
                    }
                });
        cName = v.findViewById(R.id.txtSortChild);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ChildDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ChildDialogListener {
        void displayChild(String name);
    }
}
