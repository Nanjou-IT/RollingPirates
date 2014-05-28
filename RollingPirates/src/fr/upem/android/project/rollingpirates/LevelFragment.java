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
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.upem.android.project.rollingpirates.controller.LevelController;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.view.LevelView;

public class LevelFragment extends Fragment {
	
	private final static String TAG = "LevelFragment";
	
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
		byte[] buffer = null;
		try {
			AssetManager assetManager = getActivity().getAssets();
			InputStream input = assetManager.open("levels/" + gameLevel);
			int size = input.available();
			buffer = new byte[size];
			input.read(buffer);
			input.close();
		} catch (IOException e) {
			Log.e(TAG, "Error : 'assets/levels/" + gameLevel +"' cannot be open/read/close.");
		}
		
		// Verify and format the arena world
		char[][] grid = GamePlateModel.check(buffer);
		
		RelativeLayout viewContainer = (RelativeLayout) v.findViewById(R.id.levelContainer);
		View levelView = new LevelView(getActivity(), grid);
		levelView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		viewContainer.addView(levelView);
		
		return v;
	}

}
