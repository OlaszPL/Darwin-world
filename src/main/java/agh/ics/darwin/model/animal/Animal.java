package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.*;

import java.util.*;

public class Animal implements WorldElement, Comparable<Animal> {
    private final UUID uuid = UUID.randomUUID();
    private final AbstractGenome genome;
    private MapDirection orientation;
    private Vector2d position;
    private int energy, age = 0, numberOfChildren = 0, numberOfEatenPlants = 0;
    private Integer dayOfDeath = null;
    private List<Animal> parents = null;
    private int numberOfDescendants = 0;
    private static final Comparator<Animal> comparator = new AnimalComparator();

    public Animal(Vector2d position, AbstractGenome genome, int energy){
        this.position = position;
        this.orientation = MapDirection.getRandomDirection();
        this.genome = genome;
        this.energy = energy;
    }

    public Animal(Animal father, Animal mother, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild){
        this(
                father.getPosition(),
                new FullRandomMutationGenome(father, mother, minNumberOfMutations, maxNumberOfMutations),
                energyForChild * 2
        );
        this.parents = List.of(father, mother);
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

    public AbstractGenome getGenome(){
        return genome;
    }

    public List<Integer> getGenes(){
        return genome.getGenes();
    }

    public void eat(int energy){
        this.energy += energy;
        numberOfEatenPlants++;
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

    public Optional<List<Animal>> getParents() {
        return Optional.ofNullable(parents);
    }

    public int getDescendantsNum(){
        return numberOfDescendants;
    }

    public void incrementNumberOfDescendants(){
        numberOfDescendants++;
    }

    @Override
    public String toString() {
        return orientation.toString();
    }

    @Override
    public String getResourceName() {
        return switch(this.orientation){
            case NORTH -> "/images/N.png";
            case NORTHEAST ->"/images/NE.png";
            case EAST -> "/images/E.png";
            case SOUTHEAST ->"/images/SE.png";
            case SOUTH -> "/images/S.png";
            case SOUTHWEST -> "/images/SW.png";
            case WEST -> "/images/W.png";
            case NORTHWEST -> "/images/NW.png";
        };
    }

    @Override
    public int compareTo(Animal other){
        return comparator.compare(this, other);
    }

    public Animal reproduce(Animal other, int minNumberOfMutations, int maxNumberOfMutations, int energyForChild){
        this.decreaseEnergy(energyForChild);
        other.decreaseEnergy(energyForChild);
        this.incrementNumberOfChildren();
        other.incrementNumberOfChildren();
        Animal child = new Animal(this, other, minNumberOfMutations, maxNumberOfMutations, energyForChild);
        DescendantIncrementer.incrementDescendantsNum(child);
        return child;
    }

    public void rotate(BehaviourType behaviourType){
        switch (behaviourType) {
            case FULL_PREDESTINATION_BEHAVIOUR -> FullPredestinationBehaviour.executeGene(this);
            case A_BIT_OF_CRAZINESS_BEHAVIOUR -> ABitOfCrazinessBehaviour.executeGene(this);
        }
    }
    public void move(MoveValidator validator){
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)) this.position = newPosition;
        else{
            PositionAndDirection tmp = validator.determinePositionOfAnimalOnTheEdge(this, newPosition);
            this.position = tmp.position();
            this.orientation = tmp.direction();
        }
    }
}