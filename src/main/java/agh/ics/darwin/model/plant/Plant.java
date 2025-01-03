package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.WorldElement;

public class Plant implements WorldElement {
    private final Vector2d position;

    public Plant(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }
}