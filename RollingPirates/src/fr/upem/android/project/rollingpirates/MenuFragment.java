package fr.upem.android.project.rollingpirates;

import java.io.IOException;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuFragment extends Fragment{
	public MenuFragment() { }
	
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
		return mf; 
	}
	public static MenuFragment createLevelPicker(){
		MenuFragment mf = new MenuFragment();
		Bundle bundleMenu = new Bundle();
		bundleMenu.putString("position", "levelpicker");
		mf.setArguments(bundleMenu);
		return mf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Bundle menuBundle = getArguments();

		String position = menuBundle.getString("position");
		if ("main".equals(position)) {
			v = inflater.inflate(R.layout.mainmenu_layout, null);
			setFontToTextViews(v, getActivity());
			setTextViewListener(v, getActivity());
		} else if ("setting".equals(position)) {
			v = inflater.inflate(R.layout.settings_layout, null);
			setFontToTextViewsSettings(v, getActivity());
			Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
			        R.array.levelList, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		}else if ("levelpicker".equals(position)){
		v = inflater.inflate(R.layout.levelpicker_layout, null);
			
			try {
				String[] levels = getActivity().getAssets().list("levels");
				LinearLayout theLayout = (LinearLayout) v.findViewById(R.id.thelayout);
				LinearLayout child = (LinearLayout) theLayout.getChildAt(0);
				for(int i = 0 ; i < levels.length; i ++) {
					//TODO: format string programmatically
					if(levels[i].equals("level1")){
						TextView tv = new TextView(getActivity());
						tv.setText("Level 1");
						child.addView(tv);
						
					}
					if(levels[i].equals("level2")){
						TextView tv = new TextView(getActivity());
						tv.setText("Level 2");						
						child.addView(tv);
					}
					if(levels[i].equals("level3")){
						TextView tv = new TextView(getActivity());
						tv.setText("Level 3");						
						child.addView(tv);
					}
				}
				setFontToTextViewsLevelPicker(v, getActivity());
				setTextViewListenerLevelPicker(v, getActivity());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return v;
	}
	
	/*
	 * sets Font of text Views on the menu with a font from the assets folder
	 */
	public static void setFontToTextViews(View view, Context c){ 
		LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.thelayout);
		int count = theLayout.getChildCount();

		for (int i = 0 ; i < count; i++) {
			View tmpView = theLayout.getChildAt(i);		
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;
				AssetManager am = c.getAssets();
				Typeface mainMenu_tf = Typeface.createFromAsset(am, "fonts/oj.ttf");
				tv.setTypeface(mainMenu_tf);	
				tv.setClickable(true);
				
				// lp.height = 60;
				 //tv.setLayoutParams(lp);
			}
		}
	}
	public static void setFontToTextViewsLevelPicker(View view, Context c){ 
		LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.child);
		int count = theLayout.getChildCount();

		for (int i = 0 ; i < count; i++) {
			View tmpView = theLayout.getChildAt(i);		
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;				
				tv.setClickable(true);
				tv.setTextAppearance(c, R.style.MenuFont);
				AssetManager am = c.getAssets();
				Typeface mainMenu_tf = Typeface.createFromAsset(am, "fonts/oj.ttf");
				tv.setTypeface(mainMenu_tf);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				params.setMargins(20,20,20,20);
				tv.setLayoutParams(params);
			}
		}
	}
	/*
	 * sets Font of text Views on the settings menu with a font from the assets folder
	 */
	public static void setFontToTextViewsSettings(View view, Context c){ 
		LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.thelayout);
		int count = theLayout.getChildCount();

		for (int i = 0 ; i < count; i++) {
			View tmpView = theLayout.getChildAt(i);	
			if(tmpView instanceof LinearLayout){
				tmpView = ((LinearLayout) tmpView).getChildAt(0);
				if (tmpView instanceof TextView) {
					TextView tv = (TextView) tmpView;
					AssetManager am = c.getAssets();
					Typeface mainMenu_tf = Typeface.createFromAsset(am, "fonts/oj.ttf");
					tv.setTypeface(mainMenu_tf);		
				}
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

		for (int i = 0 ; i < count; i++) {
			View tmpView = theLayout.getChildAt(i);		
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;
				
				tv.setOnClickListener( new OnClickListener() {

					@Override
					public void onClick(View v) {
						TextView tv = (TextView) v ;

						String text = tv.getText().toString();
						if ("New Game".equals(text)) {
							Intent i = new Intent(c.getApplicationContext(), MainActivity.class);
							i.putExtra("selectedItem", text);
							c.startActivity(i);
						}
						if ("Settings".equals(text)) {
							Intent i = new Intent(c.getApplicationContext(), MainActivity.class);
							i.putExtra("selectedItem", text);
							c.startActivity(i);
						}
						if ("Help".equals(text)) {

						}
						if (text.contains("level")){
							Intent i = new Intent(c.getApplicationContext(), LoadLevelActivity.class);
							i.putExtra("level", (int)text.charAt(text.length() -1));
							c.startActivity(i);
						}
						if ("Exit".equals(text)) {
								System.exit(0);
						}
					}
				});
			}
		}
		
	}
	public static void setTextViewListenerLevelPicker(View view,final Context c) {
		LinearLayout theLayout = (LinearLayout) view.findViewById(R.id.child);
		int count = theLayout.getChildCount();
		for(int i = 0; i < count ; i++ ){
			View tmpView = theLayout.getChildAt(i);	
			
			if (tmpView instanceof TextView) {
				TextView tv = (TextView) tmpView;
				tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								TextView tv = (TextView) v ;
								String text = tv.toString();
								
									Intent i = new Intent( c.getApplicationContext() , LoadLevelActivity.class);
									i.putExtra("level", (int)text.charAt(text.length()-1));
									c.startActivity(i);
							}
						});
					}
				}	
			}
}
