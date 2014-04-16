package fr.upem.android.project.rollingpirates;

import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment{
	public MenuFragment() {
	
	}
	public static MenuFragment createMainMenu() {
		return new MenuFragment(); 
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.mainmenu_layout, container);
		ViewGroup parentView = (ViewGroup) v.getParent();
		
		//int count = parentView.getChildCount();
		
//		Log.d("DEBUG",Integer.toString(count));
		TextView tv = (TextView) v.findViewById(R.id.begin);
		Context c = (Context) getActivity();
		AssetManager am = c.getAssets();
		Typeface mainMenu_tf = Typeface.createFromAsset(am, "fonts/oj.ttf");
		tv.setTypeface(mainMenu_tf);
		return v;
	}

}
