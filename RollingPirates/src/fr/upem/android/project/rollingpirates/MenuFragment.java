package fr.upem.android.project.rollingpirates;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment{
	public MenuFragment() {
	
	}
	public static MenuFragment createMainMenu() {
		return new MenuFragment(); //je laisse vide pour l'instant pour des raisons de compilation obscure
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.mainmenu_layout, container);
		return v;
	}

}
