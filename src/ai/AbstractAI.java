package ai;

import java.util.ArrayList;
import java.util.List;

import application.Board;
import application.Drawer;
import application.Game;
import application.Main;
import interfaces.Evaluation;
import interfaces.GameAI;
import javafx.scene.paint.Color;

public abstract class AbstractAI {
	
	protected final Board board;
	protected String name = "Base AI";
	protected Color color = Color.GREEN;
	protected List<String> men = new ArrayList<String>();
	protected Game game;
	protected Evaluation evaluation;
	
	public AbstractAI(Board board, String name) {
		this.board = board;
		this.name = name;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public List<String> getMen() {
		return this.men;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color==Color.BLACK?"BLACK":"WHITE";
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void add(String boardPosition, boolean showDebugOutput) {
		if(Main.DEBUG && showDebugOutput) 
			System.out.println(toString()+" Adding "+boardPosition+"" );
		this.men.add(boardPosition);
	}

	public void remove(String boardPosition, boolean showDebugOutput) {
		if(Main.DEBUG && showDebugOutput) 
			System.out.println(toString()+" Removing "+boardPosition+"" );
		this.men.remove(boardPosition);
	}
	
	public void add(String boardPosition) {
		add(boardPosition, true);
	}

	public void remove(String boardPosition) {
		remove(boardPosition, true);
	}
	
	public void drawMan(Drawer drawer, String pos) {
		drawer.drawMan(pos, this.color);
	}

	public void drawMen(Drawer drawer) {
		for (String pos : men)
			drawer.drawMan(pos, this.color);
	}
	
	public boolean boardPointIsFree(String boardPoint, GameAI ai1, GameAI ai2) {
		if(ai1.getMen().contains(boardPoint) || ai2.getMen().contains(boardPoint))
			return false;
		return true;
	}
	
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	@Override
	public String toString() {
		return "("+name+" - "+getColor()+")";
	}
}
