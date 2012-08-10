package com.xoninja.benbox;

import com.xoninja.benbox.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Dashboard extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.dashboard);
         
         GridView gridview = (GridView) findViewById(R.id.grid);
         gridview.setAdapter(new ImageAdapter(this));
         
         gridview.setOnItemClickListener(new OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
             	Intent i;
                 switch (position){
                 case 0:
                 	i = new Intent(getApplicationContext(), NuevaNota.class);
                 	startActivity(i);
                 	break;
                 case 1:
                 	//Toast.makeText(Dashboard.this, "" + position + " SOON", Toast.LENGTH_LONG).show();
                 	i = new Intent(getApplicationContext(), ListadoNotas.class);
                 	startActivity(i);
                 	break;
                 case 2:
                	 i = new Intent(getApplicationContext(), Dropbox.class);
                	 startActivity(i);
                	 break;
                 }
                 	
             	//Toast.makeText(Dashboard.this, "" + position, Toast.LENGTH_SHORT).show();
             }
         });
     }
     
    

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.dashboard, menu);
         return true;
     }

     
 }