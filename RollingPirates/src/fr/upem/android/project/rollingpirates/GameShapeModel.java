package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameShapeModel {
	
	private final List<Shape> shapes = new ArrayList<Shape>();
    private final List<ShapeListener> listeners = new LinkedList<ShapeListener>();
	
	public GameShapeModel() {
		// TODO Auto-generated constructor stub
	}
	
	public static GameShapeModel init(byte[] level) {
		GameShapeModel gm = new GameShapeModel();
		
		return gm;
	}
	
	public void addGameListener(ShapeListener listener){
    	listeners.add(listener);
    }
    
    protected void fireGameChanged(){
    	for (ShapeListener l : listeners) {
    		l.onChange();
    	}
    }
}
