
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordCounterApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Word Counter");

        TextArea inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter your text...");
        Label resultLabel = new Label();

        Button countButton = new Button("Count Words");
        countButton.setOnAction(e -> {
            String text = inputTextArea.getText();
            int wordCount = countWords(text);
            resultLabel.setText("Total words: " + wordCount);
        });

        VBox root = new VBox(10, inputTextArea, countButton, resultLabel);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int countWords(String text) {
        String[] words = text.split("[\\s\\p{Punct}]+");
        return words.length;
    }
}
