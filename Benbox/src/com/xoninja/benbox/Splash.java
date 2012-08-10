package com.xoninja.benbox;

import com.xoninja.notebox.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.splash);
        ImageView splash = (ImageView) findViewById(R.id.splashview);
        Drawable pantallaSplash = this.getResources().getDrawable(R.drawable.splash);
        splash.setImageDrawable(pantallaSplash);
        Handler handlerSplash = new Handler();
        handlerSplash.postDelayed(new splashhandler(), 2000);	        
 }
 
 class splashhandler implements Runnable{

	public void run() {
		startActivity(new Intent(getApplication(), Dashboard.class));
		//startActivity(new Intent(getApplication(), SelectorOrigenDatos.class));
		Splash.this.finish();
	}
	 
 }

}