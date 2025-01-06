package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

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
        assertEquals(animal.getPosition(), new Vector2d(0,0));
        assertEquals(animal.getGenome(), genome);
        assertEquals(animal.getEnergy(),8);
        assertEquals(animal.getAge(),0);
        assertInstanceOf(MapDirection.class,animal.getOrientation());
        assertEquals(animal.getNumberOfChildren(),0);
        assertEquals(animal.getNumberOfEatenPlants(),0);
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
        assertEquals(child.getEnergy(),2*energyForChild);

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
        assertEquals(animal.getOrientation(),MapDirection.NORTH);

        //when
        animal.setOrientation(MapDirection.NORTHEAST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.NORTHEAST);

        //when
        animal.setOrientation(MapDirection.EAST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.EAST);

        //when
        animal.setOrientation(MapDirection.SOUTHEAST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.SOUTHEAST);

        //when
        animal.setOrientation(MapDirection.SOUTH);
        //then
        assertEquals(animal.getOrientation(),MapDirection.SOUTH);

        //when
        animal.setOrientation(MapDirection.SOUTHWEST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.SOUTHWEST);

        //when
        animal.setOrientation(MapDirection.WEST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.WEST);

        //when
        animal.setOrientation(MapDirection.NORTHWEST);
        //then
        assertEquals(animal.getOrientation(),MapDirection.NORTHWEST);
    }

    @Test
    void doesItAtWorkCorrectly(){
        //given:
        Vector2d position = new Vector2d(0,0);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        int energy = 8;
        Animal animal = new Animal(position, genome, energy);

        //when+then
        assertTrue(animal.isAt(animal.getPosition()));
        assertFalse(animal.isAt(new Vector2d(4,2)));
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
        assertEquals(animal.getEnergy(),13);
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
        assertEquals(animal.getNumberOfEatenPlants(),1);

        //when
        animal.eat(2);
        //then
        assertEquals(animal.getNumberOfEatenPlants(),2);
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
        assertEquals(animal.getEnergy(),3);

        //when
        animal.decreaseEnergy(2);
        //then
        assertEquals(animal.getEnergy(),1);
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
        assertEquals(animal.getAge(),1);

        //when
        animal.incrementAge();
        //then
        assertEquals(animal.getAge(),2);
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
        assertEquals(animal.getNumberOfChildren(),1);

        //when
        animal.incrementNumberOfChildren();
        //then
        assertEquals(animal.getNumberOfChildren(),2);
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
        assertEquals(father.getEnergy(),initial_father_energy_value-energyForChild);
        assertEquals(mother.getEnergy(),initial_mother_energy_value-energyForChild);
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



















}