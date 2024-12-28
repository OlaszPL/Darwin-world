package agh.ics.darwin.model.util;

import agh.ics.darwin.model.Vector2d;

public class IncorrectPositionException extends Exception {
    public IncorrectPositionException(Vector2d position) {
        super("Position %s is not correct".formatted(position));
    }
}
