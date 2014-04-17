package fr.upem.android.project.rollingpirates;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuFragment extends Fragment{
	public MenuFragment() {
	}
	
	public static MenuFragment createMainMenu() {
		MenuFragment mf = new MenuFragment();
		Bundle bundleMenu = new Bundle();
		bundleMenu.putString("position", "main");
		mf.setArguments(bundleMenu);
		
		return mf; 
	}
	public static MenuFragment createSettingMenu() {
		MenuFragment mf = new MenuFragment();
		Bundle bundleMenu = new Bundle();
		bundleMenu.putString("position", "setting");
		mf.setArguments(bundleMenu);
		Log.d("DEBUG", "factory settings");
		return mf; 
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Bundle menuBundle = getArguments();
		
		String position = menuBundle.getString("position");
		Log.d("DEBUG", "la");
		if("main".equals(position)){
			v = inflater.inflate(R.layout.mainmenu_layout, null);
			setFontToTextViews(v, getActivity());
			setTextViewListener(v, getActivity());
			Log.d("DEBUG", "ici");
		}
		else if ("setting".equals(position)){
			v = inflater.inflate(R.layout.settings_layout, null);
			setFontToTextViews(v, getActivity());
		}
		
		return v;
	}
	/*
	 * sets Font of text Views on the menu with a font from the assets folder
	 */
	public static void setFontToTextViews(View view, Context c){ 
		 LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.thelayout);
		int count = theLayout.getChildCount();
		
		for(int i = 0 ; i < count; i++) {
	
			View tmpView = theLayout.getChildAt(i);		
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;
				AssetManager am = c.getAssets();
				Typeface mainMenu_tf = Typeface.createFromAsset(am, "fonts/oj.ttf");
				tv.setTypeface(mainMenu_tf);		
			}
		}
	}
	/*
	 * set Listener on all items on the main menu and sub menus
	 * 
	 */
	public static void setTextViewListener(View view,final Context c) {
		 LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.thelayout);
		int count = theLayout.getChildCount();
		
		for(int i = 0 ; i < count; i++) {
	
			View tmpView = theLayout.getChildAt(i);		
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;
				tv.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TextView tv = (TextView) v ;
						
						String text = tv.getText().toString();
						if ("New Game".equals(text)) {
							
							Intent i = new Intent( c.getApplicationContext() ,LoadLevelActivity.class);
							c.startActivity(i);
						}
						if ("Settings".equals(text)) {
							Intent i = new Intent( c.getApplicationContext() ,MainActivity.class);
							i.putExtra("selectedItem", text);
							c.startActivity(i);
						}
						if ("Help".equals(text)) {
							
						}
						if ("About".equals(text)) {
		
						}
						if ("Exit".equals(text)) {
		
						}
					}
				});
			
			}
		}
	}
}
