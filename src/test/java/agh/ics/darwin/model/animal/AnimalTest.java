package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

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
        assertEquals(animal.getEnergy(),0);
        assertEquals(animal.getAge(),0);
        assertTrue(isMapDirection(animal.getOrientation()));
        assertEquals(animal.getNumberOfChildren(),0);
        assertEquals(animal.getNumberOfEatenPlants(),0);
        assertNull(animal.getDayOfDeath());
        assertTrue(animal.getParents().isEmpty());
    }

    private boolean isMapDirection(MapDirection attribute) {
        if (attribute == null) {
            return false;
        }
        for (MapDirection direction : MapDirection.values()) {
            if (direction == attribute) {
                return true;
            }
        }
        return false;
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
        assertEquals(child.getPosition(),mother.getPosition());
    }

    @Test
    void areChildParentsCorrect





}