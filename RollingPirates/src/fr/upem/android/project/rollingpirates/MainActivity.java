package fr.upem.android.project.rollingpirates;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MenuFragment mainMenu = MenuFragment.createMainMenu();
        transaction.replace(android.R.id.content, mainMenu);
        transaction.commit();
        
        
    	/*newGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});*/
	
      //  Log.d("DEBUG",newGame.getText().toString());
   //     boolean i = newGame.isClickable();
    // Log.d("DEBUG",String.valueOf(i));
    }
}
