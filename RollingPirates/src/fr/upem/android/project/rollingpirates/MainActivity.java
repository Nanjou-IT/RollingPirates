package fr.upem.android.project.rollingpirates;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if (i.getStringExtra("selectedItem") == null) {
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
        	MenuFragment mainMenu = MenuFragment.createMainMenu();
        	transaction.replace(android.R.id.content, mainMenu);
        	transaction.commit();
        } else {
        	if (i.getStringExtra("selectedItem").equals("Settings")) {
        		FragmentTransaction transaction = getFragmentManager().beginTransaction();
        		MenuFragment settingMenu = MenuFragment.createSettingMenu();
        		transaction.replace(android.R.id.content, settingMenu);
        		transaction.commit();
        	}
        	if(i.getStringExtra("selectedItem").equals("Help")){
        		// TODO
        	}
        	if(i.getStringExtra("selectedItem").equals("About")){
        		// TODO
        	}
        }
    }
}
