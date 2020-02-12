package com.piercesmith.babystation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UpdateDialog extends AppCompatDialogFragment {
    EditText nTitle, nChild, nComments;
    RadioButton feeding, changing, milestone, other;
    String category;
    UpdateDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.update_dialog, null);

        builder.setView(v)
                .setTitle("Update Your Log")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update data
                        String title = nTitle.getText().toString();
                        String child = nChild.getText().toString();
                        getCategory();
                        String comments = nComments.getText().toString();
                        if (title.equals("")) {
                            Toast.makeText(getContext(), "Title cannot be empty, log not updated.", Toast.LENGTH_LONG).show();
                        }
                        else if (child.equals("")) {
                            Toast.makeText(getContext(), "You must enter your child's name, log not updated.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            listener.UpdateData(title, child, category, comments);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss dialog
                    }
                });
        nTitle = v.findViewById(R.id.txtUpdateTitle);
        nChild = v.findViewById(R.id.txtUpdateChild);
        feeding = v.findViewById(R.id.rbtnFeeding);
        changing = v.findViewById(R.id.rbtnChanging);
        milestone = v.findViewById(R.id.rbtnMilestone);
        other = v.findViewById(R.id.rbtnOther);
        nComments = v.findViewById(R.id.txtUpdateComments);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (UpdateDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCategory() {
        if (feeding.isChecked()) {
            category = "Feeding";
        }
        else if (changing.isChecked()) {
            category = "Changing";
        }
        else if (milestone.isChecked()) {
            category = "Milestone";
        }
        else if (other.isChecked()) {
            category = "Other";
        }
    }

    public interface UpdateDialogListener {
        void UpdateData(String title, String child, String category, String comments);
    }
}
