package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.*;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private final AbstractGenome genome;
    private int energy;

    public Animal(Vector2d position, AbstractGenome genome, int energy){
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genome = genome;
        this.energy = energy;
    }
    public Animal(Animal father, Animal mother, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild){
        this.position = father.position();
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
    public Vector2d position() {
        return position;
    }

    public AbstractGenome getGenome(){
        return this.genome;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void setOrientation(MapDirection orientation){
        this.orientation = orientation;
    }


    public void move(MoveValidator validator, BehaviourType behaviourType){
        switch (behaviourType){
            case FULL_PREDESTINATION_BEHAVIOUR -> FullPredestinationBehaviour.executeGene(this);
            case A_BIT_OF_CRAZINESS_BEHAVIOUR -> ABitOfCrazinessBehaviour.executeGene(this);
        }
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) this.position = newPosition;
    }
}