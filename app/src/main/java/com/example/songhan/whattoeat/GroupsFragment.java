package com.example.songhan.whattoeat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.songhan.whattoeat.database.DatabaseAdapter;

/**
 * Created by Song on 2015/12/16.
 */
public class GroupsFragment extends Fragment implements AdapterView.OnItemClickListener  {

    private DatabaseAdapter db;
    private SimpleCursorAdapter adapter;
    private ListView list;

    public static final String TAG = "group";
    public static final String SELECTED_GROUP_NAME = "selected_group_name";
    public static final String SELECTED_GROUP_ID = "selected_group_id";

    public static Fragment newInstance(Context context) {
        return new GroupsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        db = new DatabaseAdapter(getActivity());
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.row_circle,
                db.getCircles(),
                new String[] { DatabaseAdapter.CIRCLE_NAME },
                new int[] { R.id.row_circle_name });
        list = (ListView) getActivity().findViewById(R.id.circle_listview);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        TextView textView = (TextView) getActivity().findViewById(R.id.fragment_groups_textview);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_circle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_circle:
                AddGroupDialog dialog = new AddGroupDialog();
                dialog.setTargetFragment(this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), AddGroupDialog.TAG);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textView = (TextView) view.findViewById(R.id.row_circle_name);
        Bundle bundle = new Bundle();
        bundle.putLong(SELECTED_GROUP_ID, id);
        bundle.putString(SELECTED_GROUP_NAME, textView.getText().toString());

        Fragment frag = RestaurantInGroupFragment.newInstance(getActivity());
        frag.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, frag, RestaurantInGroupFragment.TAG).addToBackStack(TAG).commit();
    }

    public void refreshUI() {
        adapter.changeCursor(db.getCircles());
        adapter.notifyDataSetChanged();
    }
}