package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.*;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.parameters.*;
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

import java.util.Objects;

public class SimulationPresenter implements MapChangeListener {
    private SimulationParameters simulationParameters;
    @FXML
    public Label descriptionLabel;
    @FXML
    public GridPane mapGrid;
    private static final int CELL_WIDTH = 35;
    private static final int CELL_HEIGHT = 35;
    public Button StopButton;
    public Button ContinueButton;
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
        // Dodajemy listener do okna po inicjalizacji komponentów
        Platform.runLater(() -> {
            Stage stage = (Stage) mapGrid.getScene().getWindow();
            onSimulationStartClicked();
            stage.setOnCloseRequest(this::handleWindowClosing);
        });
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

    private void drawMapElements(WorldMap map, int left, int right, int bottom, int top) {
        for (int x = left; x <= right; x++) {
            for (int y = bottom; y <= top; y++) {
                Vector2d position = new Vector2d(x, y);
                if (map.isOccupied(position)) {
                    mapGrid.add(new WorldElementBox(map.objectAt(position)), position.getX() - left + 1, top - position.getY() + 1);
                }
            }
        }
    }

    private void drawAxes(int left, int right, int bottom, int top) {
        mapGrid.add(new Label("y\\x"),0,0);
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        for (int x = left; x <= right; x++){
            mapGrid.add(new Label("%d".formatted(x)), x - left + 1, 0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int y = bottom; y <= top; y++){
            mapGrid.add(new Label("%d".formatted(y)), 0,  top - y + 1);
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap(map);
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
        ContinueButton.setDisable(true);
        StopButton.setDisable(false);
    }

    public void onContinueClicked(){
        simulation.continueSimulation();
        ContinueButton.setDisable(true);
        StopButton.setDisable(false);
    }

    public void onSimulationStopClicked() {
        StopButton.setDisable(true);
        ContinueButton.setDisable(false);
        simulation.stop();
    }

    public void updateStats(StatsRecord statsRecord) {
        Platform.runLater(() -> {
            animalsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.animalsNumber()));
            plantsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.plantsNumber()));
            freeFieldsSeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.freeFields()));
            popularGenotypesLabel.setText("Most Popular Genotypes: " + statsRecord.mostPopularGenotypes());
            averageEnergySeries.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageEnergyLevel()));
            averageLifeSpan.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageLifespan()));
            averageChildrenNumber.getData().add(new XYChart.Data<>(statsRecord.days(), statsRecord.averageNumberOfChildren()));
        });
    }

}