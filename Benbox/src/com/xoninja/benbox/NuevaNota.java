package com.xoninja.benbox;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.xoninja.benbox.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuevaNota extends Activity {

	private static final String PREFERENCES_FILENAME = "DBKeys";
	private NotesDbAdapter mDbHelper;
	@SuppressWarnings("unused")
	private FileIO ioHelper = new FileIO();
	 EditText tituloNota;
	 EditText contenidoNota;
	 final static private String APP_KEY = "l4vyzwpfgt6hkjk";
	 final static private String APP_SECRET = "u1dhenxq7o3xicr";
	 final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	 boolean nueva = true;//por defecto creamos siempre una nueva
	 long id = 0;
	 


    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.nuevanota);
        
        tituloNota= (EditText) findViewById(R.id.titulonota);
        contenidoNota = (EditText) findViewById(R.id.contenidonota);
        Button guardar = (Button) findViewById(R.id.button1);
        
        
        Bundle bundle = getIntent().getExtras();
        Log.d("bundle", bundle.getString("tipo"));
        
        if(((bundle.getString("tipo")).compareTo("editar")==0)){
        	Log.d("n/e", "editar");
	        String titulo = bundle.getString("titulo");
	        String nota = bundle.getString("nota");
	        String rowID = bundle.getString("rowId");
	        
	        tituloNota.setText(titulo);
	        contenidoNota.setText(nota);
	        
	        nueva = false;//indicamos que se trata de una nota ya existente
	        id = Long.valueOf(rowID);
        }
        
        
        mDbHelper = new NotesDbAdapter(this);
        
        guardar.setOnClickListener(new OnClickListener(){
          	public void onClick(View v){
          		Log.d("inside", "onclick");
          		guardarNota(tituloNota.getText().toString(),contenidoNota.getText().toString(), nueva, id);
				new UploadTask().execute(tituloNota.getText().toString(), contenidoNota.getText().toString());
          		sendNotification(tituloNota.getText().toString());
          		       
          	}
         
                   
    		private void guardarNota(String titulo, String contenido, boolean nueva, long id) {
   			    Log.d("inside", "guardar");
    			mDbHelper.open();
   			    if (nueva){
			        long result = mDbHelper.createNote(titulo, contenido);
			        if (result!= -1)
			        {
			        	Intent i = new Intent(getApplicationContext(), Dashboard.class);
			        	startActivity(i);		        	
			        }
			        else
			        	Toast.makeText(NuevaNota.this, "FAIL", Toast.LENGTH_LONG).show();    		        
	    			}
	    		else{
	    			boolean result = mDbHelper.updateNote(id, titulo, contenido);
			        if (result)
			        {
			        	Intent i = new Intent(getApplicationContext(), Dashboard.class);
			        	startActivity(i);		        	
			        }
			        else
			        	Toast.makeText(NuevaNota.this, "FAIL", Toast.LENGTH_LONG).show();
    			
    		}
    		}
         });//saveOnClickListener
        
        
    }//onCreate
        
      
    @SuppressWarnings("deprecation")
	public void sendNotification(String fileName)
    {
        // Execute Check and Notify
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.dropbox;
        CharSequence tickerText = "finished upload!";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        Context context = getApplicationContext();
        CharSequence contentTitle = "Finished Upload";
        CharSequence contentText = fileName;
        Intent notificationIntent = new Intent(this, ListadoNotas.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.defaults |= Notification.DEFAULT_SOUND;
        //notification.defaults |= Notification.DEFAULT_VIBRATE;

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        int HELLO_ID = 1;
        mNotificationManager.notify(HELLO_ID, notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nuevanota, menu);
        return true;
    }
    
 public boolean onOptionsItemSelected(MenuItem item){
    	
    	Intent i;
    	
    	switch(item.getItemId()){
	    	case R.id.menu_listadonotas:
	    		i = new Intent(getApplicationContext(), ListadoNotas.class);
	         	startActivity(i);
	      		break;
	    	default:
	    		i = new Intent(getApplicationContext(), Dashboard.class);
	         	startActivity(i);
    	}
    	return true;
    	
    }
    
    private void upload(String nombreFichero, String contenidoFichero) throws IOException{
    	
    	DropboxAPI<AndroidAuthSession> mDBApi;
    	/*AccessTokenPair access = getStoredKeys();
       	mDBApi.getSession().setAccessTokenPair(access);*/
    	AndroidAuthSession session = buildSession();
    	mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    	// Uploading content.

    	FileInputStream inputStream = null;
    	try {
    	   // File file = new File("/notes/"+ nombreFichero + ".txt");
    	   
    	   ContextWrapper ctx = new ContextWrapper(this);
    	   String directorio =  ctx.getFilesDir().toString();
    	   Log.d("directorio",directorio);
    	   File file = new File(directorio + nombreFichero + ".txt");
    	    Writer out = new OutputStreamWriter(new FileOutputStream(file));
    	    try {
    	      out.write(contenidoFichero);
    	    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
    	        out.close();
    	    }
    	    
    	   inputStream = new FileInputStream(file);
    	    com.dropbox.client2.DropboxAPI.Entry newEntry = mDBApi.putFile(nombreFichero + ".txt", inputStream,
    	            file.length(), null, null);
    	    Log.i("DbExampleLog", "The uploaded file's rev is: " + newEntry.rev);
    	} catch (DropboxUnlinkedException e) {
    	    // User has unlinked, ask them to link again here.
    	    Log.e("DbExampleLog", "User has unlinked.");
    	} catch (DropboxException e) {
    	    Log.e("DbExampleLog", "Something went wrong while uploading.");
    	} catch (FileNotFoundException e) {
    	    Log.e("DbExampleLog", "File not found.");
    	} finally {
    	    if (inputStream != null) {
    	        try {
    	            inputStream.close();
    	        } catch (IOException e) {}
    	    }
    	}
    }



	private String[] getStoredKeys() {
		// TODO Auto-generated method stub
		SharedPreferences dbtoken = getSharedPreferences(PREFERENCES_FILENAME, MODE_MULTI_PROCESS);
    	//SharedPreferences.Editor prefEditor = dbtoken.edit();
    	//AccessTokenPair token = new AccessTokenPair(dbtoken.getString("tokenskey", null), dbtoken.getString("tokenssecret", null));
		String[] token = new String[2];
		token[0] = dbtoken.getString("tokenskey", null);
		token[1] = dbtoken.getString("tokenssecret", null);
		
    	return token;

	}
	
	 private AndroidAuthSession buildSession() {
	        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
	        AndroidAuthSession session;

	        String[] stored = getStoredKeys();
	        if (stored != null) {
	            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
	            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
	        } else {
	            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
	        }

	        return session;
	    }
	 
	 class UploadTask extends AsyncTask<String, Integer, String> {

		 @Override

		 protected void onPreExecute() {

		    super.onPreExecute();
		   //displayProgressBar("Downloading...");
		 }
		 @Override
		 protected String doInBackground(String... params) {

		 	String titulo = params[0];
		 	String contenido = params[1];
		 	try {
				upload(titulo, contenido);
			} 
		 	catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		    return "All Done!";
		 }

		 @Override
		 protected void onProgressUpdate(Integer... values) {
		    super.onProgressUpdate(values);
		   // updateProgressBar(values[0]);
		 }
		 @Override
		 protected void onPostExecute(String result) {
		    super.onPostExecute(result);
		    //(dismissProgressBar();
		 }
		 }
}


