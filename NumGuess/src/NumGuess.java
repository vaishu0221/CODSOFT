import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NumGuess extends Application {

    private int secretNumber;
    private int attempts;
    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Number Guessing Game");
        primaryStage.setResizable(false);

        Label titleLabel = createLabel("Number Guessing Game", 24, "#333333");
        Label descriptionLabel = createLabel("Guess the number between 1 and 100.", 16, "#666666");

        TextField guessInput = new TextField();
        guessInput.setPromptText("Enter your guess");
        guessInput.getStyleClass().add("guess-input");

        Button submitButton = new Button("Submit Guess");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> checkGuess(guessInput));

        resultLabel = createLabel("", 18, "#333333");
        resultLabel.getStyleClass().add("result-label");

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleLabel, descriptionLabel, guessInput, submitButton, resultLabel);

        Scene scene = new Scene(vbox, 400, 300);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeGame();
    }

    private void initializeGame() {
        secretNumber = (int) (Math.random() * 100) + 1;
        attempts = 5;
        resultLabel.setText("");
    }

    private void checkGuess(TextField guessInput) {
        try {
            int userGuess = Integer.parseInt(guessInput.getText());

            String message;

            if (userGuess == secretNumber) {
                message = "Correct! You guessed the number.";
                initializeGame();
            } else {
                attempts--;

                if (attempts > 0) {
                    message = (userGuess < secretNumber) ? "Too low. Try again. Remaining attempts: " + attempts
                            : "Too high. Try again. Remaining attempts: " + attempts;
                } else {
                    message = "Out of attempts. The number was: " + secretNumber;
                    initializeGame();
                }
            }

            resultLabel.setText(message);
            guessInput.clear();
            flashResultLabel();
        } catch (NumberFormatException e) {
            showMessageBox("Error", "Invalid input. Please enter a valid number.");
        }
    }

    private Label createLabel(String text, int fontSize, String color) {
        Label label = new Label(text);
        label.setFont(new Font(fontSize));
        label.setTextFill(Color.web(color));
        return label;
    }

    private void flashResultLabel() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> resultLabel.setOpacity(1.0)),
                new KeyFrame(Duration.millis(500), e -> resultLabel.setOpacity(0.0)),
                new KeyFrame(Duration.millis(1000), e -> resultLabel.setOpacity(1.0))
        );
        timeline.setCycleCount(2);
        timeline.play();
    }

    private void showMessageBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
