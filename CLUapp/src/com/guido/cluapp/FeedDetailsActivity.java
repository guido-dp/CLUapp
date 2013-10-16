package com.guido.cluapp;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

import com.guido.cluapp.rss.RssFeed;
import com.guido.cluapp.rss.RssItem;
import com.guido.cluapp.rss.RssReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FeedDetailsActivity extends Activity {

	private RssItem feed;
	private String link;
	private getFeedRss asyncgetrss = null;
	private ProgressBar progressbar = null;
	private View layout = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_details);

		link = this.getIntent().getStringExtra("feed");
		
		if (null != link) {
			progressbar = (ProgressBar) findViewById(R.id.progressBar2);
			layout = (View) findViewById(R.id.layout);
			asyncgetrss = new getFeedRss();
			asyncgetrss.execute((Void) null);
			
		}
	}
	public class getFeedRss extends AsyncTask<Void, Void, Boolean> {
		@Override
        protected void onPreExecute(){
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
            //ESEGUIRE OPERAZIONI IN BACKGROUND
        	try {
    			URL url = new URL(link + "feed/?withoutcomments=1");
    			RssFeed rssfeed = RssReader.read(url);
    			feed=rssfeed.getRssItems().get(0);
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
        	//ImageView thumb = (ImageView) findViewById(R.id.featuredImg);
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(feed.getTitle());
			TextView htmlTextView = (TextView) findViewById(R.id.content);
			htmlTextView.setText(Html.fromHtml(feed.getContent(), null, null));
			layout.setVisibility(View.VISIBLE);
			progressbar.setVisibility(View.GONE);
        }
 
        @Override
        protected void onCancelled() {
        	asyncgetrss = null;
        }
	}
	/*@Override
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
