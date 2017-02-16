package interfaces;

import java.util.List;

import application.Drawer;
import application.Game;
import application.util.Move;
import javafx.scene.paint.Color;

public interface GameAI {
	
	public void add(String boardPosition, boolean showDebugOutput);
	public void remove(String boardPosition, boolean showDebugOutput);
	public void add(String boardPosition);
	public void remove(String boardPosition);
	public List<String> getMen();
	
	public String getPlacingPosition(GameAI opponent);
	public Move getMove(GameAI opponent) throws Exception;
	public Move getJump(GameAI opponent);
	public String removeOpponentMan(GameAI opponent);
	
	public String getName();
	public String getColor();
	
	public void setGame(Game game); 
	public void setEvaluation(Evaluation evaluation);
	
	public void setColor(Color color);
	public void drawMan(Drawer drawer, String pos);
	public void drawMen(Drawer drawer);

}
