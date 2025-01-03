package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.*;

public class Animal implements WorldElement {
    private MapDirection direction = MapDirection.NORTH;
    private Vector2d position;

    public Animal(Vector2d position){
        this.position = position;
    }

    public Animal(){
        this(new Vector2d(2,2));
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public MapDirection getDirection() {
        return direction;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void move(MoveDirection direction, MoveValidator validator){
        switch (direction){
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            case FORWARD -> {
                Vector2d newPosition = this.position.add(this.direction.toUnitVector());
                if (validator.canMoveTo(newPosition)) this.position = newPosition;
            }
            case BACKWARD -> {
                Vector2d newPosition = this.position.subtract(this.direction.toUnitVector());
                if (validator.canMoveTo(newPosition)) this.position = newPosition;
            }
        }
    }
}