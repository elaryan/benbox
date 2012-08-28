package com.xoninja.benbox;


import com.xoninja.benbox.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint({ "NewApi", "NewApi" })
public class Nota extends Activity {

	private NotesDbAdapter mDbHelper;
	String titulo, nota, rowID;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setHomeButtonEnabled(true);
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
    	
    	
    	Toast toast;
    	Intent i;
    	switch(item.getItemId()){
	    	case R.id.nota_delete:
	    		
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("Eliminar nota?")
	    		       .setCancelable(false)
	    		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    		           public void onClick(DialogInterface dialog, int id) {
	    		        	   Toast toastDelete;
	    		        	   Intent i;
	    			    		if(deleteNote(rowID)){
	    			    						
	    			    			toastDelete = Toast.makeText(Nota.this, "Nota eliminada", Toast.LENGTH_LONG);
	    			    			toastDelete.show();
	    			    			
	    			    		}else{
	    			    			toastDelete = Toast.makeText(Nota.this, "Error al eliminar la nota", Toast.LENGTH_LONG);
	    			    			toastDelete.show();
	    			    		}
	    			    		
	    			    		i = new Intent(getApplicationContext(), ListadoNotas.class);
	    			         	startActivity(i);
	    		           }
	    		       })
	    		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	    		           public void onClick(DialogInterface dialog, int id) {
	    		                dialog.cancel();
	    		           }
	    		       });
	    		AlertDialog alert = builder.create();
	    		alert.show();
	    			
	      		break;
	      		
	    	case R.id.nota_edit:
	    		
	    		Bundle bundle = new Bundle();
	    		bundle.putString("tipo", "editar");
    			bundle.putString("titulo", titulo);
    			bundle.putString("nota", nota);
    			bundle.putString("rowId", rowID);

    			Intent editIntent = new Intent(getApplicationContext(), NuevaNota.class);
    			editIntent.putExtras(bundle);
    			startActivity(editIntent);
    			
	    		/*toast = Toast.makeText(Nota.this, "Funcionalidad pendiente", Toast.LENGTH_LONG);
    			toast.show();*/
    			
	      		break;
	    	case R.id.nota_share:
	    		Intent sendIntent = new Intent();
	        	sendIntent.setAction(Intent.ACTION_SEND);
	        	sendIntent.putExtra(Intent.EXTRA_TEXT, nota);
	        	sendIntent.setType("text/plain");
	        	startActivity(Intent.createChooser(sendIntent, "Send to..."));
	    	case R.id.menu_nuevanota:
	    		i = new Intent(getApplicationContext(), NuevaNota.class);
	         	startActivity(i);
	      		break;
	    	case R.id.menu_listadonotas:
	    		i = new Intent(getApplicationContext(), ListadoNotas.class);
	         	startActivity(i);
	      		break;
	    	
	    	default:
	    		Log.d("menu", "default");
	    		/*i = new Intent(getApplicationContext(), Dashboard.class);
	         	startActivity(i);*/
    	}
    	return true;
    	
    }
    
    
}
