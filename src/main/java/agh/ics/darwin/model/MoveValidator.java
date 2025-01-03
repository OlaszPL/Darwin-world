package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.animal.PositionAndDirection;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given getPosition.
     *
     * @param position
     *            The getPosition checked for the movement possibility.
     * @return True if the object can move to that getPosition.
     */
    boolean canMoveTo(Vector2d position);

    PositionAndDirection determinePositionOfAnimalOnTheEdge(Animal animal, Vector2d newPosition);
}
