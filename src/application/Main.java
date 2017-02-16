package application;

import ai.AggressiveNaiveGameAIImpl;
import ai.MiniMaxImpl;
import eval.AggressiveEvaluation;
import eval.BaseEvaluation;
import interfaces.GameAI;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Main extends Application {

	public static final boolean DEBUG = true;
	private static Stage primaryStage;

	public static void showDialog(String text) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text(text));
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
		try {

			// set up UI
			primaryStage.setTitle("Milly - 9 Men's Morris AI Testing");
			Group root = new Group();
			Board board = new Board();
			Drawer drawer = new Drawer(board);

			// set up AIs
			GameAI ai1 = new MiniMaxImpl(board, "MiniMaxImpl1", 6);
			GameAI ai2 = new AggressiveNaiveGameAIImpl(board, "AggressiveNaiveGameAIImpl");

			// set up Game
			Game game = new Game(drawer, board, ai1, ai2);

			// modify AIs
			ai1.setGame(game);
			ai1.setEvaluation(new BaseEvaluation());
			ai2.setGame(game);
			ai2.setEvaluation(new BaseEvaluation());

			// modify/show UI
			root.getChildren().add(drawer.getCanvas());
			primaryStage.setScene(new Scene(root));
			primaryStage.show();

			if (Main.DEBUG)
				System.out.println("---- Game started ----");

			try {
				game.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
