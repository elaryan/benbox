package com.xoninja.benbox;



import com.xoninja.benbox.R;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


@SuppressLint("NewApi")
public class ListadoNotas extends ListActivity {

	private NotesDbAdapter mDbHelper;
	ListView listaNotas;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.listadonotas);   
        
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY};
      
        int[] to = new int[] { R.id.textView1, R.id.textView2 };
        
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.filamaquetada, c, from, to);
        setListAdapter(notes);
               
    }
    
    @SuppressWarnings("deprecation")
	protected void onListItemClick(ListView l, View v, int position, 
    		long id){
    	//Log.d("listview id",((Integer)l.getId()).toString());
    	//Log.d("position", ((Integer)position).toString());
    	
		Cursor obj = (Cursor)l.getItemAtPosition(position);
    	startManagingCursor(obj);
    	
    	String titulo = obj.getString(obj.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
    	String nota =  obj.getString(obj.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)); 
    	String rowID = obj.getString(obj.getColumnIndexOrThrow(NotesDbAdapter.KEY_ROWID));
    	Log.d("titulo" , titulo);
    	Log.d("nota" , nota);
    	Log.d("rowId", rowID);
    	
    	Bundle bundle = new Bundle();
        bundle.putString("titulo", titulo);
        bundle.putString("nota", nota);
        bundle.putString("rowId", rowID);
        
        Intent intentNota = new Intent(this, Nota.class);
        intentNota.putExtras(bundle);
        startActivity(intentNota);
    		
    		
    	/*Intent sendIntent = new Intent();
    	sendIntent.setAction(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_TEXT, nota);
    	sendIntent.setType("text/plain");
    	startActivity(Intent.createChooser(sendIntent, "Send to..."));    	*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listadonotas, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	Intent i;
    	
    	switch(item.getItemId()){
	    	case R.id.menu_nuevanota:
	    		i = new Intent(getApplicationContext(), NuevaNota.class);
	         	startActivity(i);
	      		break;
	    	default:
	    		i = new Intent(getApplicationContext(), Dashboard.class);
	         	startActivity(i);
    	}
    	return true;
    	
    }
}
