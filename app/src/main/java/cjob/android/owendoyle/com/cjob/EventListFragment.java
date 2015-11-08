package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Comhghall on 04/11/2015.
 */
public class EventListFragment extends Fragment {

    private static final  String ARG_EVENT_TYPE = "event_type";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_EVENT_TITLE = "title";
    private RecyclerView mTypeRecyclerView;
    private EventListAdapter mAdapter;
    private String type;
    private String title;
    private String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (String) getArguments().getString(ARG_EVENT_TYPE);
        title = (String) getArguments().getString(ARG_EVENT_TITLE);
        address = (String) getArguments().getString(ARG_ADDRESS);
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

        @Override
        public void onClick(View view) {
            //Need to hook this up to the map with an intent.
        }
    }

    private class EventListAdapter extends RecyclerView.Adapter<EventViewHolder>{

        //Write constructor where you instantiate list to hold events.

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        //set data here.
        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
