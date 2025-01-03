package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Animal implements WorldElement, Comparable<Animal> {
    private final UUID uuid = UUID.randomUUID();
    private final AbstractGenome genome;
    private MapDirection orientation;
    private Vector2d position;
    private int energy, age, numberOfChildren, numberOfEatenPlants;
    private Integer dayOfDeath = null;
    private final List<Animal> parents = new ArrayList<>();
    private final BehaviourType behaviourType;

    public Animal(Vector2d position, AbstractGenome genome, int energy, BehaviourType behaviourType){
        this.behaviourType = behaviourType;
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genome = genome;
        this.energy = energy;
        this.age = 0;
        this.numberOfChildren = 0;
    }

    public Animal(Animal father, Animal mother, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild, BehaviourType behaviourType){
        this(
                father.getPosition(),
                new FullRandomMutationGenome(father, mother, minNumberOfMutations, maxNumberOfMutations),
                energyForChild*2, behaviourType
        );
        this.parents.add(father);
        this.parents.add(mother);
    }

    public UUID getId(){
        return uuid;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(MapDirection orientation){
        this.orientation = orientation;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public AbstractGenome getGenome(){
        return genome;
    }

    public void eat(int energy){
        this.energy += energy;
        numberOfEatenPlants += 1;
    }

    public int getEnergy(){
        return energy;
    }

    public void decreaseEnergy(int energy){
        this.energy -= energy;
    }

    public int getNumberOfEatenPlants(){
        return numberOfEatenPlants;
    }

    public int getAge(){
        return age;
    }

    public void incrementAge(){
        age ++;
    }

    public int getNumberOfChildren(){
        return numberOfChildren;
    }

    public void incrementNumberOfChildren(){
        numberOfChildren ++;
    }

    public void setDayOfDeath(Integer day){
        this.dayOfDeath = day;
    }

    public Optional<Integer> getDayOfDeath(){
        return Optional.ofNullable(dayOfDeath);
    }

    public List<Animal> getParents() {
        return parents;
    }

    public void setParents(Animal father, Animal mother){
        parents.add(father);
        parents.add(mother);
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

    @Override
    public int compareTo(Animal other){
        if (this.energy != other.energy){
            return this.energy - other.energy;
        }
        if (this.age != other.age){
            return this.age - other.age;
        }
        if (this.numberOfChildren != other.numberOfChildren){
            return this.numberOfChildren - other.numberOfChildren;
        }
        return 0;
    }

    public Animal reproduce(Animal other, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild){
        this.decreaseEnergy(energyForChild);
        other.decreaseEnergy(energyForChild);
        this.incrementNumberOfChildren();
        other.incrementNumberOfChildren();

        return new Animal(this, other, minNumberOfMutations, maxNumberOfMutations, 2*energyForChild, this.behaviourType);
    }

    public void move(MoveValidator validator){
        switch (behaviourType){
            case FULL_PREDESTINATION_BEHAVIOUR -> FullPredestinationBehaviour.executeGene(this);
            case A_BIT_OF_CRAZINESS_BEHAVIOUR -> ABitOfCrazinessBehaviour.executeGene(this);
        }
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) this.position = newPosition;
        else{
            PositionAndDirection tmp = validator.determinePositionOfAnimalOnTheEdge(this, newPosition);
            this.position = tmp.position();
            this.orientation = tmp.direction();
        }
    }
}