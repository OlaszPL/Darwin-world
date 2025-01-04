package agh.ics.darwin;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.MapChangeListener;
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

import java.util.List;

public class Simulation implements Runnable {
    private final SimulationParameters simulationParameters;
    private final EarthGlobeMap map;
    private final AbstractPlantGenerator plantGenerator;
    private int day = 0;


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

    public void registerObserver(MapChangeListener observer){
        map.registerObserver(observer);
        observer.setWorldMap(map);
        observer.mapChanged(map, "Map initialized!");
    }

    public void deregisterObserver(MapChangeListener observer){
        map.deregisterObserver(observer);
    }

    public void run() {
        while (true) {
            // clean dead animals
            map.cleanDeadAnimals(day++);
            map.mapChanged("Cleaned all dead animals");

            try {
                Thread.sleep(simulationParameters.miscParameters().interval());
            } catch (InterruptedException e) {
                System.out.printf("Thread interrupted! -> %s%n", e.getMessage());
            }
            List<Animal> animals = map.getAnimals();

            // execute moves
            for (Animal animal : animals) {
                map.move(animal);
                animal.decreaseEnergy(1);
                animal.incrementAge();
            }

            List<List<Animal>> groupedAnimals = map.getAnimalsGroupedAtPositionAndOrdered();

            // execute eat
            for (List<Animal> animalList : groupedAnimals) {
                Vector2d position = animalList.getFirst().getPosition();
                if (map.plantAt(position) != null) {
                    animalList.getFirst().eat(simulationParameters.energyParameters().onePlantEnergy());
                    map.removePlant(position);
                }
            }

            // reproduce
            for (List<Animal> animalList : groupedAnimals) {
                if (animalList.size() > 1) {
                    Animal animal1 = animalList.getFirst();
                    Animal animal2 = animalList.get(1);
                    if (animal2.getEnergy() >= simulationParameters.energyParameters().minReproduceEnergy()) {
                        try {
                            map.place(animal1.reproduce(animal2, simulationParameters.mutationParameters().minMutationsNum(),
                                    simulationParameters.mutationParameters().maxMutationNum(), simulationParameters.energyParameters().energyGivenToChild()));
                        } catch (IncorrectPositionException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    animal1.decreaseEnergy(simulationParameters.energyParameters().minReproduceEnergy());
                    animal2.decreaseEnergy(simulationParameters.energyParameters().minReproduceEnergy());
                }
            }

            // generate new plants
            plantGenerator.generate(simulationParameters.miscParameters().dailyPlantsNum());

            map.mapChanged("Day: %s".formatted(day));

            try {
                Thread.sleep(simulationParameters.miscParameters().interval());
            } catch (InterruptedException e) {
                System.out.printf("Thread interrupted! -> %s%n", e.getMessage());
            }
        }
    }
}
