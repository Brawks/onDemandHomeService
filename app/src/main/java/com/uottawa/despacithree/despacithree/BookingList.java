package com.uottawa.despacithree.despacithree;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookingList extends ArrayAdapter<Booking> {
    private Activity context;
    private List<Booking> bookings;

    public BookingList (Activity context, List<Booking> bookings){
        super(context, R.layout.datalist_booking, bookings);
        this.context = context;
        this.bookings = bookings;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View bookingListView = inflater.inflate(R.layout.datalist_booking, null, true);

        TextView bookingName = (TextView) bookingListView.findViewById(R.id.bookingName);
        TextView bookingDay = (TextView) bookingListView.findViewById(R.id.bookingDay);
        Booking currentBooking = bookings.get(position);

        bookingName.setText("Booking: " + currentBooking.getServiceProviderName() + " for " + currentBooking.getService());
        bookingDay.setText(currentBooking.toString());


        return bookingListView;
    }
}
