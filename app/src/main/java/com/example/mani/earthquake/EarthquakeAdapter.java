package com.example.mani.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder>{

    Context mCtx;
    List<Earthquake> earthquakeList;

    public EarthquakeAdapter(Context mCtx, List<Earthquake> earthquakeList) {
        this.mCtx = mCtx;
        this.earthquakeList = earthquakeList;
    }

    @Override
    public EarthquakeViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        return new EarthquakeViewHolder( LayoutInflater.from(mCtx)
                .inflate(R.layout.recycle_view_earthquake_single_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(EarthquakeViewHolder holder, int position) {

        Earthquake earthquake = earthquakeList.get(position);


        String dateString = earthquake.getUnixTime();
        String location = earthquake.getLocation();

        String offsetLoacation = "Near by ";
        String primaryLocation = location;

        if(location.contains("of")){
            String[] s = location.split("of");
            offsetLoacation = s[0];
            primaryLocation = s[1];
        }

        DecimalFormat formatter = new DecimalFormat("0.0");
        String manitude = formatter.format(Double.valueOf(earthquake.getMagnitude()));

        long timeInMilliseconds = Long.valueOf(dateString);
        Date dateObject         = new Date(timeInMilliseconds);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormat.format(dateObject);


        holder.mag.setText(manitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.mag.getBackground();
        int magColour = getMagnitudeColor(Double.valueOf(manitude));
        magnitudeCircle.setColor(magColour);



        holder.offset_loc.setText(offsetLoacation);
        holder.primary_loc.setText(primaryLocation);


        holder.date.setText(dateToDisplay);
        holder.time.setText(timeToDisplay);
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public class EarthquakeViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {

        TextView mag,offset_loc, primary_loc,  date,time;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);

            mag         = itemView.findViewById(R.id.magnitude);
            offset_loc  = itemView.findViewById(R.id.location_offset);
            primary_loc = itemView.findViewById(R.id.primary_location);
            date        = itemView.findViewById(R.id.date);
            time        = itemView.findViewById(R.id.time);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Earthquake earthquake = earthquakeList.get(position);
            Log.e("Tag",earthquake.getLocation());
            Toast.makeText(mCtx,"Mani", Toast.LENGTH_SHORT).show();

        }
    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColorResourceId;

        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(mCtx, magnitudeColorResourceId);
    }


}
