package com.xoninja.benbox;


import com.xoninja.benbox.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Nota extends Activity {

	private NotesDbAdapter mDbHelper;
	String titulo, nota, rowID;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota);
        
       
        Bundle bundle = getIntent().getExtras();
        titulo = bundle.getString("titulo");
        nota = bundle.getString("nota");
        rowID = bundle.getString("rowId");
        
        TextView tituloTV, notaTV;
        tituloTV = (TextView) findViewById(R.id.textView1);
        notaTV = (TextView) findViewById(R.id.textView2);
        
        tituloTV.setText(titulo);
        notaTV.setText(nota);
      
        
    }
	
	private boolean deleteNote(String rowID){
		
		mDbHelper = new NotesDbAdapter(this);
	    mDbHelper.open();
		return mDbHelper.deleteNote(Long.valueOf(rowID));
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nota, menu);
        return true;
    }
    
 public boolean onOptionsItemSelected(MenuItem item){
    	
    	Intent i;
    	Toast toast;
    	switch(item.getItemId()){
	    	case R.id.nota_delete:
	    		
	    		if(deleteNote(rowID)){
	    			toast = Toast.makeText(Nota.this, "Nota eliminada", Toast.LENGTH_LONG);
	    			toast.show();
	    		}else{
	    			toast = Toast.makeText(Nota.this, "Error al eliminar la nota", Toast.LENGTH_LONG);
	    			toast.show();
	    		}
	    		i = new Intent(getApplicationContext(), ListadoNotas.class);
	         	startActivity(i);
	    			
	      		break;
	    	case R.id.nota_edit:
	    		toast = Toast.makeText(Nota.this, "Funcionalidad pendiente", Toast.LENGTH_LONG);
    			toast.show();
	      		break;
	    	case R.id.nota_share:
	    		Intent sendIntent = new Intent();
	        	sendIntent.setAction(Intent.ACTION_SEND);
	        	sendIntent.putExtra(Intent.EXTRA_TEXT, nota);
	        	sendIntent.setType("text/plain");
	        	startActivity(Intent.createChooser(sendIntent, "Send to..."));
	    	default:
	    		Log.d("menu", "default");
	    		/*i = new Intent(getApplicationContext(), Dashboard.class);
	         	startActivity(i);*/
    	}
    	return true;
    	
    }
    
    
}
