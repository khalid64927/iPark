package com.smikeapps.parking.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smikeapps.parking.R;
import com.smikeapps.parking.comman.utils.PrinterDetail;
import com.smikeapps.parking.common.context.ApplicationContextSingleton;

public class PrinterListAdapter extends BaseAdapter{

	private ArrayList<PrinterDetail> printers;
	public PrinterListAdapter(ArrayList<PrinterDetail> printers){
		this.printers = printers;
	}
	
	@Override
	public int getCount() {
		return printers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(ApplicationContextSingleton.getApplicationContext()));
		if(convertView == null){
			convertView = viewHolder.getView();
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.setData(printers.get(position), position);
		return convertView;
	}
	
	class ViewHolder{
		
		private TextView friendlyName;
		private TextView macAddress;
		private View displayView;
		public ViewHolder(LayoutInflater inflater){
			buildView(inflater);
		}

		private void buildView(LayoutInflater inflater) {
			displayView =  inflater.inflate( R.layout.view_holder_dialog_discoverd_printer, null );
			friendlyName = (TextView)displayView.findViewById(R.id.friendlyName);
			macAddress = (TextView)displayView.findViewById(R.id.macAddress);
		}
		
		public View getView () {
			return displayView;
		}
		
		public void setData(PrinterDetail printerDetails, int position){
			friendlyName.setText(printerDetails.getFriendlyName());
			macAddress.setText(printerDetails.getMacAddress());
		}

}
}	
