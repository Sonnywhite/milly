package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.util.Mill;

public class Board {

	private final Map<String, BoardPoint> pointMap;
	private List<Mill> mills;
	
	/*
	 * A-------------B-------------C
	 * |			 |			   |
	 * |	D--------E--------F	   |
	 * |	|		 |		  |    |
	 * |	|	 G---H---I    |    |
	 * |	|	 |       |    |    |
	 * J----K----L	     M----N----O
	 * |	|	 |       |    |    |
	 * |	|	 P---Q---R    |    |
	 * |	|		 |		  |    |
	 * |	S--------T--------U    |
	 * |			 |			   |
	 * V-------------W-------------X
	 * 
	 */
	
	public Board() {
		this.pointMap = new HashMap<String, BoardPoint>();
		this.mills = new ArrayList<Mill>();
		
		buildPointMap();
		buildNeighbourhood();
		buildMillList();
		
	}
	
	private void buildPointMap() {
		pointMap.put("A", new BoardPoint("A", BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING));
		pointMap.put("B", new BoardPoint("B", BoardProperties.MAX_X/2, BoardProperties.OUTSIDE_PADDING));
		pointMap.put("C", new BoardPoint("C", BoardProperties.MAX_X-BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING));
		pointMap.put("D", new BoardPoint("D", BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING));
		pointMap.put("E", new BoardPoint("E", BoardProperties.MAX_X/2, BoardProperties.MIDDLE_PADDING));
		pointMap.put("F", new BoardPoint("F", BoardProperties.MAX_X-BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING));
		pointMap.put("G", new BoardPoint("G", BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING));
		pointMap.put("H", new BoardPoint("H", BoardProperties.MAX_X/2, BoardProperties.INNER_PADDING));
		pointMap.put("I", new BoardPoint("I", BoardProperties.MAX_X-BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING));
		pointMap.put("J", new BoardPoint("J", BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("K", new BoardPoint("K", BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("L", new BoardPoint("L", BoardProperties.INNER_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("M", new BoardPoint("M", BoardProperties.MAX_X-BoardProperties.INNER_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("N", new BoardPoint("N", BoardProperties.MAX_X-BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("O", new BoardPoint("O", BoardProperties.MAX_X-BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y/2));
		pointMap.put("P", new BoardPoint("P", BoardProperties.INNER_PADDING, BoardProperties.MAX_Y-BoardProperties.INNER_PADDING));
		pointMap.put("Q", new BoardPoint("Q", BoardProperties.MAX_X/2, BoardProperties.MAX_Y-BoardProperties.INNER_PADDING));
		pointMap.put("R", new BoardPoint("R", BoardProperties.MAX_X-BoardProperties.INNER_PADDING, BoardProperties.MAX_Y-BoardProperties.INNER_PADDING));
		pointMap.put("S", new BoardPoint("S", BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y-BoardProperties.MIDDLE_PADDING));
		pointMap.put("T", new BoardPoint("T", BoardProperties.MAX_X/2, BoardProperties.MAX_Y-BoardProperties.MIDDLE_PADDING));
		pointMap.put("U", new BoardPoint("U", BoardProperties.MAX_X-BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y-BoardProperties.MIDDLE_PADDING));
		pointMap.put("V", new BoardPoint("V", BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y-BoardProperties.OUTSIDE_PADDING));
		pointMap.put("W", new BoardPoint("W", BoardProperties.MAX_X/2, BoardProperties.MAX_Y-BoardProperties.OUTSIDE_PADDING));
		pointMap.put("X", new BoardPoint("X", BoardProperties.MAX_X-BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y-BoardProperties.OUTSIDE_PADDING));
	}
	
	private void buildNeighbourhood() {
		this.pointMap.get("A").addNeighbour("B");
		this.pointMap.get("A").addNeighbour("J");
		
		this.pointMap.get("B").addNeighbour("A");
		this.pointMap.get("B").addNeighbour("C");
		this.pointMap.get("B").addNeighbour("E");
		
		this.pointMap.get("C").addNeighbour("B");
		this.pointMap.get("C").addNeighbour("O");
		
		this.pointMap.get("D").addNeighbour("E");
		this.pointMap.get("D").addNeighbour("K");
		
		this.pointMap.get("E").addNeighbour("B");
		this.pointMap.get("E").addNeighbour("D");
		this.pointMap.get("E").addNeighbour("F");
		this.pointMap.get("E").addNeighbour("H");
		
		this.pointMap.get("F").addNeighbour("E");
		this.pointMap.get("F").addNeighbour("N");
		
		this.pointMap.get("G").addNeighbour("L");
		this.pointMap.get("G").addNeighbour("H");
		
		this.pointMap.get("H").addNeighbour("E");
		this.pointMap.get("H").addNeighbour("G");
		this.pointMap.get("H").addNeighbour("I");
		
		this.pointMap.get("I").addNeighbour("H");
		this.pointMap.get("I").addNeighbour("M");
		
		this.pointMap.get("J").addNeighbour("K");
		this.pointMap.get("J").addNeighbour("A");
		this.pointMap.get("J").addNeighbour("V");
		
		this.pointMap.get("K").addNeighbour("D");
		this.pointMap.get("K").addNeighbour("J");
		this.pointMap.get("K").addNeighbour("L");
		this.pointMap.get("K").addNeighbour("S");
		
		this.pointMap.get("L").addNeighbour("G");
		this.pointMap.get("L").addNeighbour("K");
		this.pointMap.get("L").addNeighbour("P");
		
		this.pointMap.get("M").addNeighbour("I");
		this.pointMap.get("M").addNeighbour("N");
		this.pointMap.get("M").addNeighbour("R");
		
		this.pointMap.get("N").addNeighbour("F");
		this.pointMap.get("N").addNeighbour("M");
		this.pointMap.get("N").addNeighbour("O");
		this.pointMap.get("N").addNeighbour("U");
		
		this.pointMap.get("O").addNeighbour("C");
		this.pointMap.get("O").addNeighbour("N");
		this.pointMap.get("O").addNeighbour("X");
		
		this.pointMap.get("P").addNeighbour("L");
		this.pointMap.get("P").addNeighbour("Q");
		
		this.pointMap.get("Q").addNeighbour("P");
		this.pointMap.get("Q").addNeighbour("R");
		this.pointMap.get("Q").addNeighbour("T");
		
		this.pointMap.get("R").addNeighbour("Q");
		this.pointMap.get("R").addNeighbour("M");
		
		this.pointMap.get("S").addNeighbour("T");
		this.pointMap.get("S").addNeighbour("K");
		
		this.pointMap.get("T").addNeighbour("Q");
		this.pointMap.get("T").addNeighbour("S");
		this.pointMap.get("T").addNeighbour("U");
		this.pointMap.get("T").addNeighbour("W");
		
		this.pointMap.get("U").addNeighbour("T");
		this.pointMap.get("U").addNeighbour("N");
		
		this.pointMap.get("V").addNeighbour("J");
		this.pointMap.get("V").addNeighbour("W");
		
		this.pointMap.get("W").addNeighbour("T");
		this.pointMap.get("W").addNeighbour("V");
		this.pointMap.get("W").addNeighbour("X");
		
		this.pointMap.get("X").addNeighbour("W");
		this.pointMap.get("X").addNeighbour("O");		
	}
	
	private void buildMillList() {
		mills.add(new Mill("A", "B", "C"));
		mills.add(new Mill("D", "E", "F"));
		mills.add(new Mill("G", "H", "I"));
		mills.add(new Mill("P", "Q", "R"));
		mills.add(new Mill("S", "T", "U"));
		mills.add(new Mill("V", "W", "X"));
		mills.add(new Mill("A", "J", "V"));
		mills.add(new Mill("D", "K", "S"));
		mills.add(new Mill("G", "L", "P"));
		mills.add(new Mill("I", "M", "R"));
		mills.add(new Mill("F", "N", "U"));
		mills.add(new Mill("C", "O", "X"));
		mills.add(new Mill("B", "E", "H"));
		mills.add(new Mill("J", "K", "L"));
		mills.add(new Mill("M", "N", "O"));
		mills.add(new Mill("Q", "T", "W"));
	}
	
	public List<String> getBigIntersections() {
		return Arrays.asList("E", "K", "N", "T");
	}
	
	public List<String> getSmallIntersections() {
		return Arrays.asList("B","H","J","L","M","O","Q","W");
	}
	
	public List<String> getCorners() {
		return Arrays.asList("A","C","D","F","S","U","V","X","G","I","P","R");
	}
	
	public List<Mill> getMillList() {
		return this.mills;
	}
	
	public Map<String, BoardPoint> getBoard() {
		return this.pointMap;
	}
	
	public BoardPoint getBoardPoint(String key) {
		return this.pointMap.get(key);
	}
	
}
