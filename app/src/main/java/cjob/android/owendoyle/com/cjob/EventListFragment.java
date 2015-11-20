package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cjob.android.owendoyle.com.cjob.database.EventCursorWrapper;
import cjob.android.owendoyle.com.cjob.events.Event;
import cjob.android.owendoyle.com.cjob.events.EventManager;

/**
 * Created by Comhghall on 04/11/2015.
 */
public class EventListFragment extends Fragment {

    private static final  String ARG_EVENT_TYPE = "event_type";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_EVENT_TITLE = "title";
    private RecyclerView mEventListRecyclerView;
    private EventListAdapter mAdapter;
//    private String type;
    private String title;
    private String address;
    private EventManager eventManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_event_details:
                Intent i = new Intent(getActivity(), MapActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_marker_event, menu);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        type = (String) getArguments().getString(ARG_EVENT_TYPE);
        //title = (String) getArguments().getString(ARG_EVENT_TITLE);
        //address = (String) getArguments().getString(ARG_ADDRESS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_type, container, false);
        mEventListRecyclerView = (RecyclerView) v.findViewById(R.id.event_type_recycle_view);
        mEventListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventManager = new EventManager(getActivity());
        updateUI();
        return v;
    }

    public void updateUI(){
//        EventCursorWrapper eventCursorWrapper = eventManager.queryEvents(null, null);
//        if(eventCursorWrapper.moveToFirst()) {
//            mAdapter = new EventListAdapter(eventCursorWrapper);
//        }
        List<Event> eventList = eventManager.getEventList();
        mAdapter = new EventListAdapter(eventList);
        mEventListRecyclerView.setAdapter(mAdapter);
    }

    public static EventTypeFragment newInstance(String type, String title, String address) {
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_TYPE, type);
        args.putString(ARG_EVENT_TITLE, title);
        args.putString(ARG_ADDRESS, address);

        EventTypeFragment fragment = new EventTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView eventTitle;
        TextView eventLocation;

        EventViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventTitle = (TextView)itemView.findViewById(R.id.event_title);
            eventLocation = (TextView)itemView.findViewById(R.id.event_location);
        }

        public void bindEvent(String title, String address){
            eventTitle.setText(title);
            eventLocation.setText(address);
        }

        @Override
        public void onClick(View view) {
            //Need to hook this up to the map with an intent.
        }
    }

    private class EventListAdapter extends RecyclerView.Adapter<EventViewHolder>{
        List<Event> eventList = new ArrayList<>();

        //Write constructor where you instantiate list to hold events.
        EventListAdapter(List<Event> eventListArg){
            eventList = eventListArg;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_event_list, parent, false);
            return new EventViewHolder(view);
        }

        //set data here.
        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            String address;
            String eventTitle;
//            String eventType;
//            Event event;

            address = eventList.get(position).getAddress();
            eventTitle = eventList.get(position).getTitle();
//            eventType = eventList.get(position).getType();

            holder.bindEvent(eventTitle, address);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }
}
