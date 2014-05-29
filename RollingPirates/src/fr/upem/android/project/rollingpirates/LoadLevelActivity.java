package fr.upem.android.project.rollingpirates;

import java.io.IOException;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

public class LoadLevelActivity extends Activity {

	private final static String TAG = "LoadLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        int gameLevel = getIntent().getIntExtra("level", 0);
        
        AssetManager assets = getAssets();
        String[] levelList = null;
        try {
			levelList = assets.list("levels");
		} catch (IOException e) {
			Log.e(TAG, "Error : No folder 'assets/levels' was founded.");
		}

        if (levelList != null && levelList.length != 0) {
        	if (levelList.length < gameLevel) {
        		gameLevel = levelList.length-1; // No more levels, select the last one
        	}
        	// OK
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		LevelFragment level = LevelFragment.createLevel(levelList[gameLevel]);
    		transaction.replace(android.R.id.content, level);
    		transaction.commit();
        } else {
        	// NOK
        	Log.e(TAG, "Error : No ASCII level loaded into assets/levels folder.");
        }
    }
}
