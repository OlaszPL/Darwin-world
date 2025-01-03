package agh.ics.darwin.model;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final AbstractGenome genome;
    private int energy;

    public Animal(Vector2d position, MapDirection orientation, AbstractGenome genome, int energy){
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genome = genome;
        this.energy = energy;
    }
    public Animal(Animal father, Animal mother, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild){
        this.position = father.getPosition();
        this.orientation = MapDirection.getRandomDirection();
        this.genome = new FullRandomMutationGenome(father, mother, minNumberOfMutations, maxNumberOfMutations);
        this.energy = 2 * energyForChild;
    }


    @Override
    public String toString() {
        return orientation.toString();
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public AbstractGenome getGenome(){
        return this.genome;
    }

    public int getEnergy(){
        return this.energy;
    }


//    public void move(MoveDirection direction, MoveValidator validator){
//        switch (direction){
//            case RIGHT -> this.orientation = this.orientation.next();
//            case LEFT -> this.orientation = this.orientation.previous();
//            case FORWARD -> {
//                Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
//                if (validator.canMoveTo(newPosition)) this.position = newPosition;
//            }
//            case BACKWARD -> {
//                Vector2d newPosition = this.position.subtract(this.orientation.toUnitVector());
//                if (validator.canMoveTo(newPosition)) this.position = newPosition;
//            }
//        }
//    }
}