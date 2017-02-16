package eval;

import application.Board;
import application.Game;
import interfaces.Evaluation;
import interfaces.GameAI;

public class BaseEvaluation implements Evaluation {

	protected int cornerPosVal = 2;
	protected int enemyCornerPosVal = cornerPosVal;
	protected int smallIntersecPosVal = 3;
	protected int enemySmallIntersecPosVal = smallIntersecPosVal;
	protected int bigIntersecPosVal = 5;
	protected int enemyBigIntersecPosVal = bigIntersecPosVal;
	protected int millVal = 4;
	protected int enemyMillVal = millVal;
	protected int unmoveableManVal = -5;
	protected int enemyUnmoveableManVal = unmoveableManVal;
	
	/**
	 * evaluates the board state for the turn AI
	 * 
	 * @param game
	 * @return
	 */
	public int evaluateBoardState(Game game) {
		
		@SuppressWarnings("unused")
		long start = System.currentTimeMillis();
		
		GameAI turnAI = game.getTurnAI();
		GameAI idleAI = game.getIdleAI();
		Board board = game.getBoard();
		
		int value = turnAI.getMen().size()-idleAI.getMen().size();
		
		traverseMen(value, false, turnAI, board, game);
		traverseMen(value, true, idleAI, board, game);
		
		/*
		if(Main.DEBUG)
			System.out.println("Eval ("+value+", turnAI="+game.getTurnAI().toString()+") finished after: "+(System.currentTimeMillis()-start)+"ms");
			*/
		
		return value;
	}
	
	private void traverseMen(int value, boolean isEnemy, GameAI ai, Board board, Game game) {
		for(String man : ai.getMen()) {
			
			// positions
			if(board.getCorners().contains(man)) {
				value+=isEnemy?-enemyCornerPosVal:cornerPosVal;
			} else if (board.getSmallIntersections().contains(man)) {
				value+=isEnemy?-enemySmallIntersecPosVal:smallIntersecPosVal;
			} else if (board.getBigIntersections().contains(man)) {
				value+=isEnemy?-enemyBigIntersecPosVal:bigIntersecPosVal;
			}
			
			// check if stone can move
			if(!game.isManMoveable(man))
				value+=isEnemy?-enemyUnmoveableManVal:unmoveableManVal;
			
			// mill
			if(board.getBoardPoint(man).isInMill(board, ai))
				value+=isEnemy?-enemyMillVal:millVal;
			
			// double mill ?
			
		}
	}
	
}