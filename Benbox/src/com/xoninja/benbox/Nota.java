package com.xoninja.benbox;

import com.xoninja.benbox.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;

public class Nota extends ListActivity {

	private NotesDbAdapter mDbHelper;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota);
        
       
        Bundle bundle = getIntent().getExtras();
        Long posicion = bundle.getLong("posicion");
        
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        Cursor c = mDbHelper.fetchNote(posicion);
        startManagingCursor(c);
        
      
        String[] from = new String[] { NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_BODY};
       
        int[] to = new int[] { R.id.notatitulo, R.id.notacompleta};
        
        for(int i=0;i<from.length; i++){
        	Log.d("titulo", from[i]);
        }
        
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notamaquetada, c, from, to);
            setListAdapter(notes);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nota, menu);
        return true;
    }
}
