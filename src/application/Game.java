package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.util.Move;
import interfaces.GameAI;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

public class Game {

	private int stepSpeed = 4000;
	//private int stepSpeed = 4000;

	private final Drawer drawer;
	private final Board board;
	private int turn = 1;
	private GameAI turnAI;
	private GameAI idleAI;

	public Game(Drawer drawer, Board board, GameAI ai1, GameAI ai2) {
		this.drawer = drawer;
		this.board = board;
		Random random = new Random();
		Boolean b = random.nextBoolean();

		this.turnAI = b ? ai1 : ai2;
		this.turnAI.setColor(b ? Color.BLACK : Color.WHITE);
		this.idleAI = b ? ai2 : ai1;
		this.idleAI.setColor(b ? Color.WHITE : Color.BLACK);

	}

	public void start() throws Exception {

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {

				// placing pieces phase
				while (turn <= 18) {

					// request placing position
					String pos = turnAI.getPlacingPosition(idleAI);

					// check position
					if (!turnAI.getMen().contains(pos) && !idleAI.getMen().contains(pos)) {

						// set position
						turnAI.add(pos);
						turnAI.drawMan(drawer, pos);

						// mill?
						if (board.getBoardPoint(pos).isInMill(board, turnAI)) {
							if (Main.DEBUG)
								System.out.println(
										"TURN " + turn + " Mill by " + turnAI.getName() + " - " + turnAI.getColor());
							String toRemove = turnAI.removeOpponentMan(idleAI);
							if (board.getBoardPoint(toRemove).isInMill(board, idleAI) && hasRemoveableMen(idleAI)) {
								Main.showDialog(String.format("Illegal remove! %s is in a mill but has other options",
										toRemove));
							}
							idleAI.remove(toRemove);
							redraw();
							drawer.drawRemove(toRemove);
						}

						nextStep();
					} else {
						Main.showDialog(String.format(
								"Illegal placing position! AI (%s; %s) or (%s; %s) already uses that position ",
								turnAI.getName(), turnAI.getMen().contains(pos), idleAI.getName(),
								idleAI.getMen().contains(pos)));
					}

				}

				if (Main.DEBUG)
					System.out.println("---- Moving Phase ----");

				// ---- moving/jumping phase 
				while (true) {

					Move move = turnAI.getMen().size() <= 3 ? turnAI.getJump(idleAI) : turnAI.getMove(idleAI);
					String toRemove = "";

					// check move
					String from = move.getFrom();
					String to = move.getTo();

					if (turnAI.getMen().contains(from)) {
						// turn AI is able to move FROM

						if (!turnAI.getMen().contains(to) && !idleAI.getMen().contains(to)) {
							// TO is free

							// set move
							doMove(turnAI, move);
							redraw();
							

						} else {
							Main.showDialog(String.format("Illegal move! AI (%s; %s) or (%s; %s) already uses that position ",
											turnAI.getName(), turnAI.getMen().contains(to), idleAI.getName(),
											idleAI.getMen().contains(to)));
						}
					} else {
						Main.showDialog(String.format(
								"Illegal move! AI (%s; %s) or (%s; %s) already uses that position ", turnAI.getName(),
								turnAI.getMen().contains(to), idleAI.getName(), idleAI.getMen().contains(to)));
					}

					// mill?
					if (board.getBoardPoint(to).isInMill(board, turnAI)) {
						if (Main.DEBUG)
							System.out.println(
									"TURN " + turn + " Mill by " + turnAI.getName() + " - " + turnAI.getColor());
						toRemove = turnAI.removeOpponentMan(idleAI);
						
						// remove okay?
						if (board.getBoardPoint(toRemove).isInMill(board, idleAI) && hasRemoveableMen(idleAI)) {
							Main.showDialog(String.format("Illegal remove! %s is in a mill but has other options", toRemove));
						}
						idleAI.remove(toRemove);
						redraw();
					}

					
					drawer.drawMove(move.getFrom(), move.getTo());
					if(toRemove != "") {
						drawer.drawRemove(toRemove);
					}
					
					// end game?
					if (idleAI.getMen().size() < 3) {
						Main.showDialog(String.format("(2) AI '%s' was won in turn %d", turnAI.getName(), turn));
						return null;
					}
					
					nextStep();
				}
				
			}
		};

		new Thread(task).start();
	}
	
	public boolean movePossible(GameAI ai) {
		for(String man : ai.getMen()) {
			if(isManMoveable(man))
				return true;
		}
		return false;
	}
	
	private void doMove(GameAI ai, Move move) {
		doMove(ai, move, true);
	}
	
	public void doMove(GameAI ai, Move move, boolean showDebugOutput) {
		ai.remove(move.getFrom(),showDebugOutput);
		ai.add(move.getTo(),showDebugOutput);
	}
	
	public void undoMove(GameAI ai, Move move, boolean showDebugOutput) {
		ai.add(move.getFrom(),showDebugOutput);
		ai.remove(move.getTo(),showDebugOutput);
	}
	
	public void switchAIs() {
		GameAI temp = idleAI;
		idleAI = turnAI;
		turnAI = temp;
	}

	private void nextStep() throws InterruptedException {
		switchAIs();
		turn++;
		Thread.sleep(stepSpeed);
	}

	private boolean hasRemoveableMen(GameAI ai) {
		List<String> possibleRemoves = new ArrayList<>();
		for (String man : ai.getMen()) {
			if(!board.getBoardPoint(man).isInMill(board, ai))
				possibleRemoves.add(man);
		}
		return possibleRemoves.size() > 0;
	}

	private void redraw() {
		drawer.clear();
		turnAI.drawMen(drawer);
		idleAI.drawMen(drawer);
	}
	
	public boolean isBoardPointFree(String key) {
		return !turnAI.getMen().contains(key)&&!idleAI.getMen().contains(key);
	}
	
	public boolean isManMoveable(String key) {
		for(String neighbourKey : board.getBoardPoint(key).getNeighboursList()) {
			if(isBoardPointFree(neighbourKey))
				return true;
		}
		return false;
	}
	
	public Board getBoard() {
		return board;
	}

	public GameAI getTurnAI() {
		return turnAI;
	}

	public GameAI getIdleAI() {
		return idleAI;
	}

}
