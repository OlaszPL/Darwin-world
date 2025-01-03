package agh.ics.darwin;

import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import agh.ics.darwin.parameters.*;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Main {
    public static void main(String[] args) {
        EnergyParameters energy = new EnergyParameters(3, 6, 4, 2);
        MapParameters map = new MapParameters(10, 10);
        MiscParameters misc = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 7, 7, 10, 3);
        MutationParameters muta = new MutationParameters(1, 2);

        SimulationParameters sim = ParametersValidator.validate(energy, map, muta, misc);

        Simulation simulation = new Simulation(sim);

        // utf-8 fix
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }

        simulation.run();
    }
}
