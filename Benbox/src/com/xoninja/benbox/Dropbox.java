package com.xoninja.benbox;

import com.dropbox.client2.DropboxAPI;
import android.content.SharedPreferences;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.xoninja.notebox.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class Dropbox extends Activity {

	final static private String APP_KEY = "l4vyzwpfgt6hkjk";
	final static private String APP_SECRET = "u1dhenxq7o3xicr";
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	public static final String PREFERENCES_FILENAME = "DBKeys";
	private  DropboxAPI<AndroidAuthSession> mDBApi;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox);
       
        
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        
        mDBApi.getSession().startAuthentication(Dropbox.this);
        
        
    }

  
    @Override
	protected void onResume() {
        super.onResume();

        // ...

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // MANDATORY call to complete auth.
                // Sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();

                // Provide your own storeKeys to persist the access token pair
                // A typical way to store tokens is using SharedPreferences
              storeKeys(tokens.key, tokens.secret);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }

        // ...
    }
    private void storeKeys(String key, String secret) {
		// TODO Auto-generated method stub
    	SharedPreferences dbtoken = getSharedPreferences(PREFERENCES_FILENAME, MODE_MULTI_PROCESS);
    	SharedPreferences.Editor prefEditor = dbtoken.edit();
    	prefEditor.putString("tokenskey", key);
    	prefEditor.commit();
    	prefEditor.putString("tokenssecret", secret);
    	prefEditor.commit();
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropbox, menu);
        return true;
    }
}
