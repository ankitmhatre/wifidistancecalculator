package com.dragonide.wifidistancecalculator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ankit on 4/11/2017.
 */

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.DistanceHolder> {
    private List<Distance> wifilist;

    public class DistanceHolder extends RecyclerView.ViewHolder {
        public TextView name, freq, stren, dist;

        public DistanceHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_id);
            freq = (TextView) view.findViewById(R.id.freq_id);
            stren = (TextView) view.findViewById(R.id.stren_id);
            dist = (TextView) view.findViewById(R.id.distance_id);
        }
    }

    public DistanceAdapter(List<Distance> wifilist) {
        this.wifilist = wifilist;
    }
    @Override
    public DistanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.distance_item, parent, false);

        return new DistanceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DistanceHolder holder, int position) {
        Distance dist_item = wifilist.get(position);
        holder.name.setText(dist_item.getName());
        holder.freq.setText(dist_item.getFreq() + "Hz");
        holder.stren.setText(dist_item.getStrength() + "dBm");
        holder.dist.setText(dist_item.getDistance() + "m");
    }

    @Override
    public int getItemCount() {
        return wifilist.size();
    }
}

