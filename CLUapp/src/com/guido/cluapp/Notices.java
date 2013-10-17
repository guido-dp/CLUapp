package com.guido.cluapp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.guido.cluapp.rss.RssFeed;
import com.guido.cluapp.rss.RssItem;
import com.guido.cluapp.rss.RssReader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class Notices extends Fragment {
	private ArrayList<RssItem> feedList = null;
	private ProgressBar progressbar = null;
	private ListView feedListView = null;
	private View root = null;
	private getFeedsRss asyncgetrss = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.news_fragment, container, false);
		progressbar = (ProgressBar) root.findViewById(R.id.progressBar);
		asyncgetrss = new getFeedsRss();
		asyncgetrss.execute((Void) null);
		return root;
	}
	public class getFeedsRss extends AsyncTask<Void, Void, Boolean> {
		@Override
        protected void onPreExecute(){
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
            //ESEGUIRE OPERAZIONI IN BACKGROUND
        	try {
    			URL url = new URL("http://gsnapoli.altervista.org/category/avvisi/feed/");
    			RssFeed feed = RssReader.read(url);
    			feedList=feed.getRssItems();
    		} catch (SAXException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            return true;
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
            updateList();
        }
 
        @Override
        protected void onCancelled() {
        	asyncgetrss = null;
        }
	}
	public void updateList() {
		feedListView= (ListView) root.findViewById(R.id.custom_list);
		feedListView.setVisibility(View.VISIBLE);
		progressbar.setVisibility(View.GONE);
		
		feedListView.setAdapter(new CustomListAdapter(root.getContext(), feedList));
		feedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
				String link = feedList.get(position).getLink();
				
				Intent intent = new Intent(root.getContext(), FeedDetailsActivity.class);
				intent.putExtra("feed", link);
				startActivity(intent);
			}
		});
		}

}
