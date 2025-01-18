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
import agh.ics.darwin.stats.CsvHandler;
import agh.ics.darwin.stats.StatsCreator;
import agh.ics.darwin.stats.StatsRecord;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Simulation implements Runnable {
    private final SimulationParameters simulationParameters;
    private final EarthGlobeMap map;
    private final AbstractPlantGenerator plantGenerator;
    private int day = 0;
    private final StatsCreator statsCreator;
    private CsvHandler csvHandler = null;
    private volatile boolean running = true;
    private CountDownLatch latch = null;
    private MapChangeListener observer;


    public Simulation(SimulationParameters simulationParameters){
        this.simulationParameters = simulationParameters;

        map = new EarthGlobeMap(simulationParameters.mapParameters().width(), simulationParameters.mapParameters().height());

        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(simulationParameters.mapParameters().width(),
                simulationParameters.mapParameters().height(), simulationParameters.miscParameters().startAnimalsNum());

        for (Vector2d position : positionGenerator){
            AbstractGenome genome = new FullRandomMutationGenome(simulationParameters.miscParameters().genomeLength());
            Animal animal = new Animal(position, genome, simulationParameters.energyParameters().initialAnimalEnergy());
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

        statsCreator = new StatsCreator(map);

        if (simulationParameters.miscParameters().csvSave()) {
            csvHandler = new CsvHandler(simulationParameters.miscParameters().csvPath());
        }
    }

    public void registerObserver(MapChangeListener observer){
        this.observer = observer;
        map.registerObserver(observer);
        observer.mapChanged(map, "Map initialized!");
    }

    public void deregisterObserver(MapChangeListener observer){
        map.deregisterObserver(observer);
    }

    // used to make unit tests of simulation
    EarthGlobeMap getMap(){
        return this.map;
    }

    private void sleep(){
        try {
            Thread.sleep(simulationParameters.miscParameters().interval());
        } catch (InterruptedException e) {
            System.out.printf("Thread interrupted! -> %s%n", e.getMessage());
        }
    }

    public void stop() {
        if (simulationParameters.miscParameters().csvSave()) csvHandler.close();
        running = false;
        countDown();
    }

    public void continueSimulation(){
        running = true;
        this.run();
    }

    // fixes JavaFX being too slow for simulation
    private void updateUI(String message){
        latch = new CountDownLatch(1);
        map.mapChanged(message);
        try{
            latch.await();
        } catch (InterruptedException e){
            System.out.printf("Thread interrupted! -> %s%n", e.getMessage());
        }
    }

    public void countDown(){
        if (latch != null) latch.countDown();
    }

    public void run() {
        while (running) {
            // clean dead animals
            map.cleanDeadAnimals(day++, statsCreator);
            updateUI("Cleaned all dead animals");

            sleep();

            List<Animal> animals = map.getAnimals();
            if (animals.isEmpty()) stop();

            // execute rotation
            for (Animal animal : animals) {
                animal.rotate(simulationParameters.miscParameters().behaviourType());
            }
            updateUI("Animals rotated");

            sleep();

            // execute move
            for (Animal animal : animals) {
                map.move(animal);
                animal.decreaseEnergy(simulationParameters.energyParameters().moveEnergy());
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
                }
            }

            // generate new plants
            plantGenerator.generate(simulationParameters.miscParameters().dailyPlantsNum());

            updateUI("Day: %s".formatted(day));

            // collect statistics
            StatsRecord statsRecord = statsCreator.create(day);
            this.observer.updateStats(statsRecord);
            if (simulationParameters.miscParameters().csvSave()) csvHandler.addRecord(statsRecord);
            System.out.println(statsRecord);

            sleep();
        }
    }
}
