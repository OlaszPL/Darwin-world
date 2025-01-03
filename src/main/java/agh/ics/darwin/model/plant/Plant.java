package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.WorldElement;

public record Plant(Vector2d position) implements WorldElement {

    @Override
    public String toString() {
        return "*";
    }
}