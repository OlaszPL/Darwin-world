package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.plant.Plant;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.model.util.IncorrectPositionException;
import agh.ics.darwin.model.util.MapVisualizer;

import java.util.*;

public class EarthGlobeMap implements WorldMap {
    protected final Vector2d lowerLeftBound, upperRightBound;
    private final Map<Vector2d, Animal> animals = new HashMap<>();
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

    public EarthGlobeMap(Vector2d lowerLeftBound, Vector2d upperRightBound) {
        this.lowerLeftBound = lowerLeftBound;
        this.upperRightBound = upperRightBound;
    }

    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeftBound, upperRightBound);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    public Plant plantAt(Vector2d position){
        return plants.get(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeftBound) && position.precedes(upperRightBound) && !isOccupied(position);
    }

    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.position())){
            animals.put(animal.position(), animal);
            mapChanged("Animal has been placed at %s".formatted(animal.position()));
        }
        else throw new IncorrectPositionException(animal.position());
    }

    public void addPlant(Plant plant){
        this.plants.put(plant.position(), plant);
    }

    @Override
    public void move(Animal animal) {
        if (animals.get(animal.position()) == animal){ // jeżeli zwierzak jest na mapie
            Vector2d oldPosition = animal.position();
            animals.remove(animal.position(), animal);
            animal.move(this);
            animals.put(animal.position(), animal); // jeżeli ruch jest niemożliwy, to nic się nie zmieniło
            mapChanged("Animal has been moved from %s to %s".formatted(oldPosition, animal.position()));
        }
    }

    @Override
    public List<WorldElement> getElements(){
//      kopia wartości aby nie było problemu z błędnym stanem obiektu
        return new ArrayList<>(List.copyOf(animals.values()));
    }

    public List<Plant> getPlants(){
        return new ArrayList<>(List.copyOf(plants.values()));
    }

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