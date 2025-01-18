package agh.ics.darwin.presenter;

import agh.ics.darwin.StartWindow;
import agh.ics.darwin.model.util.PresetsWriter;
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
            PresetsWriter.writeParamsToFile(file, startWindow);
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
                reader.lines()
                        .map(line -> line.split(","))
                        .filter(paramRecord -> paramRecord.length == 2)
                        .forEach(paramRecord -> PresetsWriter.writeParamToWindowField(paramRecord, startWindow));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}