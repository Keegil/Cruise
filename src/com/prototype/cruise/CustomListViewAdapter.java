package com.example.cruiseev;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<Stats> {

	Context context;

	public CustomListViewAdapter(Context context, int resourceId,
			List<Stats> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView li_id;
		TextView li_point;
		TextView li_date;
		TextView li_remdist;
		TextView li_dist;
		TextView li_accmist;
		TextView li_spmist;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Stats rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			
			holder.li_id = (TextView) convertView.findViewById(R.id.li_id);
			holder.li_point = (TextView) convertView.findViewById(R.id.li_point);
			holder.li_date = (TextView) convertView.findViewById(R.id.li_date);
			holder.li_remdist = (TextView) convertView.findViewById(R.id.li_remdist);
			holder.li_dist = (TextView) convertView.findViewById(R.id.li_dist);
			holder.li_accmist = (TextView) convertView.findViewById(R.id.li_accmist);
			holder.li_spmist = (TextView) convertView.findViewById(R.id.li_spmist);
			
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.li_id.setText(""+rowItem.getId());
		holder.li_point.setText("Score: "+rowItem.getPoint());
		holder.li_date.setText(""+rowItem.getDate());
		holder.li_remdist.setText("Remaining range: "+rowItem.getRemainingRange());
		holder.li_dist.setText(""+rowItem.getDriveLength());
		holder.li_accmist.setText(""+rowItem.accMistakes());
		holder.li_spmist.setText(""+rowItem.getSpeedMistakes());

		return convertView;
	}
}