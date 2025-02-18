package agh.ics.darwin.presenter;

import agh.ics.darwin.StartWindow;
import agh.ics.darwin.model.util.PresetsWriter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class PresetCreator {
    private static final String PRESETS_DIR = System.getProperty("user.home") + "/.darwin-world/presets/";
    private static final String DEFAULT_PRESETS_DIR = "/presets/";

    public static void savePreset(StartWindow startWindow, Stage stage) {
        createPresetsDirectoryIfNeeded();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Preset");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(PRESETS_DIR));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            PresetsWriter.writeParamsToFile(file, startWindow);
        }
    }

    public static void loadPreset(StartWindow startWindow, Stage stage) {
        createPresetsDirectoryIfNeeded();
        copyDefaultPresetsIfNeeded();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Preset");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File(PRESETS_DIR));
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

    private static void createPresetsDirectoryIfNeeded() {
        File dir = new File(PRESETS_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Cannot initialize presets folder: " + PRESETS_DIR);
            }
        }
    }

    private static void copyDefaultPresetsIfNeeded() {
        try {
            File presetsDir = new File(PRESETS_DIR);
            if (presetsDir.listFiles() == null || Objects.requireNonNull(presetsDir.listFiles()).length == 0) {
                copyPresetsFromJar();
            }
        } catch (IOException e) {
            System.out.println("Default presets initialization error: " + e.getMessage());
        }
    }

    private static void copyPresetsFromJar() throws IOException {
        try (InputStream listStream = PresetCreator.class.getResourceAsStream(DEFAULT_PRESETS_DIR + "list.txt")) {
            if (listStream == null) {
                System.out.println("Lack of list.txt file in resources: " + DEFAULT_PRESETS_DIR);
                return;
            }
            try (BufferedReader listReader = new BufferedReader(new InputStreamReader(listStream))) {
                String presetFileName;
                while ((presetFileName = listReader.readLine()) != null) {
                    presetFileName = presetFileName.trim();
                    if (presetFileName.isEmpty()) {
                        continue;
                    }
                    try (InputStream presetStream = PresetCreator.class.getResourceAsStream(DEFAULT_PRESETS_DIR + presetFileName)) {
                        if (presetStream != null) {
                            Files.copy(presetStream, Paths.get(PRESETS_DIR, presetFileName), StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            System.out.println("Cannot copy preset file: " + DEFAULT_PRESETS_DIR + presetFileName);
                        }
                    }
                }
            }
        }
    }

}