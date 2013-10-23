package com.guido.cluapp;

import java.util.Locale;

import com.guido.cluapp.service.NotificationService;
import com.guido.cluapp.utils.ConnectionDetector;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	/**
	 * The {@link isInternetPresent} is a flag.
	 */
	Boolean isInternetPresent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// flag for Internet connection status
		isInternetPresent = false;
	}
	
	protected void onStart() {
	    super.onStart();  // Always call the superclass method first
	    	// Connection detector
	 		ConnectionDetector cd=new ConnectionDetector(getApplicationContext());;
	 		// get Internet status
	         isInternetPresent = cd.isConnectingToInternet();
	         // check for Internet status
	         if (!isInternetPresent) {
	             // Internet connection is not present
	             // Ask user to connect to Internet
	             showNetErrAlertDialog(MainActivity.this, "No Internet Connection",
	                     "You don't have internet connection.");
	         }else{
	        	// Create the adapter that will return a fragment for each of the three
	     		// primary sections of the app.
	     		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

	     		// Set up the ViewPager with the sections adapter.
	     		mViewPager = (ViewPager) findViewById(R.id.pager);
	     		mViewPager.setAdapter(mSectionsPagerAdapter); 
	         }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    Toast toast = Toast.makeText(getBaseContext(), "Notification Service", Toast.LENGTH_SHORT);
    	toast.show();
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    int minutes = prefs.getInt("interval",5);
	    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	    Intent i = new Intent(this, NotificationService.class);
	    PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
	    am.cancel(pi);
	    // by my own convention, minutes <= 0 means notifications are disabled
	    if (minutes > 0) {
	        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	            SystemClock.elapsedRealtime() + minutes*1000,
	            minutes*1000, pi);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(MainActivity.this, FragmentPreferences.class);
				startActivity(intent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment = new Fragment();  
			fragment = new Page();
	    	Bundle args = new Bundle();
		    switch (position) {  
		    case 0: 
		    	args.putString("linkFeed", getString(R.string.link_feed_1));
		    	fragment.setArguments(args);;
		    	return fragment;
		    case 1:  
		    	args.putString("linkFeed", getString(R.string.link_feed_2));
		    	fragment.setArguments(args);;
		    	return fragment;   
		    case 2:
		    	args.putString("linkFeed", getString(R.string.link_feed_3));
		    	fragment.setArguments(args);;
		    	return fragment; 
		    default:  
		        break;  
		    }
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			//pages titles
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	/**
	     * Function to display simple Alert Dialog
	     * @param context - application context
	     * @param title - alert dialog title
	     * @param message - alert message
	     * */
		public void showNetErrAlertDialog(Context context, String title, String message) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			// Setting Dialog Title
			builder.setTitle(title)
			// Setting Dialog Message
	        .setMessage(message)
	        // Setting alert dialog icon
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setCancelable(false)
	        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	MainActivity.this.finish();
	            }
	        })
			.setPositiveButton("Connect",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	                startActivity(i);
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();
	    }
}