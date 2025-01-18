package agh.ics.darwin.model.util;

import agh.ics.darwin.StartWindow;
import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

public class PresetsWriter {

    public static void writeParamsToFile(File file, StartWindow startWindow) {
        try (PrintWriter writer = new PrintWriter(file)) {
            Stream.of(
                    "Parameter,Value",
                    "Width," + startWindow.widthSpinner.getValue().toString(),
                    "Height," + startWindow.heightSpinner.getValue().toString(),
                    "StartAnimalsNum," + startWindow.startAnimalsNum.getValue().toString(),
                    "GenomeLength," + startWindow.genomeLength.getValue().toString(),
                    "MinMutationsNum," + startWindow.minMutationsNum.getValue().toString(),
                    "MaxMutationsNum," + startWindow.maxMutationsNum.getValue().toString(),
                    "BehaviourType," + startWindow.behaviourType.getValue().name(),
                    "StartPlantsNum," + startWindow.startPlantsNum.getValue().toString(),
                    "DailyPlantsNum," + startWindow.dailyPlantsNum.getValue().toString(),
                    "PlantGeneratorType," + startWindow.plantGeneratorType.getValue().name(),
                    "InitialAnimalEnergy," + startWindow.initialAnimalEnergy.getValue().toString(),
                    "MinReproduceEnergy," + startWindow.minReproduceEnergy.getValue().toString(),
                    "EnergyGivenToChild," + startWindow.energyGivenToChild.getValue().toString(),
                    "MoveEnergy," + startWindow.moveEnergy.getValue().toString(),
                    "OnePlantEnergy," + startWindow.onePlantEnergy.getValue().toString(),
                    "Interval," + startWindow.interval.getValue().toString(),
                    "CsvSave," + startWindow.csvSave.isSelected(),
                    "CsvFileName," + startWindow.csvFileName.getText()
            ).forEach(writer::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeParamToWindowField(String[] paramRecord, StartWindow startWindow){
        String paramName = paramRecord[0];
        String value = paramRecord[1];
        switch (paramName) {
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
