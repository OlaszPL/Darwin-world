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
    private WorldMap map;
    @FXML
    public Label descriptionLabel;
    @FXML
    public GridPane mapGrid;
    private static final int CELL_WIDTH = 35;
    private static final int CELL_HEIGHT = 35;

    @Override
    public void setWorldMap(WorldMap map){
        this.map = map;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void drawMap(){
        clearGrid();
        Boundary boundary = map.getCurrentBounds();

        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX() + 1; x++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY() + 1; y++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }

        // dodanie oznaczeń osi
        Label sign = new Label("y/x");
        GridPane.setHalignment(sign, HPos.CENTER);
        GridPane.setValignment(sign, VPos.CENTER);
        mapGrid.add(sign , 0, 0);

        for (int x = boundary.lowerLeft().getX(); x <= boundary.upperRight().getX(); x++) {
            Label label = new Label(Integer.toString(x));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            mapGrid.add(label, x - boundary.lowerLeft().getX() + 1, 0);
        }
        for (int y = boundary.lowerLeft().getY(); y <= boundary.upperRight().getY(); y++) {
            Label label = new Label(Integer.toString(y));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            mapGrid.add(label, 0, y - boundary.lowerLeft().getY() + 1);
        }

        for (WorldElement element : map.getElements()){
            Vector2d pos = element.getPosition();
            Vector2d mapPos = new Vector2d(pos.getX() - boundary.lowerLeft().getX() + 1,
                    pos.getY() - boundary.lowerLeft().getY() + 1);

            Label label = new Label(map.objectAt(pos).toString());

            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);

            mapGrid.add(label, mapPos.getX(), mapPos.getY());
        }
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
            descriptionLabel.setText(message);
        });
    }

    public void onSimulationStartClicked(ActionEvent actionEvent) {
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));

        EnergyParameters energy = new EnergyParameters(3, 8, 4, 2);
        MapParameters map = new MapParameters(10, 10);
        MiscParameters misc = new MiscParameters(BehaviourType.FULL_PREDESTINATION_BEHAVIOUR, PlantGeneratorType.CREEPING_JUNGLE, 7, 7, 10, 3, 300);
        MutationParameters mutations = new MutationParameters(1, 2);

        SimulationParameters sim = ParametersValidator.validate(energy, map, mutations, misc);

        Simulation simulation = new Simulation(sim);
        simulation.registerObserver(this);

        new Thread(simulation).start();
    }

}