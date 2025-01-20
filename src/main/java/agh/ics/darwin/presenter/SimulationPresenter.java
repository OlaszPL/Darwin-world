package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.*;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.parameters.*;
import agh.ics.darwin.stats.StatsRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class SimulationPresenter implements MapChangeListener {
    public static final int GRID_HEIGHT_WIDTH = 385;

    @FXML
    public Button toggleButton, showDominantGenesButton, showPreferredFields;
    @FXML
    public Label genomeLabel, activeGeneLabel, energyLabel, eatenPlantsLabel, childrenLabel, descendantsLabel, ageLabel,
            dayOfDeathLabel, descriptionLabel;
    @FXML
    private VBox selectedElement;
    @FXML
    public GridPane mapGrid;
    @FXML
    public TextArea popularGenotypesLabel;
    @FXML
    public VBox statsBox;
    @FXML
    public LineChart<Number, Number> animalsChart, energyChart;

    private Simulation simulation;
    private SimulationParameters simulationParameters;
    private boolean highlightedGenes = false, highlightedFields = false;
    private Set<Animal> highlightedAnimals;
    private Set<Vector2d> highlightedPositions;

    private final ChartUpdater chartUpdater = new ChartUpdater();
    private final MapDrawer mapDrawer = new MapDrawer();

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

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            mapDrawer.drawMap(map, mapGrid, highlightedGenes, highlightedFields,
                    highlightedAnimals, highlightedPositions, selectedElement, this::handleElementClick,
                    simulationParameters.energyParameters().moveEnergy());
            selectedElement = mapDrawer.getCurrentSelectedNode();
            AnimalInfoUpdater.updateSelectedAnimalInfo(selectedElement, simulation, genomeLabel, activeGeneLabel, energyLabel, eatenPlantsLabel, childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);
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
        mapDrawer.drawMap(simulation.getMap(), mapGrid, highlightedGenes, highlightedFields,
                highlightedAnimals, highlightedPositions, selectedElement, this::handleElementClick,
                simulationParameters.energyParameters().moveEnergy());
    }

    @FXML
    public void onShowPreferredFieldsClicked() {
        if (!highlightedFields){
            highlightedPositions = new HashSet<>(simulation.getPreferredFields());
        }
        highlightedFields = !highlightedFields;
        mapDrawer.drawMap(simulation.getMap(), mapGrid, highlightedGenes, highlightedFields,
                highlightedAnimals, highlightedPositions, selectedElement, this::handleElementClick,
                simulationParameters.energyParameters().moveEnergy());
    }

    private void handleElementClick(VBox element) {
        if (!simulation.isPaused()) return;

        if (selectedElement != null) {
            selectedElement.getStyleClass().remove("selected-element");
            AnimalInfoUpdater.cleanSelectedAnimalInfo(genomeLabel, activeGeneLabel, energyLabel,
                    eatenPlantsLabel, childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);

            if (selectedElement == element) {
                selectedElement = null;
                return;
            }
        }
        selectedElement = element;
        selectedElement.getStyleClass().add("selected-element");
        mapDrawer.setElementSize(selectedElement, mapDrawer.cellSize);
        AnimalInfoUpdater.updateSelectedAnimalInfo(selectedElement, simulation,
                genomeLabel, activeGeneLabel, energyLabel, eatenPlantsLabel,
                childrenLabel, descendantsLabel, ageLabel, dayOfDeathLabel);
    }
}