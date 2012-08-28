package com.xoninja.benbox;

import com.xoninja.benbox.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Dashboard extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);

         setContentView(R.layout.dashboard);

         GridView gridview = (GridView) findViewById(R.id.grid);
         gridview.setAdapter(new ImageAdapter(this));
         
         gridview.setOnItemClickListener(new OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
             	Intent i;
                 switch (position){
                 case 0:
                	Bundle bundle = new Bundle();
                	bundle.putString("tipo", "nueva");
                 	i = new Intent(getApplicationContext(), NuevaNota.class);
                 	i.putExtras(bundle);
                 	startActivity(i);
                 	break;
                 case 1:
                 	i = new Intent(getApplicationContext(), ListadoNotas.class);
                 	startActivity(i);
                 	break;
                 case 2:
                	 i = new Intent(getApplicationContext(), Dropbox.class);
                	 startActivity(i);
                	 break;
                 case 3:
                	 i = new Intent(getApplicationContext(), Busqueda.class);
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