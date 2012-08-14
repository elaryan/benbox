package com.xoninja.benbox;


import com.xoninja.benbox.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;


public class ListadoNotas extends ListActivity {

	private NotesDbAdapter mDbHelper;
	ListView listaNotas;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadonotas);   
        
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY};
        //int[] to = new int[] { R.id.text2, R.id.text1 };
        int[] to = new int[] { R.id.textView1, R.id.textView2 };
        // Now create an array adapter and set it to display using our row
        /*SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.fila, c, from, to);*/
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.filamaquetada, c, from, to);
        setListAdapter(notes);
               
    }
    
    protected void onListItemClick(ListView l, View v, int position, 
    		long id){
    	Log.d("listview id",((Integer)l.getId()).toString());
    	Log.d("position", ((Integer)position).toString());
    	
		Cursor obj = (Cursor)l.getItemAtPosition(position);//obj SQLiteCursor
    	startManagingCursor(obj);
    	
    	String titulo = obj.getString(obj.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE));
    	String nota =  obj.getString(obj.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)); 
    	Log.d("titulo" , titulo);
    	Log.d("nota" , nota);
    		
    		
    	Intent sendIntent = new Intent();
    	sendIntent.setAction(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_TEXT, nota);
    	sendIntent.setType("text/plain");
    	startActivity(Intent.createChooser(sendIntent, "Send to..."));    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listadonotas, menu);
        return true;
    }
}
