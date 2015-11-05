package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cjob.android.owendoyle.com.cjob.events.EventManager;

/**
 * Created by Owner on 28/10/2015.
 */
public class EventTypeFragment extends Fragment {

    public static final  String EXTRA_EVENT_TYPE = "com.event_type";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_ADDRESS = "address";

    private RecyclerView mTypeRecyclerView;
    private EventTypeAdapter mAdapter;
    private double latitude;
    private double longitude;
    private String address;

    @Override
    public void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
        latitude = (double) getArguments().getDouble(ARG_LATITUDE);
        longitude = (double) getArguments().getDouble(ARG_LONGITUDE);
        address = (String) getArguments().getString(ARG_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_type,container,false);

        mTypeRecyclerView = (RecyclerView) v.findViewById(R.id.event_type_recycle_view);
        mTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI(){
        List<String> types = new ArrayList<String>();
        types.add("Alarm");
        types.add("Send SMS");
        types.add("Push Notification");
        types.add("Send Email (Gmail only)");

        mAdapter = new EventTypeAdapter(types);
        mTypeRecyclerView.setAdapter(mAdapter);
    }

    public static EventTypeFragment newInstance(double latitude, double longitude, String address) {
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putString(ARG_ADDRESS,address);

        EventTypeFragment fragment = new EventTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private class EventTypeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mEventType;
        public ImageView mEventImage;
        private int mCurrentEventType;

        public EventTypeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mEventType = (TextView) itemView.findViewById(R.id.list_item_event_title);
            mEventImage = (ImageView) itemView.findViewById(R.id.list_item_event_type_image);
        }

        public void bindEvent(int eventType, int eventImageId, String eventName){
            mCurrentEventType = eventType;
            mEventType.setText(eventName);
            mEventImage.setImageResource(eventImageId);
        }

        @Override
        public void onClick(View view) {
                Intent intent = SettingsActivity.newIntent(getActivity(), getArguments().getDouble(ARG_LATITUDE),
                        getArguments().getDouble(ARG_LONGITUDE), getArguments().getString(ARG_ADDRESS), mCurrentEventType);
                startActivity(intent);
        }
    }

    private class EventTypeAdapter extends RecyclerView.Adapter<EventTypeHolder>{
        private List<String> mEventTypes;

        public EventTypeAdapter(List<String> types){
            mEventTypes = types;
        }

        @Override
        public EventTypeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_event_type,parent,false);
            return new EventTypeHolder(view);
        }

        @Override
        public void onBindViewHolder(EventTypeHolder holder, int position){
            String type = mEventTypes.get(position);
            int eventIcon;
            int eventId;

                if(type == "Alarm") {
                    eventIcon = R.drawable.ic_alarm;
                    eventId = EventManager.ALARM;
                }
                else if(type == "Send SMS") {
                    eventIcon = R.drawable.ic_sms_icon;
                    eventId = EventManager.SMS;
                }
                else if (type =="Push Notification") {
                    eventIcon = R.drawable.ic_speaker_icon;
                    eventId = EventManager.NOTIFICATION;
                }
                else if( type == "Send Email (Gmail only)") {
                    eventIcon = R.drawable.ic_gmail_icon;
                    eventId = EventManager.EMAIL;
                }
                else {
                    eventIcon = R.drawable.ic_speaker_icon;
                    eventId = 3;
                }

            holder.bindEvent(eventId,eventIcon,type);
        }

        @Override
        public int getItemCount(){
            return mEventTypes.size();
        }


    }
}
