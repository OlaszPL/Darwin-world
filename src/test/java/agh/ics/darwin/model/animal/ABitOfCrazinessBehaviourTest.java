package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ABitOfCrazinessBehaviourTest {

    @Test
    void isABitOfCrazinessBehaviourExecuteGeneFromAnimalGenome(){
        //given
        Vector2d position = new Vector2d(4,4);
        FullRandomMutationGenome genome = new FullRandomMutationGenome(5);
        int energy = 30;
        Animal animal = new Animal(position, genome, energy);

        for (int i=0; i<20; i++){
            List<MapDirection> possibleOrientationsBasedOnGenome = new ArrayList<>();
            for (int j=0; j<genome.getGenome().size(); j++){
                int gene = genome.getGenome().get(j);
                possibleOrientationsBasedOnGenome.add(animal.getOrientation().rotate(gene));
            }
            //when
            animal.rotate(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR);

            //then
            assertTrue(possibleOrientationsBasedOnGenome.contains(animal.getOrientation()));
        }
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
        assertEquals(genome.getActiveGeneIndex(),(activeGeneIndex+1)%genome.getGenome().size());
    }

}