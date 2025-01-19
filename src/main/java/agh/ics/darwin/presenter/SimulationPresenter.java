package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.*;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.parameters.*;
import agh.ics.darwin.stats.StatsRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class SimulationPresenter implements MapChangeListener {

    private static final int GRID_HEIGHT_WIDTH = 385;
    private int cellSize;

    @FXML
    public Button toggleButton;
    @FXML
    public Button showDominantGenesButton;
    @FXML
    public Button showPreferredFields;
    @FXML
    public Label genomeLabel;
    @FXML
    public Label activeGeneLabel;
    @FXML
    public Label energyLabel;
    @FXML
    public Label eatenPlantsLabel;
    @FXML
    public Label childrenLabel;
    @FXML
    public Label descendantsLabel;
    @FXML
    public Label ageLabel;
    @FXML
    public Label dayOfDeathLabel;
    @FXML
    private VBox selectedElement;
    @FXML
    public Label descriptionLabel;
    @FXML
    public GridPane mapGrid;
    @FXML
    public TextArea popularGenotypesLabel;
    @FXML
    public VBox statsBox;
    @FXML
    public LineChart<Number, Number> animalsChart;
    @FXML
    public LineChart<Number, Number> energyChart;

    private Simulation simulation;
    private SimulationParameters simulationParameters;
    private boolean highlightedGenes = false, highlightedFields = false;
    private Set<Animal> highlightedAnimals;
    private Set<Vector2d> highlightedPositions;

    private final ChartUpdater chartUpdater = new ChartUpdater();
    private final AnimalInfoUpdater animalInfoUpdater = new AnimalInfoUpdater();

    public void setSimulationParameters(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
    }

    @FXML
    public void initialize() {
        setupWindowHandling();
        chartUpdater.initializeCharts(animalsChart, energyChart);
        setupButtons();
    }

    private void setupButtons() {
        showDominantGenesButton.setDisable(true);
        showPreferredFields.setDisable(true);
    }

    private void setupWindowHandling() {
        Platform.runLater(() -> {
            Stage stage = (Stage) mapGrid.getScene().getWindow();
            onSimulationStartClicked();
            stage.setOnCloseRequest(event -> {
                if (simulation != null) simulation.stop();
            });
        });
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void drawMap(WorldMap map){
        clearGrid();
        Boundary boundary = map.getCurrentBounds();
        int left = boundary.lowerLeft().getX();
        int right = boundary.upperRight().getX();
        int bottom = boundary.lowerLeft().getY();
        int top = boundary.upperRight().getY();
        drawAxes(left, right, bottom, top);
        drawMapElements(map, left, right, bottom, top);
        for (Node label : mapGrid.getChildren()){
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
    }

    private static void setElementSize(Node element, int size) {
        element.setStyle(String.format("-fx-min-width: %s; -fx-min-height: %s; -fx-max-width: %s; -fx-max-height: %s;",
                size, size, size, size));
    }

    private void drawMapElements(WorldMap map, int left, int right, int bottom, int top) {
        for (int x = left; x <= right; x++) {
            for (int y = bottom; y <= top; y++) {
                Vector2d position = new Vector2d(x, y);

                if (highlightedFields && highlightedPositions.contains(position)){
                    VBox vBox = new VBox();
                    vBox.getStyleClass().add("preferred-field");
                    setElementSize(vBox, cellSize - 2);

                    mapGrid.add(vBox, position.getX() - left + 1, top - position.getY() + 1);
                }

                if (map.isOccupied(position)) {
                    WorldElement element = map.objectAt(position);
                    WorldElementBox box = new WorldElementBox(element, simulationParameters.energyParameters().moveEnergy(), cellSize);

                    if (element instanceof Animal){
                        if (highlightedGenes && highlightedAnimals.contains((Animal) element)){
                            box.getStyleClass().add("dominant-animal");
                            setElementSize(box, cellSize - 2);
                        }
                        box.setOnMouseClicked(event -> handleElementClick(box));

                        if (selectedElement != null && ((WorldElementBox) selectedElement).getElement() == element){
                            box.getStyleClass().add("selected-element");
                            setElementSize(box, cellSize);
                            selectedElement = box;
                        }
                    }

                    mapGrid.add(box, position.getX() - left + 1, top - position.getY() + 1);
                }
            }
        }
    }

    private void drawAxes(int left, int right, int bottom, int top) {
        // scale grid panes
        cellSize = Math.max(Math.min((GRID_HEIGHT_WIDTH / (right - left + 2)), (GRID_HEIGHT_WIDTH / (top - bottom + 2))), 1);
        double fontSize = cellSize * 0.4;

        Label cornerLabel = new Label("y\\x");
        cornerLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
        mapGrid.add(cornerLabel, 0, 0);
        mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        mapGrid.getRowConstraints().add(new RowConstraints(cellSize));

        for (int x = left; x <= right; x++) {
            Label xLabel = new Label("%d".formatted(x));
            xLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
            mapGrid.add(xLabel, x - left + 1, 0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        for (int y = bottom; y <= top; y++) {
            Label yLabel = new Label("%d".formatted(y));
            yLabel.setStyle(String.format("-fx-font-size: %.2f;", fontSize));
            mapGrid.add(yLabel, 0, top - y + 1);
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap(map);
            animalInfoUpdater.updateSelectedAnimalInfo(selectedElement, simulation, genomeLabel, activeGeneLabel, energyLabel, eatenPlantsLabel, childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);
            descriptionLabel.setText(message);
            simulation.countDown(); // fix JavaFX being too slow for simulation interval
        });
    }

    public void onSimulationStartClicked() {
        if (simulation != null) {
            simulation.stop();
        }
        simulation = new Simulation(simulationParameters);
        simulation.registerObserver(this);
        new Thread(simulation).start();
    }

    public void updateStats(StatsRecord statsRecord) {
        Platform.runLater(() ->
                chartUpdater.updateCharts(statsRecord, popularGenotypesLabel));
    }

    @FXML
    public void onToggleClicked() {
        if (simulation.isPaused()) {
            resumeSimulation();
        } else {
            pauseSimulation();
        }
    }

    private void resumeSimulation() {
        simulation.continueSimulation();
        toggleButton.setText("⏸ Pause");
        showDominantGenesButton.setDisable(true);
        showPreferredFields.setDisable(true);
        highlightedGenes = false;
        highlightedFields = false;
    }

    private void pauseSimulation() {
        simulation.pause();
        toggleButton.setText("▶ Play");
        showDominantGenesButton.setDisable(false);
        showPreferredFields.setDisable(false);
    }

    @FXML
    public void onShowDominantGenesClicked() {
        if (!highlightedGenes) {
            highlightedAnimals = new HashSet<>(simulation.getDominantGenotypeAnimals());
        }
        highlightedGenes = !highlightedGenes;
        drawMap(simulation.getMap());
    }

    @FXML
    public void onShowPreferredFieldsClicked() {
        if (!highlightedFields){
            highlightedPositions = new HashSet<>(simulation.getPreferredFields());
        }
        highlightedFields = !highlightedFields;
        drawMap(simulation.getMap());
    }

    private void handleElementClick(VBox element) {
        if (!simulation.isPaused()) return;

        if (selectedElement != null) {
            selectedElement.getStyleClass().remove("selected-element");
            animalInfoUpdater.cleanSelectedAnimalInfo(genomeLabel, activeGeneLabel, energyLabel,
                    eatenPlantsLabel, childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);

            if (selectedElement == element) {
                selectedElement = null;
                return;
            }
        }
        selectedElement = element;
        selectedElement.getStyleClass().add("selected-element");
        setElementSize(selectedElement, cellSize);
        animalInfoUpdater.updateSelectedAnimalInfo(selectedElement, simulation,
                genomeLabel, activeGeneLabel, energyLabel, eatenPlantsLabel,
                childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);
    }
}