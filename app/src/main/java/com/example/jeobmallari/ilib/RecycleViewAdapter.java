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
    final private ListItemClickListener mOnClickListener;
    private int mNumberItems;
    private ResourceViewHolder viewHolder;
    static ArrayList<String> items;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, String titleOfResource);
    }

    public RecycleViewAdapter(ArrayList<String> items, ListItemClickListener listener){
        this.items = items;
        mOnClickListener = listener;
        mNumberItems = this.items.size();
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.resource_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        viewHolder = new ResourceViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(ResourceViewHolder holder, int data) {
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView resource_tv;
        TextView author;
        ArrayList<String> listItems;
        View rv_parent;
        public ResourceViewHolder(View itemView){
            super(itemView);
            rv_parent = itemView;
            listItems = RecycleViewAdapter.items;
            itemView.setOnClickListener(this);
            resource_tv = (TextView) itemView.findViewById(R.id.tv_resource_item);
            author = (TextView) itemView.findViewById(R.id.tv_author);
        }

        public void bind(int data){ // data is the list index
            String toParse = listItems.get(data);
            String[] tokens = toParse.split(",");
            resource_tv.setText(tokens[0]);
            author.setText(tokens[1]);
        }

        @Override
        public void onClick(View view){
            int clickedItemPosition = getAdapterPosition();
            String bookTitle = listItems.get(clickedItemPosition);
            mOnClickListener.onListItemClick(clickedItemPosition, bookTitle);
        }
    }

}
