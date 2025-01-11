package agh.ics.darwin;

import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import agh.ics.darwin.parameters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    @Test
    void constructorTest() {
        // given
        EnergyParameters energy = new EnergyParameters(3, 8, 4, 2, 1);
        MapParameters map = new MapParameters(10, 10);
        MiscParameters misc = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST,
                7, 7, 10, 3, 300, false, "out.csv");
        MutationParameters mutations = new MutationParameters(1, 2);

        SimulationParameters params = ParametersValidator.validate(energy, map, mutations, misc);

        // when
        Simulation simulation = new Simulation(params);

        // then
        assertEquals(10, simulation.getMap().getCurrentBounds().upperRight().getY() + 1);
        assertEquals(10, simulation.getMap().getCurrentBounds().upperRight().getX() + 1);
        assertEquals(7, simulation.getMap().getAnimals().size());
        assertEquals(10, simulation.getMap().getPlantsSize());

        for (Animal animal : simulation.getMap().getAnimals()){
            assertEquals(8, animal.getEnergy());
        }
    }
}