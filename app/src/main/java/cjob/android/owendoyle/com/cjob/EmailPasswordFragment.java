/*
* This is the DialogFragment for the dialog the prompts the user
* to enter their email and password for the email event
* */

package cjob.android.owendoyle.com.cjob;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;

/**
 * Created by Owner on 05/11/2015.
 */

public class EmailPasswordFragment extends DialogFragment {

    public static final String EXTRA_EMAIL = "com.email";
    public static final String EXTRA_PASS = "com.pass";

    private String email = "";
    private String pass = "";

    private EditText mEmailAddress;
    private EditText mPassword;
    private Button mCreateButton;


    // wires up the dialog fragment components
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_emailpassword,null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(v);

        mEmailAddress = (EditText) v.findViewById(R.id.user_email);
        mEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPassword = (EditText) v.findViewById(R.id.user_pass);
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.setTitle(R.string.dialog_emailpassword_title);
        mCreateButton = (Button) v.findViewById(R.id.dialog_create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            //makes sure user did not leave fields blank
            @Override
            public void onClick(View view) {
                if (email != "" && pass != "") {
                    sendResult(Activity.RESULT_OK, email, pass);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), R.string.event_empty_fields_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return dialog.create();
    }

    //sends the result back to its calling activity
    private void sendResult(int resultCode, String email, String pass) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASS, pass);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
