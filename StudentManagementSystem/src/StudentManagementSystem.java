import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem extends Application {
    private List<Student> students;
    private String dataFile;

    private TextField nameField;
    private TextField rollNumberField;
    private TextField gradeField;
    private ListView<String> studentsListView;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        this.dataFile = "students.txt";
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
        updateListView();
    }

    public void removeStudent(String rollNumber) {
        students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        saveStudents();
        updateListView();
    }

    public Student searchStudent(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    private void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (Student student : students) {
                writer.println(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String rollNumber = parts[1];
                    String grade = parts[2];
                    students.add(new Student(name, rollNumber, grade));
                }
            }
        } catch (IOException e) {
            // If the file is not found or an error occurs while reading, it means no data exists yet.
            // We can simply ignore this exception in this case.
        }
    }

    private void updateListView() {
        studentsListView.getItems().clear();
        for (Student student : students) {
            studentsListView.getItems().add(student.toString());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        // UI elements
        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label rollNumberLabel = new Label("Roll Number:");
        rollNumberField = new TextField();

        Label gradeLabel = new Label("Grade:");
        gradeField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String rollNumber = rollNumberField.getText();
            String grade = gradeField.getText();
            if (!name.isEmpty() && !rollNumber.isEmpty() && !grade.isEmpty()) {
                Student student = new Student(name, rollNumber, grade);
                addStudent(student);
            } else {
                showAlert("Error", "All fields are required!");
            }
            clearFields();
        });

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            String rollNumber = rollNumberField.getText();
            removeStudent(rollNumber);
            clearFields();
        });

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String rollNumber = rollNumberField.getText();
            Student student = searchStudent(rollNumber);
            if (student != null) {
                showAlert("Student Found", student.toString());
            } else {
                showAlert("Student Not Found", "No student with the given roll number found.");
            }
            clearFields();
        });

        studentsListView = new ListView<>();
        updateListView();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameField, 1, 0);
        GridPane.setConstraints(rollNumberLabel, 0, 1);
        GridPane.setConstraints(rollNumberField, 1, 1);
        GridPane.setConstraints(gradeLabel, 0, 2);
        GridPane.setConstraints(gradeField, 1, 2);
        GridPane.setConstraints(addButton, 0, 3);
        GridPane.setConstraints(removeButton, 1, 3);
        GridPane.setConstraints(searchButton, 2, 3);

        gridPane.getChildren().addAll(
                nameLabel, nameField, rollNumberLabel, rollNumberField, gradeLabel, gradeField,
                addButton, removeButton, searchButton
        );

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(gridPane, studentsListView);

        Scene scene = new Scene(vBox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearFields() {
        nameField.clear();
        rollNumberField.clear();
        gradeField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
