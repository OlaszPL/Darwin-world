package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.MapChangeListener;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.WorldElement;
import agh.ics.darwin.model.WorldMap;
import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.parameters.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    public Label descriptionLabel;
    @FXML
    public GridPane mapGrid;
    private static final int CELL_WIDTH = 35;
    private static final int CELL_HEIGHT = 35;

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void drawMap(WorldMap map) {
        clearGrid();
        Boundary boundary = map.getCurrentBounds();

        // Dodanie kolumn (z zapasem na oznaczenie osi Y)
        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX() + 1; x++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }

        // Dodanie wierszy (włącznie z wierszem na oznaczenia osi X na dole)
        for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY() + 1; y++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        // Dodanie etykiety "y/x"
        Label sign = new Label("y/x");
        GridPane.setHalignment(sign, HPos.CENTER);
        GridPane.setValignment(sign, VPos.CENTER);
        // Umieszczamy etykietę w lewym dolnym rogu siatki oznaczeń
        mapGrid.add(sign, 0, boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1);

        // Dodanie oznaczeń osi X (na dole)
        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            Label label = new Label(Integer.toString(x));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            int rowIndex = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;
            mapGrid.add(label, x - boundary.lowerLeft().getX() + 1, rowIndex);
        }

        // Dodanie oznaczeń osi Y (po lewej stronie, rosnąco od dołu)
        for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY(); y++) {
            Label label = new Label(Integer.toString(y));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            int invertedY = boundary.upperRight().getY() - y + boundary.lowerLeft().getY();
            mapGrid.add(label, 0, invertedY);
        }

        // Dodanie elementów mapy (z odwróceniem osi Y, bo domyślnie jest w przeciwną stronę)
        for (WorldElement element : map.getElements()) {
            Vector2d pos = element.getPosition();
            Vector2d mapPos = new Vector2d(
                    pos.getX() - boundary.lowerLeft().getX() + 1,
                    boundary.upperRight().getY() - pos.getY() + boundary.lowerLeft().getY()
            );

            Label label = new Label(map.objectAt(pos).toString());
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            mapGrid.add(label, mapPos.getX(), mapPos.getY());
        }
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap(map);
            descriptionLabel.setText(message);
        });
    }

    public void onSimulationStartClicked(ActionEvent actionEvent) {
        EnergyParameters energy = new EnergyParameters(3, 8, 4, 2);
        MapParameters map = new MapParameters(10, 10);
        MiscParameters misc = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 7, 7, 10, 3, 300);
        MutationParameters mutations = new MutationParameters(1, 2);

        SimulationParameters sim = ParametersValidator.validate(energy, map, mutations, misc);

        Simulation simulation = new Simulation(sim);
        simulation.registerObserver(this);

        new Thread(simulation).start();
    }

}