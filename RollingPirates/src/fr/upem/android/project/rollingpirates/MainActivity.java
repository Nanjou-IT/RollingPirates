package fr.upem.android.project.rollingpirates;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //here we set fonts from assets
      //  	Typeface mainMenu_tf = Typeface.createFromAsset(getAssets(), "data/fonts/oj.ttf/");
        	
        
    }
}
