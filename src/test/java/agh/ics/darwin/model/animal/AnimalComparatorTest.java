package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalComparatorTest {
    @Test
    void compare_whenFirstAnimalHasMoreEnergy_shouldReturnPositive() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 10);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) > 0);
    }

    @Test
    void compare_whenFirstAnimalHasLessEnergy_shouldReturnNegative() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 10);
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) < 0);
    }

    @Test
    void compare_whenEnergyEqualAndFirstAnimalOlder_shouldReturnPositive() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        animal1.incrementAge();
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) > 0);
    }

    @Test
    void compare_whenEnergyEqualAndFirstAnimalYounger_shouldReturnNegative() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        animal2.incrementAge();
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) < 0);
    }

    @Test
    void compare_whenEnergyAndAgeEqualAndFirstAnimalHasMoreChildren_shouldReturnPositive() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        animal1.incrementNumberOfChildren();
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) > 0);
    }

    @Test
    void compare_whenEnergyAndAgeEqualAndFirstAnimalHasLessChildren_shouldReturnNegative() {
        Animal animal1 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        Animal animal2 = new Animal(new Vector2d(0, 0), new FullRandomMutationGenome(5), 5);
        animal2.incrementNumberOfChildren();
        AnimalComparator comparator = new AnimalComparator();

        assertTrue(comparator.compare(animal1, animal2) < 0);
    }

}