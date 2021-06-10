package com.example.trackcovid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackCovidAdapter extends RecyclerView.Adapter<TrackCovidViewHolder> {

    ArrayList mCovidData;

    TrackCovidAdapter() {
        mCovidData = new ArrayList<TrackCovidData>();
    }

    @NonNull
    @Override
    public TrackCovidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TrackCovidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackCovidViewHolder holder, int position) {
        TrackCovidData currentItem = (TrackCovidData) mCovidData.get(position);
        holder.state.setText(currentItem.mState);
        holder.confirmed.setText(currentItem.mConfirmed);
        holder.active.setText(currentItem.mActive);
        holder.recovered.setText(currentItem.mRecovered);
        holder.deaths.setText(currentItem.mDeaths);
    }

    @Override
    public int getItemCount() {
        return mCovidData.size();
    }

    void UpdateData(ArrayList<TrackCovidData> updatedCovidData) {
        mCovidData.clear();
        mCovidData.addAll(updatedCovidData);

        notifyDataSetChanged();
    }
}
class TrackCovidViewHolder extends RecyclerView.ViewHolder {
    public TextView state;
    public TextView confirmed;
    public TextView active;
    public TextView recovered;
    public TextView deaths;

    TrackCovidViewHolder(@NonNull View itemView) {
        super(itemView);
        state = itemView.findViewById(R.id.itemState);
        confirmed = itemView.findViewById(R.id.itemConfirmed);
        active = itemView.findViewById(R.id.itemActive);
        recovered = itemView.findViewById(R.id.itemRecovered);
        deaths = itemView.findViewById(R.id.itemDeaths);
    }
}