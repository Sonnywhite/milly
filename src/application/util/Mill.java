package application.util;

import java.util.ArrayList;
import java.util.List;

import interfaces.GameAI;

public class Mill {

	private List<String> boardPoints = new ArrayList<String>();

	public Mill(String bp1, String bp2, String bp3) {
		boardPoints.add(bp1);
		boardPoints.add(bp2);
		boardPoints.add(bp3);
	}

	public String getFirstPoint() {
		return boardPoints.get(0);
	}

	public String getSecondPoint() {
		return boardPoints.get(1);
	}

	public String getThirdPoint() {
		return boardPoints.get(2);
	}

	public List<String> getBoardPoints() {
		return boardPoints;
	}

	public void setBoardPoints(List<String> boardPoints) {
		this.boardPoints = boardPoints;
	}

	public boolean isOwnedBy(GameAI ai) {

		return ai.getMen().contains(getFirstPoint()) && ai.getMen().contains(getSecondPoint())
				&& ai.getMen().contains(getThirdPoint());
	}

	@Override
	public String toString() {
		return getBoardPoints().toString();
	}

}
