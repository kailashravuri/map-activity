package com.map.activity.tracker;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public ArrayList<ListData> myValues;

    public RecyclerViewAdapter(ArrayList<ListData> myValues) {
        this.myValues = myValues;
    }

    private static ClickListener clickListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewitem, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.routeName.setText(myValues.get(position).getRouteName());
        holder.startTime.setText(myValues.get(position).getStartTime());
        holder.endTime.setText(myValues.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return myValues.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView routeName;
        private TextView startTime;
        private TextView endTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.text_routename);
            startTime = (TextView) itemView.findViewById(R.id.text_starttime);
            endTime = (TextView) itemView.findViewById(R.id.text_endtime);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
