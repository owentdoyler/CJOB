package cjob.android.owendoyle.com.cjob;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 28/10/2015.
 */
public class EventTypeFragment extends Fragment {

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
        types.add("Personal Message");
        types.add("Send Email");
        types.add("WhatsApp");

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

    private class EventTypeHolder extends RecyclerView.ViewHolder{
        public TextView mEventType;

        public EventTypeHolder(View itemView){
            super(itemView);
            mEventType = (TextView) itemView;
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
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            return new EventTypeHolder(view);
        }

        @Override
        public void onBindViewHolder(EventTypeHolder holder, int position){
            String type = mEventTypes.get(position);
            holder.mEventType.setText(type);
        }

        @Override
        public int getItemCount(){
            return mEventTypes.size();
        }
    }
}
