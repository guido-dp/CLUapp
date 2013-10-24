package com.guido.cluapp.service;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpHead;

import com.guido.cluapp.MainActivity;
import com.guido.cluapp.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {
	 
     private WakeLock mWakeLock;
     NotificationManager   notificationManager;
     Notification          notification;
    
    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * This is where we initialize. We call this when onStart/onStartCommand is
     * called by the system. We won't do anything with the intent here
     */
    private void handleIntent(Intent intent) {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        mWakeLock.acquire();
        
        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            stopSelf();
            return;
        }
        
        // do the actual work, in a separate thread
        new PollTask().execute();
    }
    
    private class PollTask extends AsyncTask<Void, Void, Boolean> {
        
        @Override
        protected Boolean doInBackground(Void... params) {
            return checkUpdate();
        }
        
        @Override
        protected void onPostExecute(Boolean result) {
        	
        	if(result){
        	NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        	builder.setContentTitle("New Notification");
        	builder.setContentText("Testing notification");
        	builder.setNumber(1);
        	builder.setSmallIcon(android.R.drawable.stat_notify_error);
        	Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        	PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
        	builder.setContentIntent(contentIntent);
        	notificationManager=(NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        	notificationManager.notify(1, builder.build());
        	}
            stopSelf();
        }
    }
    
    /**
     * This is deprecated, but you have to implement it if you're planning on
     * supporting devices with an API level lower than 5 (Android 2.0).
     */
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
    }
    
    /**
     * This is called on 2.0+ (API level 5 or higher). Returning
     * START_NOT_STICKY tells the system to not restart the service if it is
     * killed because of poor resource (memory/cpu) conditions.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }
    
    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
    private Boolean checkUpdate() {
    	String url = getApplicationContext().getString(R.string.link_feed_1);
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		final HttpHead headRequest = new HttpHead(url);
		try {
			HttpResponse response = client.execute(headRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("checkUpdate", "Error " + statusCode
						+ " while retrieving update from " + url);
				return false;
			}
			final Header header = response.getFirstHeader("Last-Modified");
			if (header != null) {
					Log.e("Last-Modified",header.toString());
					return true;
					//TODO check date
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			headRequest.abort();
			Log.w("checkUpdate", "Error while retrieving update from " + url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return false;
	}


}