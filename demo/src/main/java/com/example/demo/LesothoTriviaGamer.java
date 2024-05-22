package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LesothoTriviaGamer extends Application {

    // Trivia questions, options, and answers
    private final String[] questions = {
            "Which of the following is Lesotho's official language?",
            "What is the highest peak in Lesotho?",
            "What is the traditional Basotho blanket called?",
            "Which river forms the border between Lesotho and South Africa?",
            "Identify this famous landmark in Lesotho:"
    };

    private final String[][] options = {
            {"A. English", "B. Sesotho", "C. French", "D. Afrikaans"},
            {"A. Thabana Ntlenyana", "B. Mount Qiloane", "C. Mount Afadang", "D. Black Mountain"},
            {"A. Kobo", "B. Seshoeshoe", "C. Koboletsi", "D. Mokorotlo"},
            {"A. Orange River", "B. Caledon River", "C. Tugela River", "D. Vaal River"},
            {"A. Thaba Bosiu", "B. Sani Pass", "C. Maletsunyane Falls", "D. Katse Dam"}
    };

    private final String[] answers = {
            "B. Sesotho",
            "A. Thabana Ntlenyana",
            "A. Kobo",
            "B. Caledon River",
            "C. Maletsunyane Falls"
    };

    private final String[] imageUrls = {
            "sesotho.jpg",
            "thabanantlenyana.jpg",
            "kobo.jpg",
            "river.jpg",
            "maletsunyane.jpg"
    };

    private int currentQuestionIndex = 0;
    private int score = 0;
    private Label questionLabel;
    private RadioButton[] optionButtons;
    private ToggleGroup optionsGroup;
    private Label feedbackLabel;
    private ImageView imageView;
    private Timeline timer;
    private int secondsLeft = 30;
    private Label timerLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lesotho Trivia Game");

        // Layout
        BorderPane root = new BorderPane();

        // Question label
        questionLabel = new Label();
        questionLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-font-weight: bold;");

        // Options
        optionsGroup = new ToggleGroup();
        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER_LEFT);

        optionButtons = new RadioButton[4];
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new RadioButton();
            optionButtons[i].setToggleGroup(optionsGroup);
            optionsBox.getChildren().add(optionButtons[i]);
        }

        // Timer label
        timerLabel = new Label("Time left: 30s");
        timerLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> submitAnswer());
        submitButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px 20px;"
        );

        // Feedback label
        feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        // Image
        imageView = new ImageView();
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(questionLabel, imageView, optionsBox, timerLabel, submitButton, feedbackLabel);

        root.setCenter(mainBox);

        // Apply internal CSS styling
        root.setStyle(
                "-fx-background-color: #ADD8E6;" + // Set background color to light blue
                        "-fx-padding: 20px;"
        );

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize and start the timer
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft--;
            timerLabel.setText("Time left: " + secondsLeft + "s");
            if (secondsLeft == 0) {
                timer.stop();
                submitAnswer();
            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);

        // Initialize the first question
        loadQuestion(currentQuestionIndex);
    }

    private void loadQuestion(int index) {
        if (index < questions.length) {
            questionLabel.setText(questions[index]);
            String[] choices = options[index];
            for (int i = 0; i < choices.length; i++) {
                optionButtons[i].setText(choices[i]);
            }
            feedbackLabel.setText("");
            optionsGroup.selectToggle(null); // Clear selection

            // Display image if available
            if (imageUrls[index] != null) {
                imageView.setImage(new Image(imageUrls[index]));
                imageView.setVisible(true);
            } else {
                imageView.setVisible(false);
            }

            // Reset timer
            secondsLeft = 30;
            timerLabel.setText("Time left: " + secondsLeft + "s");
            timer.playFromStart();
        }
    }

    private void submitAnswer() {
        // Stop timer
        timer.stop();

        RadioButton selectedButton = (RadioButton) optionsGroup.getSelectedToggle();
        if (selectedButton == null) {
            feedbackLabel.setText("Please select an answer.");
            return;
        }

        String selectedAnswer = selectedButton.getText();
        String correctAnswer = answers[currentQuestionIndex];
        if (selectedAnswer.equals(correctAnswer)) {
            feedbackLabel.setText("Correct!");
            score++;
        } else {
            feedbackLabel.setText("Wrong. The correct answer is: " + correctAnswer);
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            loadQuestion(currentQuestionIndex);
        } else {
            questionLabel.setText("Quiz completed! Your score: " + score + "/" + questions.length);
            for (RadioButton optionButton : optionButtons) {
                optionButton.setDisable(true);
            }
        }
    }
}
