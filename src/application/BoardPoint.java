package application;

import java.util.ArrayList;
import java.util.List;

import application.util.Mill;
import interfaces.GameAI;
import javafx.geometry.Point2D;

public class BoardPoint extends Point2D {
	
	private final String key;
	private List<String> neighboursList;
	
	public BoardPoint(String key, double x, double y) {
		super(x, y);
		this.key = key;
		setNeighboursList(new ArrayList<>());
	}
	
	public String getKey() {
		return key;
	}

	public List<String> getNeighboursList() {
		return neighboursList;
	}

	public void setNeighboursList(List<String> neighboursList) {
		this.neighboursList = neighboursList;
	}
	
	public void addNeighbour(String boardPointKey) {
		this.neighboursList.add(boardPointKey);
	}
	
	public boolean isInMill(Board board, GameAI ai) {
		
		for(Mill mill : board.getMillList()) {
			if(mill.getBoardPoints().contains(this.key)) {
				
				// mill that contains the actual BoardPoint
				
				if(mill.isOwnedBy(ai))
					return true;
				
			}
		}
		
		return false;
	}

	

}
