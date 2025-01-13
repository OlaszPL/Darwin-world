package agh.ics.darwin;

import agh.ics.darwin.model.animal.BehaviourType;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartWindow extends Application {
    public Spinner<Integer> startAnimalsNum;
    public Spinner<Integer> genomeLength;
    public ComboBox<BehaviourType> behaviourType;
    @FXML
    private Spinner<Integer> widthSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configure simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startwindow.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        startAnimalsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        genomeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        behaviourType.getItems().setAll(BehaviourType.values());
    }
}