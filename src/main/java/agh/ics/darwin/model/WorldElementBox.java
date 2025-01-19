package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.Animal;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.util.HashMap;
import java.util.Map;

public class WorldElementBox extends VBox {
    private static final Map<String, Image> imageCache = new HashMap<>();
    private ProgressBar energyBar;
    private final WorldElement element;

    private static Image loadImage(String resourceName) {
        return imageCache.computeIfAbsent(resourceName, Image::new);
    }

    public WorldElementBox(WorldElement element, int moveEnergy, int cellSize) {
        this.element = element;
        Image image = loadImage(element.getResourceName());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(cellSize);
        imageView.setFitWidth(cellSize);
        imageView.setPreserveRatio(true);
        this.getChildren().add(imageView);
        this.setAlignment(Pos.CENTER);

        if (element instanceof Animal) {
            energyBar = new ProgressBar();
            energyBar.getStyleClass().clear();
            energyBar.setPrefWidth(cellSize - 2);
            double barSize = cellSize * 0.05;
            energyBar.setMaxHeight(barSize);
            energyBar.setMinHeight(barSize);

            VBox.setVgrow(energyBar, Priority.NEVER);
            VBox.setMargin(energyBar, new Insets(0, 0, cellSize * 0.12, 0));

            this.getChildren().add(energyBar);
            this.setAlignment(Pos.CENTER);
            this.setPrefSize(cellSize, cellSize);

            updateEnergyBar((Animal) element, moveEnergy);
        }
    }

    private double getEnergyRatio(Animal animal, int moveEnergy) {
        int maxEnergy = 10 * moveEnergy;
        return Math.min(1.0 * animal.getEnergy() / maxEnergy, 1);
    }

    private void updateEnergyBar(Animal animal, int moveEnergy) {
        double energyRatio = getEnergyRatio(animal, moveEnergy);
        energyBar.setProgress(Math.max(0, energyRatio));

        if (energyRatio > 0.5) {
            energyBar.getStyleClass().add("energy-bar-high");
        } else if (energyRatio > 0.2) {
            energyBar.getStyleClass().add("energy-bar-medium");
        } else {
            energyBar.getStyleClass().add("energy-bar-low");
        }
    }

    public WorldElement getElement(){
        return element;
    }
}