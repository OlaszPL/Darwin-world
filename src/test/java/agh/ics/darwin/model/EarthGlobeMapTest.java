package agh.ics.darwin.model;

import agh.ics.darwin.model.animal.AbstractGenome;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.animal.FullRandomMutationGenome;
import agh.ics.darwin.model.plant.Plant;
import agh.ics.darwin.model.util.Boundary;
import agh.ics.darwin.model.util.IncorrectPositionException;
import agh.ics.darwin.stats.StatsCreator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EarthGlobeMapTest {

    @Test
    void getCurrentBoundsTest(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);

        // when
        Boundary bounds = map.getCurrentBounds();

        // then
        assertEquals(new Vector2d(0, 0), bounds.lowerLeft());
        assertEquals(new Vector2d(9, 9), bounds.upperRight());
    }

    @Test
    void testFunctionsFocusedOnPlacingElementsAndCheckingTheirExistence(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(3, 3), genome, 15);
        animal.incrementNumberOfChildren();

        // when
        try {
            map.place(new Animal(new Vector2d(3, 3), genome, 10));
            map.place(new Animal(new Vector2d(3, 3), genome, 5));
            map.place(new Animal(new Vector2d(3, 3), genome, 15));
            map.place(animal);
        } catch (IncorrectPositionException e){
            System.out.println(e.getMessage());
        }
        Plant plant = new Plant(new Vector2d(1,1));
        map.addPlant(plant);

        // then
        assertEquals(animal, map.objectAt(new Vector2d(3,3)));
        assertEquals(plant, map.objectAt(new Vector2d(1,1)));
        assertEquals(plant, map.plantAt(new Vector2d(1,1)));
        assertNull(map.plantAt(new Vector2d(3, 3)));
        assertTrue(map.isOccupied(new Vector2d(1,1)));
        assertTrue(map.isOccupied(new Vector2d(3,3)));
    }

    @Test
    void objectShouldBePlacedAtPosition(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(3, 3);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(1, 2), genome, 7);

        // then
        assertDoesNotThrow(() -> map.place(animal));
        assertEquals(animal, map.objectAt(new Vector2d(1, 2)));
        assertTrue(map.isOccupied(new Vector2d(1,2)));
    }

    @Test
    void objectShouldNotBePlacedAtPosition(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(3, 5);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(-5, 16), genome, 7);

        // then
        assertThrows(IncorrectPositionException.class, () -> map.place(animal));
        assertNull(map.objectAt(new Vector2d(-5, 16)));
        assertFalse(map.isOccupied(new Vector2d(-5, 16)));
    }

    @Test
    void canMoveToTest(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(6, 6);

        // then
        assertTrue(map.canMoveTo(new Vector2d(5, 5)));
        assertTrue(map.canMoveTo(new Vector2d(0, 0)));
        assertFalse(map.canMoveTo(new Vector2d(6, 5)));
        assertFalse(map.canMoveTo(new Vector2d(-1, 5)));
    }

    @Test
    void determinePositionOfAnimalOnTheEdgeTest(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(5, 5);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(4, 4), genome, 7);

        // when + then
        assertEquals(new Vector2d(0, 4), map.determinePositionOfAnimalOnTheEdge(animal, new Vector2d(5,4)).position());
        assertEquals(animal.getOrientation().reverse(), map.determinePositionOfAnimalOnTheEdge(animal, new Vector2d(4,5)).direction());
        assertEquals(new Vector2d(0, 4), map.determinePositionOfAnimalOnTheEdge(animal, new Vector2d(5,5)).position());
        assertEquals(animal.getOrientation().reverse(), map.determinePositionOfAnimalOnTheEdge(animal, new Vector2d(5,5)).direction());
        assertEquals(new Vector2d(0, 3), map.determinePositionOfAnimalOnTheEdge(animal, new Vector2d(5,3)).position());
    }

    @Test
    void getElementsTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal1 = new Animal(new Vector2d(3, 3), genome, 10);
        Animal animal2 = new Animal(new Vector2d(4, 4), genome, 15);
        Animal animal3 = new Animal(new Vector2d(4, 4), genome, 7);
        Plant plant1 = new Plant(new Vector2d(1, 1));
        Plant plant2 = new Plant(new Vector2d(2, 2));

        // when
        try {
            map.place(animal1);
            map.place(animal2);
            map.place(animal3);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }
        map.addPlant(plant1);
        map.addPlant(plant2);

        List<WorldElement> elements = map.getElements();

        // then
        assertTrue(elements.contains(animal1));
        assertTrue(elements.contains(animal2));
        assertTrue(elements.contains(animal3));
        assertTrue(elements.contains(plant1));
        assertTrue(elements.contains(plant2));
        assertEquals(5, elements.size());
    }

    @Test
    void getPlantsTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        Plant plant1 = new Plant(new Vector2d(1, 1));
        Plant plant2 = new Plant(new Vector2d(2, 2));
        Plant plant3 = new Plant(new Vector2d(3, 3));

        // when
        map.addPlant(plant1);
        map.addPlant(plant2);
        map.addPlant(plant3);

        List<Plant> plants = map.getPlants();

        // then
        assertTrue(plants.contains(plant1));
        assertTrue(plants.contains(plant2));
        assertTrue(plants.contains(plant3));
        assertEquals(3, plants.size());
    }

    @Test
    void plantShouldNotBePlaced(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        map.addPlant(new Plant(new Vector2d(1,1)));

        // when
        map.addPlant(new Plant(new Vector2d(1,1)));

        // then
        assertEquals(1, map.getPlantsSize());
    }

    @Test
    void getAnimalsTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal1 = new Animal(new Vector2d(3, 3), genome, 10);
        Animal animal2 = new Animal(new Vector2d(4, 4), genome, 15);
        Animal animal3 = new Animal(new Vector2d(5, 5), genome, 7);

        // when
        try {
            map.place(animal1);
            map.place(animal2);
            map.place(animal3);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        List<Animal> animals = map.getAnimals();

        // then
        assertTrue(animals.contains(animal1));
        assertTrue(animals.contains(animal2));
        assertTrue(animals.contains(animal3));
        assertEquals(3, animals.size());
    }

    @Test
    void cleanDeadAnimalsTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal aliveAnimal = new Animal(new Vector2d(3, 3), genome, 10);
        Animal deadAnimal1 = new Animal(new Vector2d(4, 4), genome, 0);
        Animal deadAnimal2 = new Animal(new Vector2d(5, 5), genome, -5);
        StatsCreator statsCreator = new StatsCreator(map);

        try {
            map.place(aliveAnimal);
            map.place(deadAnimal1);
            map.place(deadAnimal2);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        // when
        map.cleanDeadAnimals(5, statsCreator);

        // then
        List<Animal> animals = map.getAnimals();
        assertTrue(animals.contains(aliveAnimal));
        assertFalse(animals.contains(deadAnimal1));
        assertFalse(animals.contains(deadAnimal2));
        assertEquals(1, animals.size());
        assertEquals(Optional.of(5), deadAnimal1.getDayOfDeath());
        assertEquals(Optional.of(5), deadAnimal2.getDayOfDeath());
    }

    @Test
    void getAnimalsGroupedAtPositionAndOrderedTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal1 = new Animal(new Vector2d(3, 3), genome, 10);
        Animal animal2 = new Animal(new Vector2d(3, 3), genome, 15);
        Animal animal3 = new Animal(new Vector2d(4, 4), genome, 7);
        Animal animal4 = new Animal(new Vector2d(4, 4), genome, 20);
        Animal animal5 = new Animal(new Vector2d(4, 4), genome, 20);
        animal5.incrementNumberOfChildren();

        // when
        try {
            map.place(animal1);
            map.place(animal2);
            map.place(animal3);
            map.place(animal4);
            map.place(animal5);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        List<List<Animal>> groupedAnimals = map.getAnimalsGroupedAtPositionAndOrdered();

        // then
        assertEquals(2, groupedAnimals.size());

        List<Animal> group1 = groupedAnimals.getFirst();
        assertEquals(2, group1.size());
        assertEquals(animal2, group1.getFirst());
        assertEquals(animal1, group1.get(1));

        List<Animal> group2 = groupedAnimals.get(1);
        assertEquals(3, group2.size());
        assertEquals(animal5, group2.getFirst());
        assertEquals(animal4, group2.get(1));
        assertEquals(animal3, group2.get(2));
    }

    @Test
    void removePlantTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        Plant plant1 = new Plant(new Vector2d(1, 1));
        Plant plant2 = new Plant(new Vector2d(2, 2));
        map.addPlant(plant1);
        map.addPlant(plant2);

        // when
        map.removePlant(new Vector2d(1, 1));

        // then
        List<Plant> plants = map.getPlants();
        assertFalse(plants.contains(plant1));
        assertTrue(plants.contains(plant2));
        assertEquals(1, plants.size());
    }

    @Test
    void moveAnimalTest() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(3, 3), genome, 10);

        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            System.out.println(e.getMessage());
        }

        // when
        map.move(animal);

        // then
        List<Animal> animalsAtOldPosition = map.getOrderedAnimalsAt(new Vector2d(3, 3));
        List<Animal> animalsAtNewPosition = map.getOrderedAnimalsAt(animal.getPosition());

        assertTrue(animalsAtOldPosition == null || !animalsAtOldPosition.contains(animal));
        assertTrue(animalsAtNewPosition != null && animalsAtNewPosition.contains(animal));
    }

}