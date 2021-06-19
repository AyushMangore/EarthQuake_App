package com.ayush.earthquake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.graphics.drawable.GradientDrawable;
/*
as we have created a custom list view , there is a need of custom array adapter as well and that is what created in this EarthQuakeAdapter class
which extends array adapter class.
 */
public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {
    public EarthQuakeAdapter(Context context, List<EarthQuake> earthQuakeList){
        super(context,0,earthQuakeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_custom, parent, false);
        }

        EarthQuake currentEarthQuake = getItem(position);

        TextView magnitudeView = listItemView.findViewById(R.id.magnitude);

        TextView locationView1 = listItemView.findViewById(R.id.place1);
        TextView locationView2 = listItemView.findViewById(R.id.place2);
        /*
        formatting magnitude to one decimal place
         */
        String formattedMagnitude = formatMag(currentEarthQuake.getmMAngnitude());
        magnitudeView.setText(formattedMagnitude);
        String str = currentEarthQuake.getmPlace().toString();
        /*
        splitting the location string from 'of' substring as we have created two text views.
         */
        String[] splitted = str.split(" of ", 2);
        if (str.contains(" of ")) {
            locationView1.setText(splitted[0] + " of ");
            locationView2.setText(splitted[1]);
        } else {
            locationView2.setText(str);
        }
        /*
        In getMagnitudeColor we are passing the magnitude value on the basis of which the color of the magnitude text view  background has been chosen
         */
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColour = getMagnitudeColor(currentEarthQuake.getmMAngnitude());
        magnitudeCircle.setColor(magnitudeColour);

        Date dateObject = new Date(currentEarthQuake.getmTimeInMilliseconds());

        //  formatDate method format the date in the form of month date, year

        TextView dateView = listItemView.findViewById(R.id.date);
        String formatedDate = formatDate(dateObject);
        dateView.setText(formatedDate);

        // formatTime method format the time in the form of h:mm AM/PM

        TextView timeView = listItemView.findViewById(R.id.time);
        String formatedTime = formatTime(dateObject);
        timeView.setText(formatedTime);

        return listItemView;
    }
    private int getMagnitudeColor(double magnitude){
        int magnitudeResourceid;
        int floorMag = (int)Math.floor(magnitude);
        switch (floorMag){
            case 0 :
            case 1 :
                magnitudeResourceid = R.color.magnitude1;break;
            case 2:
                magnitudeResourceid = R.color.magnitude2;break;
            case 3:
                magnitudeResourceid = R.color.magnitude3;break;
            case 4:
                magnitudeResourceid = R.color.magnitude4;break;
            case 5:
                magnitudeResourceid = R.color.magnitude5;break;
            case 6:
                magnitudeResourceid = R.color.magnitude6;break;
            case 7:
                magnitudeResourceid = R.color.magnitude7;break;
            case 8:
                magnitudeResourceid = R.color.magnitude8;break;
            case 9:
                magnitudeResourceid = R.color.magnitude9;break;
            default :
                magnitudeResourceid = R.color.magnitude10plus;break;
        }
        return ContextCompat.getColor(getContext(),magnitudeResourceid);
    }
    private String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return simpleDateFormat.format(date);
    }
    private String formatTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }
    private String formatMag(Double mag){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mag);
    }
}
