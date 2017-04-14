package com.example.jeobmallari.ilib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.*;

/**
 * Created by Jeob Mallari on 3/12/2017.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ResourceViewHolder> {

    private static final String TAG = RecycleViewAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private int mNumberItems;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, String titleOfResource);
    }

    private String received;
//    public RecycleViewAdapter(int numberItems){
//        mNumberItems = numberItems;
//    }

    public RecycleViewAdapter(int numberItems, String passed, ListItemClickListener listener){
        mNumberItems = numberItems;
        received = passed;
        mOnClickListener = listener;
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.resource_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ResourceViewHolder viewHolder = new ResourceViewHolder(view);
// ---------------------------------
        viewHolder.populate();
// ---------------------------------
        return viewHolder;
    }

    public void onBindViewHolder(ResourceViewHolder holder, int data) {
        //Log.d(TAG, "#" + position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView resource_tv;
        ArrayList<String> listItems;
        String[] trial;
        View rv_parent;
        public ResourceViewHolder(View itemView){
            super(itemView);
            rv_parent = itemView;
            listItems = new ArrayList<String>();

            itemView.setOnClickListener(this);
            resource_tv = (TextView) itemView.findViewById(R.id.tv_resource_item);
        }

        public void populate(){ // TODO BY JEOB USE THIS FXN TO POPULATE BOOK RESULTS
            DatabaseReference childRef = dbRef.child("books");
            // use String received

            // TODO EDIT CODE TO IMPLEMENT DATA RETRIEVAL. THIS addValueEventListener ONLY LISTENS TO VALUE CHANGES
            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    List books = new ArrayList<>();
                    for (DataSnapshot bookDataSnapshot : dataSnapshot.getChildren()) {
                        String book = bookDataSnapshot.getValue(String.class);
                        listItems.add(book);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(null, "Error in data access.", LENGTH_LONG).show();
                }
            });
        }

        public void bind(int data){ // data is the list index
            resource_tv.setText(listItems.get(data)+"\n");
        }

        @Override
        public void onClick(View view){
            int clickedItemPosition = getAdapterPosition();
            String bookTitle = listItems.get(clickedItemPosition);
            mOnClickListener.onListItemClick(clickedItemPosition, bookTitle);
        }
    }

}
