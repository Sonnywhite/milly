package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import application.Board;
import application.BoardPoint;
import application.Main;
import application.util.Mill;
import application.util.Move;
import interfaces.GameAI;

public class AggressiveNaiveGameAIImpl extends AbstractAI implements GameAI {

	public AggressiveNaiveGameAIImpl(Board board, String name) {
		super(board, name);
	}

	/*
	 * Idea: Choose free positions next to other men from yourself. Set a mill
	 * if possible.
	 */
	@Override
	public String getPlacingPosition(GameAI opponent) {
		
		String placingPos = "";

		// 1. try to fill a mill
		if (getMen().size() >= 2) {
			A: for (Mill mill : board.getMillList()) {
				int samePoints = 0;
				if (getMen().contains(mill.getFirstPoint()))
					samePoints++;
				if (getMen().contains(mill.getSecondPoint()))
					samePoints++;
				if (getMen().contains(mill.getThirdPoint()))
					samePoints++;

				if (samePoints == 2) {
					// test if last point is free
					for (String point : mill.getBoardPoints()) {
						if (boardPointIsFree(point, this, opponent)) {
							placingPos = point;
							if (Main.DEBUG)
								System.out.println(
										"(" + name + " - " + getColor() + ") Fill mill placing man on " + placingPos);
							break A;
						}
					}
				}
			}
		}

		// 2. get a free neighbor of some point
		if (placingPos == "") {
			B: for (String man : getMen()) {
				for (String neighbor : board.getBoardPoint(man).getNeighboursList()) {
					if (boardPointIsFree(neighbor, this, opponent)) {
						placingPos = neighbor;
						if (Main.DEBUG) {
							System.out.println("(" + name + " - " + getColor() + ") Placing next to neighbor " + man
									+ " man on " + placingPos);
						}
						break B;
					}
				}
			}

			// 3. choose a random free position
			if (placingPos == "") {
				List<String> freePositions = new ArrayList<String>();
				for (Entry<String, BoardPoint> entry : board.getBoard().entrySet()) {
					if (boardPointIsFree(entry.getKey(), this, opponent))
						freePositions.add(entry.getKey());
				}
				Random random = new Random();
				placingPos = freePositions.get(random.nextInt(freePositions.size()));
				if (Main.DEBUG)
					System.out.println("(" + name + " - " + getColor() + ") Random placing man on " + placingPos);
			}
		}

		return placingPos;
	}

	@Override
	public Move getMove(GameAI opponent) throws Exception {
		List<Move> possibleMoves = new ArrayList<>();
		
		// close a mill
		for (Mill mill : board.getMillList()) {
			int samePoints = 0;
			if (getMen().contains(mill.getFirstPoint()))
				samePoints++;
			if (getMen().contains(mill.getSecondPoint()))
				samePoints++;
			if (getMen().contains(mill.getThirdPoint()))
				samePoints++;

			if (samePoints == 2) {
				// AI owns 2 points of that mill
				
				// last mill point free?
				for (String point : mill.getBoardPoints()) {
					if (boardPointIsFree(point, this, opponent)) {
						// last mill point is free
						
						// is there an AI neighbor that could move onto this point?
						for(String neighbor : board.getBoardPoint(point).getNeighboursList()) {
							if(getMen().contains(neighbor) && !mill.getBoardPoints().contains(neighbor)) {
								// the neighbor is owned by that AI
								// but not part of the current mill
								// also the current point is free => move!
								Move move = new Move(neighbor, point);
								possibleMoves.add(move);
								if (Main.DEBUG)
									System.out.println("(" + name + " - " + getColor() + ") Possible Mill! " + move);
							}
						}
					}
				}
			}
		}
		
		if(possibleMoves.size()==0) {
			// open a mill
			for(Mill mill : board.getMillList()) {
				if(getMen().containsAll(mill.getBoardPoints())) {
					for(String millPoint : mill.getBoardPoints()) {
						for(String neighbor : board.getBoardPoint(millPoint).getNeighboursList()) {
							if(boardPointIsFree(neighbor, this, opponent)) {
								// complete mill is owned by that AI
								// that mill point got a free neighbor
								// => move there
								Move move = new Move(millPoint, neighbor);
								possibleMoves.add(move);
								if (Main.DEBUG)
									System.out.println("(" + name + " - " + getColor() + ") Possible Open Mill " + move);
							}
						}
					}
				}
			}
			
			if(possibleMoves.size()==0) {
				// do a random move
				for (String from : getMen()) {
					// traverse neighbors and look for a free position
					for (String to : board.getBoardPoint(from).getNeighboursList()) {
						if (boardPointIsFree(to, this, opponent)) {
							possibleMoves.add(new Move(from, to));
						}
					}
				}
			}
		}
		
		if(possibleMoves.size()==0)
			throw new Exception("No move possible!");
		
		Random random = new Random();
		Move move = possibleMoves.get(random.nextInt(possibleMoves.size()));
		if (Main.DEBUG)
			System.out.println("(" + name + " - " + getColor() + ") Moving " + move);
		return move;
	}

	@Override
	public Move getJump(GameAI opponent) {
		
		// close a mill
		for (Mill mill : board.getMillList()) {
			int samePoints = 0;
			if (getMen().contains(mill.getFirstPoint()))
				samePoints++;
			if (getMen().contains(mill.getSecondPoint()))
				samePoints++;
			if (getMen().contains(mill.getThirdPoint()))
				samePoints++;

			if (samePoints == 2) {
				// AI owns 2 points of that mill
				
				// last mill point free?
				for (String point : mill.getBoardPoints()) {
					if (boardPointIsFree(point, this, opponent)) {
						// last mill point is free
						
						// jump with last free point into this mill
						for(String man : getMen()) {
							if(!mill.getBoardPoints().contains(man)) {
								return new Move(man, point);
							}
						}
					}
				}
			}
		}
		
		List<String> freePositions = new ArrayList<String>();
		for (Entry<String, BoardPoint> entry : board.getBoard().entrySet()) {
			if (boardPointIsFree(entry.getKey(), this, opponent))
				freePositions.add(entry.getKey());
		}
		Random random = new Random();
		Move move = new Move(getMen().get(random.nextInt(getMen().size())),
				freePositions.get(random.nextInt(freePositions.size())));
		if (Main.DEBUG)
			System.out.println("(" + name + " - " + getColor() + ") Jumping " + move);
		return move;
	}

	@Override
	public String removeOpponentMan(GameAI opponent) {
		List<String> possibleRemoves = new ArrayList<>();
		for (String man : opponent.getMen()) {
			if(!board.getBoardPoint(man).isInMill(board, opponent))
				possibleRemoves.add(man);
		}
		// 2010 rule allows one to remove a man from a mill if the opponent
		// got's only mills
		if (possibleRemoves.size() == 0) {
			if (Main.DEBUG)
				System.out.println("No piece outside a mill found.");
			possibleRemoves.addAll(opponent.getMen());
		}
		Random random = new Random();
		String toRemove = possibleRemoves.get(random.nextInt(possibleRemoves.size()));
		if (Main.DEBUG)
			System.out.println("(" + name + " - " + getColor() + ") toRemove: " + toRemove);
		return toRemove;
	}

}
