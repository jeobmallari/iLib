package com.example.jeobmallari.ilib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jeob Mallari on 3/12/2017.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ResourceViewHolder> {

    private static final String TAG = RecycleViewAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private int mNumberItems;

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
        public ResourceViewHolder(View itemView){
            super(itemView);

            listItems = new ArrayList<String>();
            trial = itemView.getResources().getStringArray(R.array.sample_items);   // use this string to display book titles
            for(int i=0;i<trial.length;i++){
                listItems.add(trial[i]);
            }
            itemView.setOnClickListener(this);
            resource_tv = (TextView) itemView.findViewById(R.id.tv_resource_item);
        }

        public void populate(){ // TODO BY JEOB USE THIS FXN TO POPULATE BOOK RESULTS
            int arrLen = trial.length;
            for(int i=arrLen;i<mNumberItems;i++) {
                String text = "Book #" + (i+1) + " about " + received; // use this string to display book titles
                //listItems[arrLen+i] = text;
                listItems.add(i, text);
            }
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
