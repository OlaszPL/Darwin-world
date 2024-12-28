package agh.ics.darwin.model;

import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.model.util.IncorrectPositionException;
import agh.ics.darwin.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Vector2d lowerLeftBound, upperRightBound;
    private final Map<Vector2d, Animal> animals = new HashMap<>();
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

    public AbstractWorldMap(Vector2d lowerLeftBound, Vector2d upperRightBound) {
        this.lowerLeftBound = lowerLeftBound;
        this.upperRightBound = upperRightBound;
    }

    public AbstractWorldMap(){
        this(new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE), new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeftBound, upperRightBound);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
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
        if (canMoveTo(animal.getPosition())){
            animals.put(animal.getPosition(), animal);
            mapChanged("Animal has been placed at %s".formatted(animal.getPosition()));
        }
        else throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animals.get(animal.getPosition()) == animal){ // jeżeli zwierzak jest na mapie
            Vector2d oldPosition = animal.getPosition();
            animals.remove(animal.getPosition(), animal);
            animal.move(direction, this);
            animals.put(animal.getPosition(), animal); // jeżeli ruch jest niemożliwy, to nic się nie zmieniło
            mapChanged("Animal has been moved from %s to %s".formatted(oldPosition, animal.getPosition()));
        }
    }

    @Override
    public List<WorldElement> getElements(){
//      kopia wartości aby nie było problemu z błędnym stanem obiektu
        return new ArrayList<>(List.copyOf(animals.values()));
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