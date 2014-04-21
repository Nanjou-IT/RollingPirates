package fr.upem.android.project.rollingpirates;

import java.io.IOException;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

public class LoadLevelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "activitylevel");
        // TODO : Receive the game level as an integer !
        int gameLevel = 0;
        
        
        AssetManager assets = getAssets();
        String[] levelList = null;
        try {
			levelList = assets.list("levels");
		} catch (IOException e) {
			// TODO : Handle exception
		}
        
        
        if (levelList != null && levelList.length >= gameLevel) {
        	// OK
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		LevelFragment level = LevelFragment.createLevel(levelList[gameLevel]);
    		transaction.replace(android.R.id.content, level);
    		transaction.commit();
        } else {
        	// NOK
        	// TODO : Error no ASCII file level 
        }
    }
}
