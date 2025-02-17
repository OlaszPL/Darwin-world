package agh.ics.darwin.presenter;

import agh.ics.darwin.model.*;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.util.Boundary;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Set;
import java.util.function.Consumer;

public class MapDrawer {
    public int cellSize;
    private VBox currentSelectedNode;

    public void drawMap(WorldMap map, GridPane mapGrid,
                        boolean highlightedGenes, boolean highlightedFields,
                        Set<Animal> highlightedAnimals, Set<Vector2d> highlightedPositions,
                        VBox selectedElement, Consumer<VBox> elementClickHandler,
                        int moveEnergy, int gridHeightWidth) {
        currentSelectedNode = selectedElement;
        clearGrid(mapGrid);
        Boundary boundary = map.getCurrentBounds();
        drawAxes(mapGrid, boundary, gridHeightWidth);
        drawMapElements(map, mapGrid, boundary, cellSize, highlightedGenes, highlightedFields,
                highlightedAnimals, highlightedPositions, selectedElement, elementClickHandler,
                moveEnergy);
        alignGridElements(mapGrid);
    }

    private void clearGrid(GridPane mapGrid) {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void setElementSize(Node element, int size) {
        element.setStyle(String.format(
                "-fx-min-width: %s; -fx-min-height: %s; -fx-max-width: %s; -fx-max-height: %s;",
                size, size, size, size));
    }

    private void drawAxes(GridPane mapGrid, Boundary boundary, int gridHeightWidth) {
        int left = boundary.lowerLeft().getX();
        int right = boundary.upperRight().getX();
        int bottom = boundary.lowerLeft().getY();
        int top = boundary.upperRight().getY();

        cellSize = Math.max(Math.min((gridHeightWidth / (right - left + 2)), (gridHeightWidth / (top - bottom + 2))), 1);
        double fontSize = cellSize * 0.4;

        addCornerLabel(mapGrid, cellSize, fontSize);
        addColumnLabels(mapGrid, left, right, cellSize, fontSize);
        addRowLabels(mapGrid, bottom, top, cellSize, fontSize);
    }

    private void addCornerLabel(GridPane mapGrid, int cellSize, double fontSize) {
        Label cornerLabel = new Label("y\\x");
        cornerLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
        mapGrid.add(cornerLabel, 0, 0);
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
    }

    private void drawMapElements(WorldMap map, GridPane mapGrid, Boundary boundary,
                                 int cellSize, boolean highlightedGenes, boolean highlightedFields,
                                 Set<Animal> highlightedAnimals, Set<Vector2d> highlightedPositions,
                                 VBox selectedElement, Consumer<VBox> elementClickHandler,
                                 int moveEnergy) {
        int left = boundary.lowerLeft().getX();
        int top = boundary.upperRight().getY();

        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                drawMapElement(map, mapGrid, position, left, top, cellSize,
                        highlightedGenes, highlightedFields, highlightedAnimals,
                        highlightedPositions, selectedElement, elementClickHandler, moveEnergy);
            }
        }
    }

    private void drawMapElement(WorldMap map, GridPane mapGrid, Vector2d position,
                                int left, int top, int cellSize,
                                boolean highlightedGenes, boolean highlightedFields,
                                Set<Animal> highlightedAnimals, Set<Vector2d> highlightedPositions,
                                VBox selectedElement, Consumer<VBox> elementClickHandler,
                                int moveEnergy) {
        if (highlightedFields && highlightedPositions.contains(position)) {
            VBox vBox = new VBox();
            vBox.getStyleClass().add("preferred-field");
            setElementSize(vBox, cellSize - 2);
            mapGrid.add(vBox, position.getX() - left + 1, top - position.getY() + 1);
        }

        if (map.objectAt(position).isPresent()){
            addWorldElement(map, mapGrid, position, left, top, cellSize,
                    highlightedGenes, highlightedAnimals, selectedElement,
                    elementClickHandler, moveEnergy);
        }
    }

    private void addWorldElement(WorldMap map, GridPane mapGrid, Vector2d position,
                                 int left, int top, int cellSize,
                                 boolean highlightedGenes, Set<Animal> highlightedAnimals,
                                 VBox selectedElement, Consumer<VBox> elementClickHandler,
                                 int moveEnergy) {

        if (map.objectAt(position).isEmpty()) return;
        WorldElement element = map.objectAt(position).get();
        WorldElementBox box = new WorldElementBox(element,
                moveEnergy, cellSize);

        if (element instanceof Animal) {
            configureAnimalBox(box, (Animal) element, cellSize, highlightedGenes,
                    highlightedAnimals, selectedElement, elementClickHandler);
        }

        mapGrid.add(box, position.getX() - left + 1, top - position.getY() + 1);
    }

    private void configureAnimalBox(WorldElementBox box, Animal animal, int cellSize,
                                    boolean highlightedGenes, Set<Animal> highlightedAnimals,
                                    VBox selectedElement, Consumer<VBox> elementClickHandler) {
        if (highlightedGenes && highlightedAnimals.contains(animal)) {
            box.getStyleClass().add("dominant-animal");
            setElementSize(box, cellSize - 2);
        }

        box.setOnMouseClicked(event -> elementClickHandler.accept(box));

        if (selectedElement != null &&
                ((WorldElementBox) selectedElement).getElement() == animal) {
            box.getStyleClass().add("selected-element");
            currentSelectedNode = box;
            setElementSize(box, cellSize);
        }
    }

    private void alignGridElements(GridPane mapGrid) {
        for (Node label : mapGrid.getChildren()) {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
    }

    private void addColumnLabels(GridPane mapGrid, int left, int right,
                                 int cellSize, double fontSize) {
        for (int x = left; x <= right; x++) {
            Label xLabel = new Label(String.valueOf(x));
            xLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
            mapGrid.add(xLabel, x - left + 1, 0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }
    }

    private void addRowLabels(GridPane mapGrid, int bottom, int top,
                              int cellSize, double fontSize) {
        for (int y = bottom; y <= top; y++) {
            Label yLabel = new Label(String.valueOf(y));
            yLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
            mapGrid.add(yLabel, 0, top - y + 1);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }

    public VBox getCurrentSelectedNode(){
        return currentSelectedNode;
    }
}
