package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.model.util.IncorrectPositionException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * Places animal on map. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * Return true if given getPosition on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the getPosition is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given getPosition.
     *
     * @param position The getPosition of the animal.
     * @return animal or null if the getPosition is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);

    List<WorldElement> getElements();

    Boundary getCurrentBounds();

    UUID getId();
}