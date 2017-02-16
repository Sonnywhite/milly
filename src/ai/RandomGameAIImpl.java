package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import application.Board;
import application.BoardPoint;
import application.Main;
import application.util.Move;
import interfaces.GameAI;

public class RandomGameAIImpl extends AbstractAI implements GameAI {

	public RandomGameAIImpl(Board board, String name) {
		super(board, name);
	}

	@Override
	public String getPlacingPosition(GameAI opponent) {
		List<String> freePositions = new ArrayList<String>();
		for (Entry<String, BoardPoint> entry : board.getBoard().entrySet()) {
			if (boardPointIsFree(entry.getKey(), this, opponent))
				freePositions.add(entry.getKey());
		}
		Random random = new Random();
		String placingPos =  freePositions.get(random.nextInt(freePositions.size()));
		if(Main.DEBUG)
			System.out.println("("+name+" - "+getColor()+") Placing man on "+placingPos);
		return placingPos;
	}

	@Override
	public Move getMove(GameAI opponent) {
		List<Move> possibleMoves = new ArrayList<>();
		for(String from : getMen()) {
			
			// traverse neighbors and look for a free position
			for(String to : board.getBoardPoint(from).getNeighboursList()) {
				if(boardPointIsFree(to, this, opponent)) {
					possibleMoves.add(new Move(from,to));
				}
			}
		}
		Random random = new Random();
		Move move = possibleMoves.get(random.nextInt(possibleMoves.size()));
		if(Main.DEBUG)
			System.out.println("("+name+" - "+getColor()+") Moving from "+move.getFrom()+ " to "+move.getTo());
		return move;
	}

	@Override
	public Move getJump(GameAI opponent) {
		List<String> freePositions = new ArrayList<String>();
		for (Entry<String, BoardPoint> entry : board.getBoard().entrySet()) {
			if (boardPointIsFree(entry.getKey(), this, opponent))
				freePositions.add(entry.getKey());
		}
		Random random = new Random();
		Move move = new Move(
				getMen().get(random.nextInt(getMen().size())), 
				freePositions.get(random.nextInt(freePositions.size())));
		if(Main.DEBUG)
			System.out.println("("+name+" - "+getColor()+") Jumping from "+move.getFrom()+ " to "+move.getTo());
		return move;
	}

	@Override
	public String removeOpponentMan(GameAI opponent) {
		List<String> possibleRemoves = new ArrayList<>();
		for(String man : opponent.getMen()) {
			if(!board.getBoardPoint(man).isInMill(board, opponent))
				possibleRemoves.add(man);
		}
		// 2010 rule allows one to remove a man from a mill if the opponent got's only mills
		if(possibleRemoves.size()==0)
			possibleRemoves.addAll(opponent.getMen());
		Random random = new Random();
		String toRemove =  possibleRemoves.get(random.nextInt(possibleRemoves.size()));
		if(Main.DEBUG)
			System.out.println("("+name+" - "+getColor()+") toRemove: "+toRemove);
		return toRemove;
	}

}
