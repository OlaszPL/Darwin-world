package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FullRandomMutationGenomeTest {

    @Test
    void isNumberOfChangedGenesEqualGivenArgument(){
        //given
        AbstractGenome fatherGenome = new FullRandomMutationGenome(10);
        AbstractGenome motherGenome = new FullRandomMutationGenome(10);
        Animal father = new Animal(new Vector2d(2,2),fatherGenome, 6);
        Animal mother = new Animal(new Vector2d(2,2),motherGenome, 4);
        List<Integer> firstPossible = IntStream.range(0, 10)
                .mapToObj(i -> i < 6 ? fatherGenome.getGenes().get(i) : motherGenome.getGenes().get(i))
                .toList();

        List<Integer> secondPossible = IntStream.range(0, 10)
                .mapToObj(i -> i < 6 ? motherGenome.getGenes().get(i) : fatherGenome.getGenes().get(i))
                .toList();

        //when
        FullRandomMutationGenome genome = new FullRandomMutationGenome(father, mother, 3, 3);

        long mutationCount1 = IntStream.range(0, 10)
                .filter(i -> i < 6 ? genome.getGenes().get(i) != fatherGenome.getGenes().get(i) : genome.getGenes().get(i) != motherGenome.getGenes().get(i))
                .count();
        long mutationCount2 = IntStream.range(0, 10)
                .filter(i -> i < 4 ? genome.getGenes().get(i) != motherGenome.getGenes().get(i) : genome.getGenes().get(i) != fatherGenome.getGenes().get(i))
                .count();

        //then
        assertTrue(mutationCount1==3 || mutationCount2==3);
    }

    @Test
    void isNumberOfChangedGenesInRangeOfGivenMinAndMaxNumOfMutations(){
        //given
        AbstractGenome fatherGenome = new FullRandomMutationGenome(14);
        AbstractGenome motherGenome = new FullRandomMutationGenome(14);
        Animal father = new Animal(new Vector2d(2,2),fatherGenome, 6);
        Animal mother = new Animal(new Vector2d(2,2),motherGenome, 4);

        //when
        FullRandomMutationGenome genome = new FullRandomMutationGenome(father, mother, 2, 5);

        //then
        long mutationCount1 = IntStream.range(0, 14)
                .filter(i -> i < 8 ? genome.getGenes().get(i) != fatherGenome.getGenes().get(i) : genome.getGenes().get(i) != motherGenome.getGenes().get(i))
                .count();
        long mutationCount2 = IntStream.range(0, 14)
                .filter(i -> i < 6 ? genome.getGenes().get(i) != motherGenome.getGenes().get(i) : genome.getGenes().get(i) != fatherGenome.getGenes().get(i))
                .count();

        //then
        assertTrue(mutationCount1>=2 && mutationCount1<=5 || mutationCount2>=2 && mutationCount2<=5);
    }
}