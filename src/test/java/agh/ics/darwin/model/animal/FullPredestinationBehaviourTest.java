package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullPredestinationBehaviourTest {

    @Test
    void doesExecuteSetOrientationBasedOnAppropriateGene(){
        //given
        AbstractGenome genome = new FullRandomMutationGenome(7);
        Animal animal = new Animal(new Vector2d(2,2),genome,6);
        int activeGeneIndex = genome.getActiveGeneIndex();
        MapDirection orientation = animal.getOrientation();

        //when
        FullPredestinationBehaviour.executeGene(animal);

        //then
        assertEquals(animal.getOrientation(),orientation.rotate(genome.getGenes().get(activeGeneIndex)));
    }

    @Test
    void doesExecuteGeneIncrementAnimalGenomeActiveGeneIndex(){
        //given
        AbstractGenome genome = new FullRandomMutationGenome(5);
        Animal animal = new Animal(new Vector2d(1,1),genome,10);
        int activeGeneIndex = genome.getActiveGeneIndex();

        //when
        FullPredestinationBehaviour.executeGene(animal);

        //then
        assertEquals(genome.getActiveGeneIndex(),(activeGeneIndex+1)%genome.getGenes().size());
    }
}