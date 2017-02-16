package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import application.Board;
import application.BoardPoint;
import application.Main;
import application.util.Mill;
import application.util.Move;
import interfaces.GameAI;

public class MiniMaxImpl extends AbstractAI implements GameAI {

	private final int searchDepth;
	private Move move;

	public MiniMaxImpl(Board board, String name, int searchDepth) {
		super(board, name);
		this.searchDepth = searchDepth;
	}

	private List<Move> getAllPossibleMoves(GameAI ai) {
		List<Move> possibleMoves = new ArrayList<>();
		for (String man : ai.getMen()) {
			if (game.isManMoveable(man)) {
				for (String neighbour : game.getBoard().getBoardPoint(man).getNeighboursList()) {
					if (game.isBoardPointFree(neighbour))
						possibleMoves.add(new Move(man, neighbour));
				}
			}
		}
		return possibleMoves;
	}

	private int max(int player, int depth) {
		GameAI ai = player > 0 ? game.getTurnAI() : game.getIdleAI();
		if (depth == 0 || !game.movePossible(ai))
				return evaluation.evaluateBoardState(game);
		int maxValue = Integer.MIN_VALUE;
		List<Move> moves = getAllPossibleMoves(ai);
		for (Move move : moves) {
			game.doMove(ai, move, false);
			game.switchAIs();
			int value = min(Math.negateExact(player), depth - 1);
			game.undoMove(ai, move, false);
			game.switchAIs();
			if (value > maxValue) {
				maxValue = value;
				if (depth == searchDepth)
					this.move = move;
			}
		}
		return maxValue;
	}

	private int min(int player, int depth) {
		GameAI ai = player > 0 ? game.getTurnAI() : game.getIdleAI();
		if (depth == 0 || !game.movePossible(ai))
			return evaluation.evaluateBoardState(game);
		int minValue = Integer.MAX_VALUE;
		List<Move> moves = getAllPossibleMoves(ai);
		for (Move move : moves) {
			game.doMove(ai, move, false);
			game.switchAIs();
			int value = max(Math.negateExact(player), depth - 1);
			game.undoMove(ai, move, false);
			game.switchAIs();
			if (value < minValue) {
				minValue = value;
			}
		}
		return minValue;
	}

	@Override
	public String getPlacingPosition(GameAI opponent) {

		// 1. try to fill a mill
		if (getMen().size() >= 2) {
			for (Mill mill : board.getMillList()) {
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
							if (Main.DEBUG) {
								System.out.println(toString() + " Filling mill; placing man on " + point);
							}
							return point;
						}
					}
				}
			}
		}

		// 2. block opponents mill
		if (opponent.getMen().size() >= 2) {
			for (Mill mill : board.getMillList()) {
				int samePoints = 0;
				if (opponent.getMen().contains(mill.getFirstPoint()))
					samePoints++;
				if (opponent.getMen().contains(mill.getSecondPoint()))
					samePoints++;
				if (opponent.getMen().contains(mill.getThirdPoint()))
					samePoints++;

				if (samePoints == 2) {

					// test if last point is free
					for (String point : mill.getBoardPoints()) {
						if (boardPointIsFree(point, this, opponent)) {
							if (Main.DEBUG) {
								System.out.println(toString() + " Blocking mill; placing man on " + point);
							}
							return point;
						}
					}
				}
			}
		}

		// 3. choose free big intersection position
		for (String intersection : board.getBigIntersections()) {
			if (boardPointIsFree(intersection, this, opponent)) {
				if (Main.DEBUG)
					System.out.println(toString() + " Free big intersection found; placing man on " + intersection);
				return intersection;
			}
		}

		// 4. prepare own mill
		for (Mill mill : board.getMillList()) {
			String pos = "";
			if (getMen().contains(mill.getFirstPoint()) && boardPointIsFree(mill.getSecondPoint(), this, opponent)
					&& boardPointIsFree(mill.getThirdPoint(), this, opponent))
				pos = mill.getSecondPoint();
			else if (getMen().contains(mill.getSecondPoint()) && boardPointIsFree(mill.getFirstPoint(), this, opponent)
					&& boardPointIsFree(mill.getThirdPoint(), this, opponent))
				pos = mill.getFirstPoint();
			else if (getMen().contains(mill.getThirdPoint()) && boardPointIsFree(mill.getSecondPoint(), this, opponent)
					&& boardPointIsFree(mill.getFirstPoint(), this, opponent))
				pos = mill.getSecondPoint();
			if (!pos.isEmpty()) {
				if (Main.DEBUG)
					System.out.println(toString() + " Preparing mill; placing man on " + pos);
				return pos;
			}
		}

		// 5. choose free small intersection position
		for (String intersection : board.getSmallIntersections()) {
			if (boardPointIsFree(intersection, this, opponent)) {
				if (Main.DEBUG)
					System.out.println(toString() + " Free small intersection found; placing man on " + intersection);
				return intersection;
			}
		}

		// 6. choose random free position (only corner should be left)
		List<String> freePositions = new ArrayList<String>();
		for (Entry<String, BoardPoint> entry : board.getBoard().entrySet()) {
			if (boardPointIsFree(entry.getKey(), this, opponent))
				freePositions.add(entry.getKey());
		}
		Random random = new Random();
		String placingPos = freePositions.get(random.nextInt(freePositions.size()));
		if (Main.DEBUG)
			System.out.println(toString() + " Random placing man on " + placingPos);
		return placingPos;

	}

	@Override
	public Move getMove(GameAI opponent) throws Exception {

		// trivial move before min-max: close a mill
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

						// is there an AI neighbor that could move onto this
						// point?
						for (String neighbor : board.getBoardPoint(point).getNeighboursList()) {
							if (getMen().contains(neighbor) && !mill.getBoardPoints().contains(neighbor)) {
								// the neighbor is owned by that AI
								// but not part of the current mill
								// also the current point is free => move!
								Move move = new Move(neighbor, point);
								if (Main.DEBUG)
									System.out.println(toString() + " Possible Mill! " + move);
								return move;
							}
						}
					}
				}
			}
		}

		move = null;
		max(1, searchDepth);

		if (move == null)
			System.out.println(toString() + " No move possible");

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
						for (String man : getMen()) {
							if (!mill.getBoardPoints().contains(man)) {
								if (Main.DEBUG)
									System.out.println(toString() + " Mill! " + move);
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
			if (!board.getBoardPoint(man).isInMill(board, opponent))
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

	@Override
	public String toString() {
		return "(" + name + ",depth=" + searchDepth + " - " + getColor() + ")";
	}

}
