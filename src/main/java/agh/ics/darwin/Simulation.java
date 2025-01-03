package agh.ics.darwin;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.animal.AbstractGenome;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.animal.FullRandomMutationGenome;
import agh.ics.darwin.model.plant.AbstractPlantGenerator;
import agh.ics.darwin.model.plant.CreepingJungle;
import agh.ics.darwin.model.plant.EquatorialForest;
import agh.ics.darwin.model.util.IncorrectPositionException;
import agh.ics.darwin.model.util.RandomPositionGenerator;
import agh.ics.darwin.parameters.SimulationParameters;

public class Simulation {
    private final SimulationParameters simulationParameters;
    private final EarthGlobeMap map;
    private final AbstractPlantGenerator plantGenerator;

    public Simulation(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;

        map = new EarthGlobeMap(simulationParameters.mapParameters().width(), simulationParameters.mapParameters().height());

        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(simulationParameters.mapParameters().width(),
                simulationParameters.mapParameters().height(), simulationParameters.miscParameters().startAnimalsNum());

        for (Vector2d position : positionGenerator){
            AbstractGenome genome = new FullRandomMutationGenome(simulationParameters.miscParameters().genomeLength());
            Animal animal = new Animal(position, genome, simulationParameters.energyParameters().initialAnimalEnergy(),
                    simulationParameters.miscParameters().behaviourType());
            try {
                this.map.place(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        plantGenerator = switch (simulationParameters.miscParameters().plantGeneratorType()){
            case EQUATORIAL_FOREST -> new EquatorialForest(map, simulationParameters.miscParameters().startPlantsNum());
            case CREEPING_JUNGLE -> new CreepingJungle(map, simulationParameters.miscParameters().startPlantsNum());
        };
    }

    public void run(){
        System.out.printf(map.toString());
    }
}
