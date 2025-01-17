package agh.ics.darwin;

import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import agh.ics.darwin.presenter.PresetCreator;
import atlantafx.base.theme.CupertinoLight;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class StartWindow extends Application {
    @FXML
    public Spinner<Integer> startAnimalsNum, genomeLength, minMutationsNum, maxMutationsNum, widthSpinner, heightSpinner,
    startPlantsNum, dailyPlantsNum, initialAnimalEnergy, minReproduceEnergy, energyGivenToChild, moveEnergy, onePlantEnergy,
    interval;
    @FXML
    public ComboBox<BehaviourType> behaviourType;
    @FXML
    public ComboBox<PlantGeneratorType> plantGeneratorType;
    @FXML
    public CheckBox csvSave;
    @FXML
    public TextField csvFileName;
    @FXML
    public Button browseButton;

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configure simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startwindow.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        // preferencje wartości do ustawienia
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));

        startAnimalsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 10));
        genomeLength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 10));
        behaviourType.getItems().setAll(BehaviourType.values());
        behaviourType.setValue(BehaviourType.FULL_PREDESTINATION_BEHAVIOUR);
        minMutationsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        maxMutationsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));

        startPlantsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        dailyPlantsNum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        plantGeneratorType.getItems().setAll(PlantGeneratorType.values());
        plantGeneratorType.setValue(PlantGeneratorType.EQUATORIAL_FOREST);

        initialAnimalEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        minReproduceEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        energyGivenToChild.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        moveEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        onePlantEnergy.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));

        interval.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 10));
        csvSave.setSelected(false);
        csvFileName.setDisable(true);
        browseButton.setDisable(true);
        csvFileName.setText("stats.csv");

        csvSave.selectedProperty().addListener((observable, oldValue, newValue) -> {
            csvFileName.setDisable(!newValue);
            browseButton.setDisable(!newValue);
        });
    }

    @FXML
    public void browseCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(csvFileName.getScene().getWindow());
        if (file != null) {
            csvFileName.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void savePreset() {
        PresetCreator.savePreset(this, (Stage) widthSpinner.getScene().getWindow());
    }

    @FXML
    public void loadPreset() {
        PresetCreator.loadPreset(this, (Stage) widthSpinner.getScene().getWindow());
    }

    @FXML
    public void newSimulation() {
        SimulationApp simulationApp = new SimulationApp();
        try {
            simulationApp.start(new Stage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}