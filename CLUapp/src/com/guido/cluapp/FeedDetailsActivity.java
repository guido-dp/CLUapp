package com.guido.cluapp;

import com.guido.cluapp.rss.RssItem;
import com.guido.cluapp.utils.ImageDownloaderTask;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FeedDetailsActivity extends Activity {

	private RssItem feed;
	private ProgressBar progressbar = null;
	private View layout = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_details);
		progressbar = (ProgressBar) findViewById(R.id.progressBar2);
		layout = (View) findViewById(R.id.layout);
		
		feed = (RssItem) getIntent().getParcelableExtra("feed");
		
		String content=new String(feed.getContent());
		ImageView thumb = (ImageView) findViewById(R.id.featuredImg);
		if(content.startsWith(layout.getContext().getString(R.string.prefix))){
			int sindex=content.indexOf(layout.getContext().getString(R.string.start_link_img));
			int eindex=content.substring(sindex).indexOf(layout.getContext().getString(R.string.end_link_img));
			new ImageDownloaderTask(thumb).execute(content.substring(sindex,sindex+eindex));
		}
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(feed.getTitle());
		TextView htmlTextView = (TextView) findViewById(R.id.content);
		htmlTextView.setText(Html.fromHtml(feed.getContent(), null, null));
		layout.setVisibility(View.VISIBLE);
		progressbar.setVisibility(View.GONE);
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.menu_share:
	        	shareContent();
	            return true;
	        case R.id.menu_view:
	        	Intent intent = new Intent(FeedDetailsActivity.this, WebViewActivity.class);
				intent.putExtra("url", feed.getUrl());
				startActivity(intent);
				
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void shareContent() {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, feed.getTitle() + "\n" + feed.getUrl());
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share using"));

	}*/
}
