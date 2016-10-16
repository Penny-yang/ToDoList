package com.peipeiyang.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yangp on 5/26/2016.
 */
public class NotedetailAdapter extends ArrayAdapter<Notedetail> {
    private int layoutResourceId;
    public final static String TAG = "AnimalAdapter";
    private LayoutInflater inflater;
    private List<Notedetail> notes;

    public NotedetailAdapter(Context context, int layoutResourceId,
                         List<Notedetail> notes ) {
        super(context, layoutResourceId, notes);
        this.layoutResourceId = layoutResourceId;
        this.notes = notes;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotesHolder holder = null;
        if (null == convertView) {
            Log.d(TAG, "getView:  rowView null: position " + position);
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new NotesHolder();
            holder.txtTitle = (TextView)convertView.findViewById(R.id.todo_title);
            holder.txtDescription = (TextView)convertView.findViewById(R.id.todo_description);
            holder.txtdate = (TextView)convertView.findViewById(R.id.todo_date);
            holder.imgcategory = (ImageView)convertView.findViewById(R.id.todo_category);
           // holder.txtaddinfo = (TextView)convertView.findViewById(R.id.todo_addinfo);
            // Tags can  be used to store data
            convertView.setTag(holder);
        }
        else {
            Log.d(TAG, "getView:  rowView !null - reuse holder: position " + position);
            holder = (NotesHolder)convertView.getTag();
        }
        Log.d(TAG, " getView animals " + notes.size());

        try {
            Notedetail note = notes.get(position);
            holder.txtTitle.setText(note.getTitle());
            holder.txtDescription.setText(note.getDescription());
            holder.txtdate.setText(note.getDuedate());
//            holder.txtaddinfo.setText(note.getAdditionalinfo());

            if(note.getCategory().equals(Notedetail.STUDY)){
                holder.imgcategory.setImageResource(R.drawable.study);
            }
            else if (note.getCategory().equals(Notedetail.WORK)){
                holder.imgcategory.setImageResource(R.drawable.work);
            }
            else if (note.getCategory().equals(Notedetail.DAILYLIFE)) {
                holder.imgcategory.setImageResource(R.drawable.dailylife);
            }
        } catch(Exception e) {
            Log.e(TAG, " getView animals " + e + " position was : " + position +
                    " animals.size: " + notes.size());
        }
        return convertView;
    }





        static class NotesHolder {
            TextView txtTitle;
            TextView txtDescription;
            TextView txtdate;
            ImageView imgcategory;
            TextView txtaddinfo;
        }

}
