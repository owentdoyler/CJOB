/*
* This fragments holds the list of the current active events
* */

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
import android.widget.ImageView;
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
    public static final String EXTRA_EVENT_ID = "cjob.android.owendoyle.com.cjob.EVENT_ID";
    private RecyclerView mEventListRecyclerView;
    private EventListAdapter mAdapter;
//    private String type;
    private String title;
    private String address;
    private EventManager eventManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.event_list_view_map:
                Intent i = new Intent(getActivity(), MapActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event_list, menu);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    //updates the list of events when needed
    public void updateUI(){

        List<Event> eventList = eventManager.getEventList();
        mAdapter = new EventListAdapter(eventList);
        mEventListRecyclerView.setAdapter(mAdapter);
    }


    public class EventViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView eventTitle;
        TextView eventLocation;
        ImageView eventSymbol;
        View currentView;
        int eventID;

        EventViewHolder(View itemView){
            super(itemView);
            currentView = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventSymbol = (ImageView) itemView.findViewById(R.id.event_image);
            eventTitle = (TextView)itemView.findViewById(R.id.event_title);
            eventLocation = (TextView)itemView.findViewById(R.id.event_location);
        }

        public void bindEvent(int eventSymbolID, String title, String address, String lat, String lng, int eventID){
            eventSymbol.setImageResource(eventSymbolID);
            eventTitle.setText(title);
            eventLocation.setText(address);

            // this method gets an image from the static map API at a given location
            new MapImageMaker((ImageView) currentView.findViewById(R.id.map_image))
                    .execute("https://maps.googleapis.com/maps/api/staticmap?" +
                            "center="+lat+","+lng+"&zoom=16&size=300x300" +
                            "&markers="+lat+","+lng+"&maptype=normal");
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
            final View view = layoutInflater.inflate(R.layout.fragment_event_list, parent, false);
            //opens the map with the clicked event highlighted
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mEventListRecyclerView.getChildLayoutPosition(view);
                    Intent i = new Intent(getActivity(), MapActivity.class);
                    i.putExtra(EXTRA_EVENT_ID, eventList.get(position).getId());
                    startActivity(i);
                }
            });
            return new EventViewHolder(view);
        }

        //sets the data for each event here.
        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            String address;
            String eventTitle;
            int eventType;
            int eventSymbolId;
            String latitide;
            String longitude;
            int eventID;

            latitide = Double.toString(eventList.get(position).getLatitude());
            longitude = Double.toString(eventList.get(position).getLongitude());
            eventID = eventList.get(position).getId();
            address = eventList.get(position).getAddress();
            address = address.replace("\n"," ");
            eventTitle = eventList.get(position).getTitle();
            eventType = Integer.parseInt(eventList.get(position).getType());
            switch (eventType){
                case EventManager.ALARM:
                    eventSymbolId = R.drawable.ic_alarm;
                    break;
                case EventManager.NOTIFICATION:
                    eventSymbolId = R.drawable.ic_speaker_icon;
                    break;
                case EventManager.EMAIL:
                    eventSymbolId = R.drawable.ic_gmail_icon;
                    break;
                case EventManager.SMS:
                    eventSymbolId = R.drawable.ic_sms_icon;
                    break;
                default:
                    eventSymbolId = R.drawable.ic_speaker_icon;
                    break;
            }


            holder.bindEvent(eventSymbolId,eventTitle, address, latitide, longitude, eventID);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }
}
