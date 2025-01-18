package agh.ics.darwin.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class WorldElementBox extends VBox {
    private static final Map<String, Image> imageCache = new HashMap<>();

    private static Image loadImage(String resourceName) {
        return imageCache.computeIfAbsent(resourceName, Image::new);
    }

    public WorldElementBox(WorldElement element) {
        Image image = loadImage(element.getResourceName());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);
        this.getChildren().add(imageView);
        this.setAlignment(Pos.CENTER);
    }
}