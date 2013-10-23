package com.guido.cluapp.utils;

import java.util.ArrayList;

import com.guido.cluapp.R;
import com.guido.cluapp.rss.RssItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<RssItem> listData;
	private LayoutInflater layoutInflater;

	
	public CustomListAdapter(Context context, ArrayList<RssItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.title);
			holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		RssItem newsItem = (RssItem) listData.get(position);
		
		holder.headlineView.setText(newsItem.getTitle());
		holder.reportedDateView.setText(newsItem.getPubDate().toString());
		
		String content=newsItem.getContent();
		if(content.startsWith(convertView.getContext().getString(R.string.prefix))){
			int sindex=content.indexOf(convertView.getContext().getString(R.string.start_link_img));
			int eindex=content.substring(sindex).indexOf(convertView.getContext().getString(R.string.end_link_img));
			new ImageDownloaderTask(holder.imageView).execute(content.substring(sindex,sindex+eindex));
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView reportedDateView;
		ImageView imageView;
	}
}
