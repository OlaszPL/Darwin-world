package agh.ics.darwin.presenter;

import agh.ics.darwin.StartWindow;
import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class PresetCreator {

    public static void savePreset(StartWindow startWindow, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Preset");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/presets"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Parameter,Value");
                writer.println("Width," + startWindow.widthSpinner.getValue());
                writer.println("Height," + startWindow.heightSpinner.getValue());
                writer.println("StartAnimalsNum," + startWindow.startAnimalsNum.getValue());
                writer.println("GenomeLength," + startWindow.genomeLength.getValue());
                writer.println("MinMutationsNum," + startWindow.minMutationsNum.getValue());
                writer.println("MaxMutationsNum," + startWindow.maxMutationsNum.getValue());
                writer.println("BehaviourType," + startWindow.behaviourType.getValue().name());
                writer.println("StartPlantsNum," + startWindow.startPlantsNum.getValue());
                writer.println("DailyPlantsNum," + startWindow.dailyPlantsNum.getValue());
                writer.println("PlantGeneratorType," + startWindow.plantGeneratorType.getValue().name());
                writer.println("InitialAnimalEnergy," + startWindow.initialAnimalEnergy.getValue());
                writer.println("MinReproduceEnergy," + startWindow.minReproduceEnergy.getValue());
                writer.println("EnergyGivenToChild," + startWindow.energyGivenToChild.getValue());
                writer.println("MoveEnergy," + startWindow.moveEnergy.getValue());
                writer.println("OnePlantEnergy," + startWindow.onePlantEnergy.getValue());
                writer.println("Interval," + startWindow.interval.getValue());
                writer.println("CsvSave," + startWindow.csvSave.isSelected());
                writer.println("CsvFileName," + startWindow.csvFileName.getText());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void loadPreset(StartWindow startWindow, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Preset");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/presets"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String parameter = parts[0];
                        String value = parts[1];
                        switch (parameter) {
                            case "Width" -> startWindow.widthSpinner.getValueFactory().setValue(Integer.parseInt(value));
                            case "Height" -> startWindow.heightSpinner.getValueFactory().setValue(Integer.parseInt(value));
                            case "StartAnimalsNum" -> startWindow.startAnimalsNum.getValueFactory().setValue(Integer.parseInt(value));
                            case "GenomeLength" -> startWindow.genomeLength.getValueFactory().setValue(Integer.parseInt(value));
                            case "MinMutationsNum" -> startWindow.minMutationsNum.getValueFactory().setValue(Integer.parseInt(value));
                            case "MaxMutationsNum" -> startWindow.maxMutationsNum.getValueFactory().setValue(Integer.parseInt(value));
                            case "BehaviourType" -> startWindow.behaviourType.setValue(BehaviourType.valueOf(value));
                            case "StartPlantsNum" -> startWindow.startPlantsNum.getValueFactory().setValue(Integer.parseInt(value));
                            case "DailyPlantsNum" -> startWindow.dailyPlantsNum.getValueFactory().setValue(Integer.parseInt(value));
                            case "PlantGeneratorType" -> startWindow.plantGeneratorType.setValue(PlantGeneratorType.valueOf(value));
                            case "InitialAnimalEnergy" -> startWindow.initialAnimalEnergy.getValueFactory().setValue(Integer.parseInt(value));
                            case "MinReproduceEnergy" -> startWindow.minReproduceEnergy.getValueFactory().setValue(Integer.parseInt(value));
                            case "EnergyGivenToChild" -> startWindow.energyGivenToChild.getValueFactory().setValue(Integer.parseInt(value));
                            case "MoveEnergy" -> startWindow.moveEnergy.getValueFactory().setValue(Integer.parseInt(value));
                            case "OnePlantEnergy" -> startWindow.onePlantEnergy.getValueFactory().setValue(Integer.parseInt(value));
                            case "Interval" -> startWindow.interval.getValueFactory().setValue(Integer.parseInt(value));
                            case "CsvSave" -> startWindow.csvSave.setSelected(Boolean.parseBoolean(value));
                            case "CsvFileName" -> startWindow.csvFileName.setText(value);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}