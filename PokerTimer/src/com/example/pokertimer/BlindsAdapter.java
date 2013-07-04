package com.example.pokertimer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BlindsAdapter extends BaseAdapter {

	public List<Round> roundsData;
	private Context mContext;
	private LayoutInflater mInflater;
 
	public BlindsAdapter(Context context, List<Round> objects) {
		this.roundsData = objects;
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
   
			convertView = mInflater.inflate(R.layout.blind_row, null);
   
			holder.txtSB = (TextView) convertView.findViewById(R.id.set_blind_sb);
			holder.txtBB = (TextView) convertView.findViewById(R.id.set_blind_bb);
			holder.txtAnte = (TextView) convertView.findViewById(R.id.set_blind_ante);
			holder.txtMinutes = (TextView) convertView.findViewById(R.id.set_blind_minutes);
			holder.txtSeconds = (TextView) convertView.findViewById(R.id.set_blind_seconds);
   
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
  
		Round r = roundsData.get(position);
		holder.txtSB.setText(r.getSB() + "");
		holder.txtBB.setText(r.getBB() + "");
		holder.txtAnte.setText(r.getAnte() + "");
		holder.txtMinutes.setText(r.getMinutes() + "");
		holder.txtSeconds.setText(r.getSeconds() + "");
		
		return convertView;
	}
 
	static class ViewHolder {
		TextView txtSB;
		TextView txtBB;
		TextView txtAnte;
		TextView txtMinutes;
		TextView txtSeconds;
	}

	public int getCount() {
		return roundsData.size();
	}

	public Round getItem(int position) {
		return roundsData.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
}