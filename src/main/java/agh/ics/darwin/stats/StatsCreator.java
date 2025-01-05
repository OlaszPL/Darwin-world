package agh.ics.darwin.stats;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.util.Boundary;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsCreator {
    private final EarthGlobeMap map;
    private int sumOfDaysForDeadAnimals = 0;
    private int numberOfDeadAnimals = 0;

    public StatsCreator(EarthGlobeMap map){
        this.map = map;
    }

    public void increaseDeadAnimals(int age){
        sumOfDaysForDeadAnimals += age;
        numberOfDeadAnimals++;
    }

    private int freeFieldsNumber(){
        Vector2d position;
        int cnt = 0;
        Boundary bounds = map.getCurrentBounds();
        for (int i = bounds.lowerLeft().getX(); i <= bounds.upperRight().getX(); i++){
            for (int j = bounds.lowerLeft().getY(); j <= bounds.upperRight().getY(); j++){
                position = new Vector2d(i, j);
                if (!map.isOccupied(position)) cnt++;
            }
        }
        return cnt;
    }

    public StatsRecord create(int day){
        List<Animal> animals = map.getAnimals();

        List<String> animalsGenotypes = animals.stream()
                .map(animal -> animal.getGenome().getGenome().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining("")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        double averageEnergyLevel = animals.stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0.0);

        double averageLifespan = numberOfDeadAnimals > 0 ? (double) sumOfDaysForDeadAnimals / numberOfDeadAnimals : 0;

        double averageNumberOfChildren = animals.stream()
                .mapToInt(Animal::getNumberOfChildren)
                .average()
                .orElse(0.0);

        return new StatsRecord(day, animals.size(), map.getPlantsSize(), freeFieldsNumber(), animalsGenotypes,
                averageEnergyLevel, averageLifespan, averageNumberOfChildren);
    }

}