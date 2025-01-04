package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.animal.PositionAndDirection;
import agh.ics.darwin.model.plant.Plant;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.model.util.IncorrectPositionException;
import agh.ics.darwin.model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Stream;

public class EarthGlobeMap implements WorldMap {
    protected final Vector2d lowerLeftBound, upperRightBound;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    protected final MapVisualizer vis = new MapVisualizer(this);
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final UUID uuid = UUID.randomUUID();

    public void registerObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void deregisterObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    private void mapChanged(String message){
        for (MapChangeListener observer : observers){
            observer.mapChanged(this, message);
        }
    }

    public EarthGlobeMap(int width, int height) {
        this.lowerLeftBound = new Vector2d(0, 0);
        this.upperRightBound = new Vector2d(width - 1, height - 1);
    }

    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeftBound, upperRightBound);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (animals.containsKey(position) && !animals.get(position).isEmpty()) {
            return Collections.max(animals.get(position));
        }
        return plantAt(position);
    }

    public Plant plantAt(Vector2d position){
        return plants.get(position);
    }

    public List<Animal> getOrderedAnimalsAt(Vector2d position){

        return animals.get(position)!= null ? animals.get(position).stream()
                .sorted()
                .toList() : null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeftBound) && position.precedes(upperRightBound);
    }

    public PositionAndDirection determinePositionOfAnimalOnTheEdge(Animal animal, Vector2d newPosition){
        MapDirection orientation = animal.getOrientation();
        Vector2d position = animal.getPosition();

        if (newPosition.getY() > upperRightBound.getY() || newPosition.getY() < lowerLeftBound.getY()){
            orientation = animal.getOrientation().reverse();
        }

        if (newPosition.getX() > upperRightBound.getX()){
            position = new Vector2d(newPosition.getX() % (upperRightBound.getX() + 1), position.getY());
        }
        else if (newPosition.getX() < lowerLeftBound.getX()){
            position = new Vector2d(upperRightBound.getX() + 1 + newPosition.getX(), position.getY());
        }

        return new PositionAndDirection(position, orientation);
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())){
            if (animals.containsKey(animal.getPosition())){
                animals.get(animal.getPosition()).add(animal);
            }
            else {
                List<Animal> newList = new ArrayList<>();
                newList.add(animal);
                animals.put(animal.getPosition(), newList);
            }
            mapChanged("Animal has been placed at %s".formatted(animal.getPosition()));
        }
        else throw new IncorrectPositionException(animal.getPosition());
    }

    public void addPlant(Plant plant){
        this.plants.put(plant.getPosition(), plant);
    }

    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animals.get(animal.getPosition()).remove(animal);
        animal.move(this);
        if (animals.containsKey(animal.getPosition())){
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            List<Animal> newList = new ArrayList<>();
            newList.add(animal);
            animals.put(animal.getPosition(),newList);
        }
        mapChanged("Animal has been moved from %s to %s".formatted(oldPosition, animal.getPosition()));
    }

    @Override
    public List<WorldElement> getElements(){
        return Stream.concat(animals.values().stream().flatMap(List::stream), plants.values().stream()).toList();
    }

    public List<Plant> getPlants(){
        return plants.values().stream().toList();
    }

    public List<Animal> getAnimals() { return animals.values().stream().flatMap(List::stream).toList();}

    @Override
    public String toString() {
        Boundary bound = getCurrentBounds();
        return vis.draw(bound.lowerLeft(), bound.upperRight());
    }

    @Override
    public UUID getId(){
        return uuid;
    }


}