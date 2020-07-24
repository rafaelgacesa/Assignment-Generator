import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label directoryLabel = new Label("Save Directory: ");
        directoryLabel.setFont(new Font("Arial", 14));
        directoryLabel.setPadding(new Insets(5, 0, 0, 0));

        TextField directoryField = new TextField();
        directoryField.setPrefColumnCount(30);
        String p;
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            p = Generator.getStringPath("Assignments\\");
        else
            p = Generator.getStringPath("Assignments/");
        directoryField.setText(p);

        Button directoryButton = new Button("Browse");

        HBox directoryBox = new HBox(directoryLabel, directoryField, directoryButton);
        directoryBox.setSpacing(7.5); // spacing between objects
        directoryBox.setPadding(new Insets(10,10,10,10));
        directoryBox.setAlignment(Pos.TOP_CENTER);

        Button generateButton = new Button("Generate");
        generateButton.setDefaultButton(true);

        HBox generateBox = new HBox(generateButton);
        generateBox.setSpacing(7.5); // spacing between objects
        generateBox.setPadding(new Insets(10,10,10,10));
        generateBox.setAlignment(Pos.TOP_CENTER);

        VBox overall = new VBox(directoryBox, generateBox);

        directoryButton.setOnAction(actionEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (System.getProperty("os.name").toLowerCase().contains("win"))
                directoryField.setText(selectedDirectory.getAbsolutePath() + "\\");
            else
                directoryField.setText(selectedDirectory.getAbsolutePath() + "/");
        });

        generateButton.setOnAction(actionEvent -> {
            try {
                Generator.Generate(directoryField.getText());
            } catch (IOException | InvalidFormatException ignored){}

            try {
                Desktop.getDesktop().open(new File(directoryField.getText()));
            } catch (IOException ignored) {}

            primaryStage.close();
        });

        Scene scene = new Scene(overall, 600, 100);
        primaryStage.setTitle("Assignment Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
