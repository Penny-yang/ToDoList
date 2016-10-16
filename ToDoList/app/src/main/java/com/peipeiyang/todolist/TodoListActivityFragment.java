package com.peipeiyang.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodoListActivityFragment extends Fragment {
    private DBHelper dbHelper = null;
    ArrayAdapter<Notedetail> adapter = null;
    List<Notedetail> notes = new ArrayList<Notedetail>();
    private List<Notedetail>  rest_data;
    public final static String TAG = "NoteList";

    Button addbutn;
    static int REQUEST_CAR_DETAILS =1;

    private final static String SELECTED_SHOW_RESTAURANT_TYPE = "pref_selected_show";
    private final static String WHATIF_BTN_PREF  = "pref_display_button";
    public static final int SHOW_PREFERENCES = 1;

    public TodoListActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "TodoListActivityFragment onOptionsItemSelected: " +
                Integer.toHexString(item.getItemId()));

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(getActivity(),
                        SettingActivity.class), SHOW_PREFERENCES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Selectall();

        addbutn = (Button)getActivity().findViewById(R.id.addButn);
        addbutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails();
            }
        });
    }

    //show the second page
    public void showDetails(){
        Intent intent = new Intent(getActivity(),
                AddActivity.class);
        startActivityForResult(intent, REQUEST_CAR_DETAILS);

    }
//
//    @Override
//    public void onActivityResult(int reqCode, int resCode, Intent data) {
//        super.onActivityResult(reqCode, resCode, data);
//        Log.d(TAG, " onActivityResult " + " data: " + data);
//        switch (reqCode) {
//            case SHOW_PREFERENCES:
//                SharedPreferences mySharedPreferences = PreferenceManager.
//                        getDefaultSharedPreferences(getActivity());
//                rest_data = setupRestaurants();
//                adapter.clear();
//                adapter.addAll(rest_data);
//                adapter.notifyDataSetChanged();
//                break;
//        }
//    }

    //get return result and select from database
   public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAR_DETAILS) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra(AddActivityFragment.OPINION)) {

                   Selectall();

                }
            }
        }
    }

    //select all the items from database
    private void Selectall() {
        try {
            rest_data = setupRestaurants();

        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }

        ListView list = (ListView) getActivity().findViewById(R.id.noteList);

        adapter = new NotedetailAdapter(getActivity(), R.layout.row, rest_data);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doClick(view, position);
            }
        });

        //delete when long click items
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                onDelete(view, position);
                return true;
            }
        });
    }

    private List<Notedetail> setupRestaurants() {
        try {
            dbHelper = new DBHelper(getActivity());

        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }
        SharedPreferences mySharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        int showRestaurantTypeId = Integer.parseInt(mySharedPreferences.
                getString(SELECTED_SHOW_RESTAURANT_TYPE, "0"));

        switch (showRestaurantTypeId) {
            case 1:
                notes = dbHelper.selectPart("Study");
                break;
            case 2:
                notes = dbHelper.selectPart("Work");
                break;
            case 3:
                notes = dbHelper.selectPart("Daily Life");
                break;
            default:
                notes = dbHelper.selectAll();
        }
            return notes;

    }

    private void onDelete(View view, int position){
        // When clicked, delete the item that was clicked.
        // (Show a toast to indicate what is occurring)
        Notedetail note = adapter.getItem(position);
        if (note != null) {
            String item = "deleting: " + note.getTitle();
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemClick: " + note.getTitle());
            // Removes the object from the array
            if (dbHelper != null) dbHelper.deleteRecord(note.getId());
            adapter.remove(note);
            // Notifies t that the underlying data has changed
            adapter.notifyDataSetChanged();
        }
    }


    //show additional information
    public void doClick(View view, int position){
        Notedetail noteshow = adapter.getItem(position);

        Toast.makeText(getActivity(),
                (noteshow.getAdditionalinfo()), Toast.LENGTH_SHORT).show();   }

}
