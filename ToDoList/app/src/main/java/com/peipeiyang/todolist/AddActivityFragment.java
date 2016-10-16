package com.peipeiyang.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddActivityFragment extends Fragment {
    public static String OPINION = "opinion";
    private DBHelper dbHelper = null;
    ArrayAdapter<Notedetail> adapter = null;
    List<Notedetail> notes = new ArrayList<Notedetail>();
    public final static String TAG = "NoteList";

    public AddActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            dbHelper = new DBHelper(getActivity());
         //   notes = dbHelper.selectAll();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }



        Button savebutton = (Button)getActivity().findViewById(R.id.saveBT);
        savebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(OPINION, "");
                getActivity().setResult(Activity.RESULT_OK, data);



                onSave();



            }

        });

    }



    private void onSave() {

        Notedetail note = new Notedetail();
        EditText title = (EditText) getActivity().findViewById(R.id.titleET);
        String noteTitle = ("Title:  " + title.getText()).toString();
        String titlecheck = title.getText().toString();
        EditText date = (EditText) getActivity().findViewById(R.id.dateET);
        String notedate = ("Due date:  " + date.getText()).toString();
        String datecheck = date.getText().toString();
        EditText description = (EditText) getActivity().findViewById(R.id.descriptionET);
        EditText addinfo = (EditText) getActivity().findViewById(R.id.addtionalET);


        if ((TextUtils.isEmpty(titlecheck) )|| (TextUtils.isEmpty(datecheck))) {
            showMissingInfoAlert();
        }else {
            note.setTitle(noteTitle);
            note.setDuedate(notedate);
            note.setDescription(("Description:  " + description.getText()).toString());
            note.setAdditionalinfo(("Addtional Infor:  " + addinfo.getText()).toString());
            RadioGroup types = (RadioGroup) getActivity().findViewById(R.id.note_category);

            switch (types.getCheckedRadioButtonId()) {
                case R.id.note_study:
                    note.setCategory("Study");
                    break;
                case R.id.note_work:
                    note.setCategory("Work");
                    break;
                case R.id.note_daily:
                    note.setCategory("DailyLife");
                    break;
            }

            long noteId = 0;
            if(dbHelper != null) {
                noteId = dbHelper.insert(note);
                note.setId(noteId);
                // Add the object at the end of the array.
              //  adapter.add(note);
                // Notifies the adapter that the underlying data has changed,           //
                //       any View reflecting the data should refresh itself.
             //   adapter.notifyDataSetChanged();
          }
            // Remove the soft keyboard after hitting the save button
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


            getActivity().finish();
        }
    }

    public void showMissingInfoAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);

        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_message))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close current activity
                        dialog.cancel();
                    }

                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
