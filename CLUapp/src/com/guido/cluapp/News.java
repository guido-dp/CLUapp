package com.guido.cluapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class News extends Fragment {
	private ProgressBar progressbar = null;
	private ListView feedListView = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.news_fragment, container, false);
		progressbar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		String url = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";
		//new DownloadFilesTask().execute(url);
		return rootView;
	}
}
