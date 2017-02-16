package application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Drawer {

	private Canvas canvas;
	private GraphicsContext gc;
	
	private final Board board;

	public Drawer(Board board) {
		this.board = board;
		this.canvas = new Canvas(BoardProperties.WIDTH, BoardProperties.HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		
		drawPlayground();
	}

	private void drawPlayground() {
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);

		// outside square
		gc.strokeLine(BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_X - BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING);
		gc.strokeLine(BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y - BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_X - BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y - BoardProperties.OUTSIDE_PADDING);
		gc.strokeLine(BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y - BoardProperties.OUTSIDE_PADDING);
		gc.strokeLine(BoardProperties.MAX_X - BoardProperties.OUTSIDE_PADDING, BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_X - BoardProperties.OUTSIDE_PADDING, BoardProperties.MAX_Y - BoardProperties.OUTSIDE_PADDING);

		// middle square
		gc.strokeLine(BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_X - BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING);
		gc.strokeLine(BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y - BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_X - BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y - BoardProperties.MIDDLE_PADDING);
		gc.strokeLine(BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y - BoardProperties.MIDDLE_PADDING);
		gc.strokeLine(BoardProperties.MAX_X - BoardProperties.MIDDLE_PADDING, BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_X - BoardProperties.MIDDLE_PADDING, BoardProperties.MAX_Y - BoardProperties.MIDDLE_PADDING);

		// inner square
		gc.strokeLine(BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING, BoardProperties.MAX_X - BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING);
		gc.strokeLine(BoardProperties.INNER_PADDING, BoardProperties.MAX_Y - BoardProperties.INNER_PADDING, BoardProperties.MAX_X - BoardProperties.INNER_PADDING, BoardProperties.MAX_Y - BoardProperties.INNER_PADDING);
		gc.strokeLine(BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING, BoardProperties.MAX_Y - BoardProperties.INNER_PADDING);
		gc.strokeLine(BoardProperties.MAX_X - BoardProperties.INNER_PADDING, BoardProperties.INNER_PADDING, BoardProperties.MAX_X - BoardProperties.INNER_PADDING, BoardProperties.MAX_Y - BoardProperties.INNER_PADDING);

		// lines
		gc.strokeLine(BoardProperties.WIDTH / 2, BoardProperties.OUTSIDE_PADDING, BoardProperties.WIDTH / 2, BoardProperties.INNER_PADDING);
		gc.strokeLine(BoardProperties.OUTSIDE_PADDING, BoardProperties.HEIGHT / 2, BoardProperties.INNER_PADDING, BoardProperties.HEIGHT / 2);
		gc.strokeLine(BoardProperties.WIDTH - BoardProperties.INNER_PADDING, BoardProperties.HEIGHT / 2, BoardProperties.WIDTH - BoardProperties.OUTSIDE_PADDING, BoardProperties.HEIGHT / 2);
		gc.strokeLine(BoardProperties.WIDTH / 2, BoardProperties.HEIGHT - BoardProperties.INNER_PADDING, BoardProperties.WIDTH / 2, BoardProperties.HEIGHT - BoardProperties.OUTSIDE_PADDING);
	}
	
	public void drawMan(String key, Color color) {
		gc.setFill(color);
		drawMan(key);
	}
	
	public void clear() {
		gc.clearRect(0, 0, BoardProperties.WIDTH, BoardProperties.HEIGHT);
		drawPlayground();
	}

	private void drawMan(String key) {
		BoardPoint boardPoint = board.getBoardPoint(key);
		double x = boardPoint.getX();
		double y = boardPoint.getY();
		gc.strokeOval(x - BoardProperties.MAN_WIDTH / 2, y - BoardProperties.MAN_WIDTH / 2, BoardProperties.MAN_WIDTH, BoardProperties.MAN_WIDTH);
		gc.fillOval(x - BoardProperties.MAN_WIDTH / 2, y - BoardProperties.MAN_WIDTH / 2, BoardProperties.MAN_WIDTH, BoardProperties.MAN_WIDTH);
	}

	public Canvas getCanvas() {
		return this.canvas;
	}
	
	public void drawMove(String from, String to) {
		Paint temp = gc.getStroke();
		
		gc.setStroke(Color.RED);
		gc.setLineWidth(3);
		BoardPoint f = board.getBoardPoint(from);
		BoardPoint t = board.getBoardPoint(to);		
		gc.strokeLine(f.getX(), f.getY(), t.getX(), t.getY());
		
		gc.setStroke(temp);
	}
	
	public void drawRemove(String pos) {
		Paint temp = gc.getStroke();
		
		gc.setStroke(Color.RED);
		BoardPoint boardPoint = board.getBoardPoint(pos);
		double x = boardPoint.getX();
		double y = boardPoint.getY();
		gc.strokeOval(x - BoardProperties.MAN_WIDTH / 2, y - BoardProperties.MAN_WIDTH / 2, BoardProperties.MAN_WIDTH, BoardProperties.MAN_WIDTH);
		
		gc.setStroke(temp);
	}

}
