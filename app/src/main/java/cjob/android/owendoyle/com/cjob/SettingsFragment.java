package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Owner on 30/10/2015.
 */
public class SettingsFragment extends Fragment {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_EVENT_TYPE = "event_id";
    private static final int REQUEST_CONTACT = 0;

    private String event_title = "";
    private String text_message = "";
    private String email_to = "";
    private String email_subject = "";
    private String contact_name = "";
    private String cotact_number = "";
    private int event_radius = 10;
    private boolean deleteOnComplete = true;

    private SeekBar mRadiusBar;
    private TextView mRadiusBarProgress;
    private EditText mEventTitle;
    private Button mChooseContactButton;
    private EditText mEmailTo;
    private EditText mEmailSubject;
    private EditText mTextMessage;
    private CheckBox mDeleteOnComplete;
    private TextView mDebugText;

    @Override
    public void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(getArguments().get(ARG_EVENT_TYPE) == 1){
            View v = inflater.inflate(R.layout.alarm_settings,container,false);
            editTitle(v);
            radiusPicker(v);
            deleteOnCompleteCheck(v);
            return v;
        }
        else if(getArguments().get(ARG_EVENT_TYPE) == 2){
            View v = inflater.inflate(R.layout.sms_settings,container,false);
            editTitle(v);
            radiusPicker(v);
            chooseContact(v);
            textMessage(v);
            deleteOnCompleteCheck(v);
            return v;
        }
        else if(getArguments().get(ARG_EVENT_TYPE) == 3){
            View v = inflater.inflate(R.layout.alarm_settings,container,false);
            editTitle(v);
            radiusPicker(v);
            deleteOnCompleteCheck(v);
            return v;
        }
        else{
            View v = inflater.inflate(R.layout.email_settings,container,false);
            editTitle(v);
            radiusPicker(v);
            emailToFrom(v);
            textMessage(v);
            deleteOnCompleteCheck(v);
            return v;
        }

    }

    public static SettingsFragment newInstance(double latitude, double longitude, String address, int event_type) {
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putString(ARG_ADDRESS, address);
        args.putInt(ARG_EVENT_TYPE, event_type);

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void editTitle(View v){
        mEventTitle = (EditText) v.findViewById(R.id.event_title_text);
        mEventTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                event_title = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void radiusPicker(View v){
        mRadiusBar = (SeekBar) v.findViewById(R.id.alarm_radius_seekbar);
        mRadiusBarProgress = (TextView) v.findViewById(R.id.seekbar_progress_text_view);
        mRadiusBarProgress.setText("" + (mRadiusBar.getProgress() + 10) + "m");
        mRadiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    i = 1;
                }
                mRadiusBarProgress.setText("" + i * 10 + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                event_radius = (seekBar.getProgress() * 10);
            }
        });
    }

    private void chooseContact(View v){
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mChooseContactButton = (Button) v.findViewById(R.id.choose_contact_button);
        mChooseContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
    }

    private void emailToFrom(View v){
        mEmailTo = (EditText) v.findViewById(R.id.event_email_to);
        mEmailSubject = (EditText) v.findViewById(R.id.event_email_subject);
        mEmailTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_to = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEmailSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_subject = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void textMessage(View v){
        mTextMessage = (EditText) v.findViewById(R.id.event_text_message);
        mTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_message = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void deleteOnCompleteCheck(View v){
        mDeleteOnComplete = (CheckBox) v.findViewById(R.id.event_delete_on_complete);
        mDeleteOnComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                deleteOnComplete = b;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try{
                if(c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                String recieverid = c.getString(0);
                String reciever = c.getString(1);
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()){
                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            // do something with the Home number here...
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            mChooseContactButton.setText(reciever + ": " + number);
                            contact_name = reciever;
                            cotact_number = number;
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            // do something with the Work number here...
                            break;
                    }
                }
                phones.close();


            }finally {
                c.close();
            }
        }
    }

}
