package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.*;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.parameters.*;
import agh.ics.darwin.stats.SelectedAnimalStats;
import agh.ics.darwin.stats.StatsRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SimulationPresenter implements MapChangeListener {
    private static final int GRID_HEIGHT_WIDTH = 385;
    private int cellSize;
    public Button toggleButton;
    public Button showDominantGenesButton;
    public Button showPreferredFields;
    public Label genomeLabel;
    public Label activeGeneLabel;
    public Label energyLabel;
    public Label eatenPlantsLabel;
    public Label childrenLabel;
    public Label descendantsLabel;
    public Label ageLabel;
    public Label dayOfDeathLabel;
    private SimulationParameters simulationParameters;
    private boolean highlightedGenes = false, highlightedFields = false;
    private Set<Animal> highlightedAnimals;
    private Set<Vector2d> highlightedPositions;
    private VBox selectedElement;
    @FXML
    public Label descriptionLabel;
    @FXML
    public GridPane mapGrid;
    public TextArea popularGenotypesLabel;
    public VBox statsBox;
    public LineChart<Number, Number> animalsChart;
    public LineChart<Number, Number> energyChart;
    private Simulation simulation;
    private final XYChart.Series<Number, Number> animalsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> plantsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> freeFieldsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageEnergySeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageLifeSpan = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageChildrenNumber = new XYChart.Series<>();

    public void setSimulationParameters(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) mapGrid.getScene().getWindow();
            onSimulationStartClicked();
            stage.setOnCloseRequest(this::handleWindowClosing);
        });
        showDominantGenesButton.setDisable(true);
        showPreferredFields.setDisable(true);
        animalsChart.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/chart-styles.css")).toExternalForm());
        energyChart.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/chart-styles.css")).toExternalForm());
        animalsChart.getData().add(animalsSeries);
        animalsSeries.setName("Animals");
        animalsChart.getData().add(plantsSeries);
        plantsSeries.setName("Plants");
        animalsChart.getData().add(freeFieldsSeries);
        freeFieldsSeries.setName("Free Fields");
        energyChart.getData().add(averageEnergySeries);
        averageEnergySeries.setName("Energy");
        energyChart.getData().add(averageLifeSpan);
        averageLifeSpan.setName("Lifespan");
        energyChart.getData().add(averageChildrenNumber);
        averageChildrenNumber.setName("Children");
        animalsChart.setCreateSymbols(false);
        energyChart.setCreateSymbols(false);
    }

    private void handleWindowClosing(WindowEvent event) {
        if (simulation != null) {
            simulation.stop();
        }
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

                map.objectAt(position).ifPresent(element -> {
                    WorldElementBox box = new WorldElementBox(element, simulationParameters.energyParameters().moveEnergy(), cellSize);

                    if (element instanceof Animal) {
                        if (highlightedGenes && highlightedAnimals.contains((Animal) element)) {
                            box.getStyleClass().add("dominant-animal");
                            setElementSize(box, cellSize - 2);
                        }
                        box.setOnMouseClicked(event -> handleElementClick(box));
                    }

                    if (selectedElement != null && ((WorldElementBox) selectedElement).getElement() == element) {
                        box.getStyleClass().add("selected-element");
                        selectedElement = box;
                    }

                    mapGrid.add(box, position.getX() - left + 1, top - position.getY() + 1);
                });
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
            updateSelectedAnimalInfo();
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
        Platform.runLater(() -> {
            animalsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.animalsNumber()));
            animalsSeries.setName("Animals\n" + statsRecord.animalsNumber());
            plantsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.plantsNumber()));
            plantsSeries.setName("Plants\n" + statsRecord.plantsNumber());
            freeFieldsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.freeFields()));
            freeFieldsSeries.setName("Free fields\n" + statsRecord.freeFields());
            popularGenotypesLabel.setText("Most Popular Genotypes: " + statsRecord.mostPopularGenotypes());
            averageEnergySeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageEnergyLevel()));
            averageEnergySeries.setName("Energy\n" + statsRecord.averageEnergyLevel());
            averageLifeSpan.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageLifespan()));
            averageLifeSpan.setName("Lifespan\n" + statsRecord.averageLifespan());
            averageChildrenNumber.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageNumberOfChildren()));
            averageChildrenNumber.setName("Children\n" + statsRecord.averageNumberOfChildren());
        });
    }

    private void updateSelectedAnimalInfo(){
        if (selectedElement != null) {
            Animal animal = (Animal) ((WorldElementBox) selectedElement).getElement();

            SelectedAnimalStats animalStats = simulation.generateAnimalStats(animal);
            genomeLabel.setText(animalStats.genome().toString());
            activeGeneLabel.setText(String.valueOf(animalStats.activeGene()));
            energyLabel.setText(String.valueOf(animalStats.energy()));
            eatenPlantsLabel.setText(String.valueOf(animalStats.eatenPlants()));
            childrenLabel.setText(String.valueOf(animalStats.childrenNumber()));
            descendantsLabel.setText(String.valueOf(animalStats.descendantsNumber()));
            ageLabel.setText(String.valueOf(animalStats.age()));
            dayOfDeathLabel.setText(animalStats.dayOfDeath().map(String::valueOf).orElse(""));
        }
    }

    private void cleanSelectedAnimalInfo(){
        genomeLabel.setText("");
        activeGeneLabel.setText("");
        energyLabel.setText("");
        eatenPlantsLabel.setText("");
        childrenLabel.setText("");
        descendantsLabel.setText("");
        ageLabel.setText("");
        dayOfDeathLabel.setText("");
    }

    public void onToggleClicked() {
        if (simulation.isPaused()) {
            simulation.continueSimulation();
            toggleButton.setText("⏸ Pause");
            showDominantGenesButton.setDisable(true);
            showPreferredFields.setDisable(true);
            highlightedGenes = false;
            highlightedFields = false;
        } else {
            simulation.pause();
            toggleButton.setText("▶ Play");
            showDominantGenesButton.setDisable(false);
            showPreferredFields.setDisable(false);
        }
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
        if (simulation.isPaused()) {
            if (selectedElement != null) {
                selectedElement.getStyleClass().remove("selected-element");
                cleanSelectedAnimalInfo();
                if (selectedElement == element) {
                    selectedElement = null;
                    return;
                }
            }
            selectedElement = element;
            selectedElement.getStyleClass().add("selected-element");
            setElementSize(selectedElement, cellSize);
            updateSelectedAnimalInfo();
        }
    }
}