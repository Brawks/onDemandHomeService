package com.uottawa.despacithree.despacithree;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceList extends ArrayAdapter<Service>{

	private Activity context;
	private List<Service> services;

	public ServiceList (Activity context, List<Service> services){
		super(context, R.layout.datalist_service, services);
		this.context = context;
		this.services = services;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View serviceListView = inflater.inflate(R.layout.datalist_service, null, true);

		TextView serviceName = (TextView) serviceListView.findViewById(R.id.serviceName);
		TextView serviceWage = (TextView) serviceListView.findViewById(R.id.serviceWage);
		Service currentService = services.get(position);

		serviceName.setText("Service: " + currentService.getName());
		serviceWage.setText("Wage: " + Double.toString(currentService.getWage()) + " $/hour");

		return serviceListView;
	}
}