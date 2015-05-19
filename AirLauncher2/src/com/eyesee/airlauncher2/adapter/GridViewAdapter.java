package com.eyesee.airlauncher2.adapter;

import java.util.ArrayList;

import com.eyesee.airlauncher2.R;
import com.eyesee.airlauncher2.entity.AppInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 每页的GridView适配器
 * @author mark
 */
public class GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AppInfo> allApps;

	public GridViewAdapter(Context context, ArrayList<AppInfo> allApps) {
		this.mContext = context;
		this.allApps = allApps;
	}

	@Override
	public int getCount() {
		return allApps.size();
	}

	@Override
	public Object getItem(int position) {
		return allApps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_gv, null);
			viewHolder.appIcon = (ImageView) convertView
					.findViewById(R.id.iv_app_icon);
			viewHolder.appName = (TextView) convertView
					.findViewById(R.id.tv_app_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.appIcon.setImageDrawable(allApps.get(position).icon);
		viewHolder.appName.setText(allApps.get(position).name);
		return convertView;
	}

	class ViewHolder {
		ImageView appIcon;
		TextView appName;
	}
}
