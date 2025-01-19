package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void check_if_animal_initialized_correct(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;

        //when
        Animal animal = new Animal(position, genome, energy);

        //then
        assertEquals(new Vector2d(0,0), animal.getPosition());
        assertEquals(animal.getGenome(), genome);
        assertEquals(8, animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertInstanceOf(MapDirection.class,animal.getOrientation());
        assertEquals(0, animal.getNumberOfChildren());
        assertEquals(0, animal.getNumberOfEatenPlants());
        assertTrue(animal.getDayOfDeath().isEmpty());
        assertTrue(animal.getParents().isEmpty());
    }

    @Test
    void isChildEnergyEqualDoubleEnergyForChild(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;

        //when
        Animal child = new Animal(father, mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(2*energyForChild, child.getEnergy());

    }

    @Test
    void isChildPositionTheSameAsParents(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;

        //when
        Animal child = new Animal(father, mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(child.getPosition(),father.getPosition());
    }

    @Test
    void areChildParentsSetCorrectly(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;

        //when
        Animal child = new Animal(father, mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(child.getParents(), Optional.of(List.of(father, mother)));
    }


    @Test
    void isIdInstanceOfUUID(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;

        //when
        Animal animal = new Animal(position, genome, energy);

        //then
        assertInstanceOf(UUID.class,animal.getId());
    }

    @Test
    void isOrientationSetCorrectly(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.setOrientation(MapDirection.NORTH);
        //then
        assertEquals(MapDirection.NORTH, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.NORTHEAST);
        //then
        assertEquals(MapDirection.NORTHEAST, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.EAST);
        //then
        assertEquals(MapDirection.EAST, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.SOUTHEAST);
        //then
        assertEquals(MapDirection.SOUTHEAST, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.SOUTH);
        //then
        assertEquals(MapDirection.SOUTH, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.SOUTHWEST);
        //then
        assertEquals(MapDirection.SOUTHWEST, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.WEST);
        //then
        assertEquals(MapDirection.WEST, animal.getOrientation());

        //when
        animal.setOrientation(MapDirection.NORTHWEST);
        //then
        assertEquals(MapDirection.NORTHWEST, animal.getOrientation());
    }

    @Test
    void doesEatDecreaseEnergyCorrectly(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.eat(5);

        //then
        assertEquals(13, animal.getEnergy());
    }

    @Test
    void doesEatIncrementNumOfEatenPlants(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.eat(5);
        //then
        assertEquals(1, animal.getNumberOfEatenPlants());

        //when
        animal.eat(2);
        //then
        assertEquals(2, animal.getNumberOfEatenPlants());
    }

    @Test
    void isEnergyDecreasedByGivenValue(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.decreaseEnergy(5);
        //then
        assertEquals(3, animal.getEnergy());

        //when
        animal.decreaseEnergy(2);
        //then
        assertEquals(1, animal.getEnergy());
    }

    @Test
    void isAgeIncrementedCorrectly(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.incrementAge();
        //then
        assertEquals(1, animal.getAge());

        //when
        animal.incrementAge();
        //then
        assertEquals(2, animal.getAge());
    }

    @Test
    void isNumberOfChildrenIncrementedCorrectly(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.incrementNumberOfChildren();
        //then
        assertEquals(1, animal.getNumberOfChildren());

        //when
        animal.incrementNumberOfChildren();
        //then
        assertEquals(2, animal.getNumberOfChildren());
    }

    @Test
    void isDeathDaySetCorrect(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when
        animal.setDayOfDeath(13);
        //then
        assertEquals(animal.getDayOfDeath(), Optional.of(13));

        //when
        animal.setDayOfDeath(1);
        //then
        assertEquals(animal.getDayOfDeath(),Optional.of(1));
    }

    @Test
    void areAnimalsDisplayedCorrect(){
        //given
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);
        String expectedString = animal.getOrientation().toString();

        //when+then
        assertEquals(animal.toString(),expectedString);
    }

    @Test
    void isCompareToResultPositiveWhenAnimalHasMoreEnergyThanOther(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 3;
        Animal other = new Animal(position, genome, otherEnergy);

        //when+then
        assertTrue(animal.compareTo(other)>0);
    }

    @Test
    void isCompareToResultNegativeWhenAnimalHasMoreEnergyThanOther(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 10;
        Animal other = new Animal(position, genome, otherEnergy);

        //when+then
        assertTrue(animal.compareTo(other)<0);
    }

    @Test
    void isCompareToResultPositiveWhenEnergyEqualAndAgeHigher(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 5;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 5;
        Animal other = new Animal(position, genome, otherEnergy);
        animal.incrementAge();

        //when+then
        assertEquals(animal.getEnergy(),other.getEnergy());
        assertTrue(animal.getAge()>other.getAge());
        assertTrue(animal.compareTo(other)>0);
    }

    @Test
    void isCompareToResultNegativeWhenEnergyEqualAndAgeLower(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 5;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 5;
        Animal other = new Animal(position, genome, otherEnergy);
        other.incrementAge();

        //when+then
        assertEquals(animal.getEnergy(),other.getEnergy());
        assertTrue(animal.getAge()<other.getAge());
        assertTrue(animal.compareTo(other)<0);
    }

    @Test
    void isCompareToResultPositiveWhenEnergyAndAgeEqualAndChildrenNumBigger(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 5;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 5;
        Animal other = new Animal(position, genome, otherEnergy);
        animal.incrementNumberOfChildren();

        //when+then
        assertEquals(animal.getEnergy(),other.getEnergy());
        assertEquals(animal.getAge(),other.getAge());
        assertTrue(animal.getNumberOfChildren()>other.getNumberOfChildren());
        assertTrue(animal.compareTo(other)>0);
    }


    @Test
    void isCompareToResultNegativeWhenEnergyAndAgeEqualAndChildrenNumBigger(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 5;
        Animal animal = new Animal(position, genome, energy);
        int otherEnergy = 5;
        Animal other = new Animal(position, genome, otherEnergy);
        other.incrementNumberOfChildren();

        //when+then
        assertEquals(animal.getEnergy(),other.getEnergy());
        assertEquals(animal.getAge(),other.getAge());
        assertTrue(animal.getNumberOfChildren()<other.getNumberOfChildren());
        assertTrue(animal.compareTo(other)<0);
    }

    @Test
    void doesReproducingReturnAnimal(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;


        //when+then
        assertInstanceOf(Animal.class,father.reproduce(mother, minNumberOfMutations,maxNumberOfMutations, energyForChild));
    }

    @Test
    void isSumOfEnergyConstantInReproducing(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;


        //when
        int sumOfEnergy = father.getEnergy()+mother.getEnergy();
        Animal child = father.reproduce(mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(mother.getEnergy()+father.getEnergy()+child.getEnergy(),sumOfEnergy);
    }

    @Test
    void isAnimalsEnergyDecreasedByAppropriateValueWhileReproducing(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);
        int initial_father_energy_value = 8;
        int initial_mother_energy_value = 5;
        Animal father = new Animal(position, fatherGenome, initial_father_energy_value);
        Animal mother = new Animal(position, motherGenome, initial_mother_energy_value);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;

        //when
        father.reproduce(mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(initial_father_energy_value-energyForChild, father.getEnergy());
        assertEquals(initial_mother_energy_value-energyForChild, mother.getEnergy());
    }

    @Test
    void isNumberOfChildrenIncrementedInReproducing(){
        //given
        Vector2d position = new Vector2d(1,1);
        AbstractGenome fatherGenome = new FullRandomMutationGenome(5);
        AbstractGenome motherGenome = new FullRandomMutationGenome(5);

        Animal father = new Animal(position, fatherGenome, 9);
        Animal mother = new Animal(position, motherGenome, 7);
        int minNumberOfMutations=0;
        int maxNumberOfMutations=2;
        int energyForChild = 2;
        int num_of_fathers_children = father.getNumberOfChildren();
        int num_of_mothers_children = mother.getNumberOfChildren();


        //when
        father.reproduce(mother, minNumberOfMutations,maxNumberOfMutations, energyForChild);

        //then
        assertEquals(father.getNumberOfChildren(),num_of_fathers_children+1);
        assertEquals(mother.getNumberOfChildren(),num_of_mothers_children+1);
    }

    @Test
    void testAnimalMove() {
        //given
        WorldMap validator = new EarthGlobeMap(10,10);
        Vector2d position = new Vector2d(4,4);
        FullRandomMutationGenome genome = new FullRandomMutationGenome(5);
        int energy = 30;
        Animal animal = new Animal(position, genome, energy);

        for (int i=0; i<20; i++){
            position = animal.getPosition();
            MapDirection orientation = animal.getOrientation();

            //when
            animal.move(validator);

            //then
            Animal animalInStateOfAnimalBeforeMove = new Animal(position,genome,energy);
            Vector2d newPosition = animalInStateOfAnimalBeforeMove.getPosition().add(orientation.toUnitVector());
            if (validator.canMoveTo(newPosition)){
                assertEquals(animal.getPosition(), newPosition);
            }
            else {
                animal.setOrientation(orientation);
                PositionAndDirection posAndDir = validator.determinePositionOfAnimalOnTheEdge(animalInStateOfAnimalBeforeMove,newPosition);
                Vector2d expectedPos = posAndDir.position();
                assertEquals(animal.getPosition(), expectedPos);
            }
        }
    }

    @Test
    void isFullPredestinationBehaviourRotationCorrect(){
        //given
        Vector2d position = new Vector2d(4,4);
        FullRandomMutationGenome genome = new FullRandomMutationGenome(5);
        int energy = 30;
        Animal animal = new Animal(position, genome, energy);

        int activeGene = genome.getActiveGene();
        MapDirection newOrientation = animal.getOrientation().rotate(activeGene);

        //when
        animal.rotate(BehaviourType.FULL_PREDESTINATION_BEHAVIOUR);

        //then
        assertEquals(animal.getOrientation(),newOrientation);
    }

    @Test
    void isAnimalWithABitOfCrazinessBehaviourRotatedBasingOnGenesFromItsGenome(){
        //given
        Vector2d position = new Vector2d(4,4);
        FullRandomMutationGenome genome = new FullRandomMutationGenome(5);
        int energy = 30;
        Animal animal = new Animal(position, genome, energy);

        for (int i=0; i<20; i++){
            List<MapDirection> possibleOrientationsBasedOnGenome = new ArrayList<>();
            for (int j=0; j<genome.getGenes().size(); j++){
                int gene = genome.getGenes().get(j);
                possibleOrientationsBasedOnGenome.add(animal.getOrientation().rotate(gene));
            }

            //when
            animal.rotate(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR);

            //then
            assertTrue(possibleOrientationsBasedOnGenome.contains(animal.getOrientation()));
        }
    }



















}