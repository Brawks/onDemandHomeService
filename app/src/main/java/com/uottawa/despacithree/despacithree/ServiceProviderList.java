package com.uottawa.despacithree.despacithree;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceProviderList extends ArrayAdapter<ServiceProviderAccount> {
    private Activity context;
    private List<ServiceProviderAccount> accounts;
    private boolean showAva;
    private String currentDay;

    public ServiceProviderList (Activity context, List<ServiceProviderAccount> accounts, boolean showAva, String currentDay){
        super(context, R.layout.serviceprovider_list, accounts);
        this.context = context;
        this.accounts = accounts;
        this.showAva = showAva;
        this.currentDay = currentDay;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View serviceListView = inflater.inflate(R.layout.serviceprovider_list, null, true);

        TextView serviceName = (TextView) serviceListView.findViewById(R.id.listServiceProviderName);
        TextView serviceRating = (TextView) serviceListView.findViewById(R.id.listServiceProviderRating);
        TextView serviceAvailability = (TextView) serviceListView.findViewById(R.id.listServiceProviderAvailability);
        ServiceProviderAccount currentService = accounts.get(position);

        serviceName.setText("Service Provider: " + currentService.getFullName());
        serviceRating.setText("Ratings: " + (currentService.getAverageRating() == 0 ? "Not Rated": (double) Math.round(currentService.getAverageRating()*10)/10));

        switch (currentDay){
            case "Monday":
                serviceAvailability.setText(currentService.getAvailability().getMonday().toString());
                break;
            case "Tuesday":
                serviceAvailability.setText(currentService.getAvailability().getTuesday().toString());
                break;
            case "Wednesday":
                serviceAvailability.setText(currentService.getAvailability().getWednesday().toString());
                break;
            case "Thursday":
                serviceAvailability.setText(currentService.getAvailability().getThursday().toString());
                break;
            case "Friday":
                serviceAvailability.setText(currentService.getAvailability().getFriday().toString());
                break;
            case "Saturday":
                serviceAvailability.setText(currentService.getAvailability().getSaturday().toString());
                break;
            case "Sunday":
                serviceAvailability.setText(currentService.getAvailability().getSunday().toString());
                break;
        }

        if (!showAva) {
            serviceAvailability.setVisibility(View.GONE);
        }

        return serviceListView;
    }
}
