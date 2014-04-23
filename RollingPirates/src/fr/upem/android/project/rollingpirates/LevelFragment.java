package fr.upem.android.project.rollingpirates;

import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LevelFragment extends Fragment {
	
	public LevelFragment() {
		// empty
	}
	
	public static LevelFragment createLevel(String gameLevel) {
		LevelFragment levelFragment = new LevelFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString("level", gameLevel);
		levelFragment.setArguments(bundle);
		return levelFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.level_layout, null);
		
		Bundle arguments = getArguments();
		String gameLevel = arguments.getString("level");
		
		Log.d("DEBUG", "Level : " + gameLevel);
		
		/*
		 * Parse text and generate the LevelView 
		 */
		InputStream input;
		try {
			AssetManager assetManager = getActivity().getAssets();
			input = assetManager.open("levels/" + gameLevel);
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();

			String text = new String(buffer);
			Log.d("DEBUG", "Data : " + text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		RelativeLayout viewContainer = (RelativeLayout) v.findViewById(R.id.levelContainer);
		
		View levelView = new LevelView(getActivity());
		levelView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		viewContainer.addView(levelView);
		
		return v;
	}

}
