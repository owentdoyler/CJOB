/*
* this is the simple home screen fragment
* */

package cjob.android.owendoyle.com.cjob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Owner on 20/11/2015.
 */
public class HomeScreenFragment extends Fragment {

    private Button mViewMapButton;
    private Button mViewEventsButton;

    @Override
    public void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
    }

    //inflates the home screen layout and wires up the buttons
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        mViewMapButton = (Button) v.findViewById(R.id.create_event_button);
        mViewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MapActivity.class);
                startActivity(i);
            }
        });

        mViewEventsButton = (Button) v.findViewById(R.id.view_events_button);
        mViewEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EventListActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

}
