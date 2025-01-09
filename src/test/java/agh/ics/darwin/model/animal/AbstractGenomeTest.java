package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

class AbstractGenomeTest {


    @Test
    void isGenomeLengthTheSameAsGivenArgument(){
        //given
        int genomeLength = 5;

        //when
        AbstractGenome genome = new FullRandomMutationGenome(genomeLength);
        List<Integer> genes = genome.getGenome();

        //then
        assertEquals(genes.size(),genomeLength);
    }

    @Test
    void isAllGeneNumberBetween0and7() {
        //given
        int genomeLength = 7;
        AbstractGenome genome = new FullRandomMutationGenome(genomeLength);

        //when
        List<Integer> genes = genome.getGenome();

        //then
        for (int gene:genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
    }

    @Test
    void isActiveGeneIndexInRangeOfGenomeSize(){
        //given
        int genomeLength = 7;
        AbstractGenome genome = new FullRandomMutationGenome(genomeLength);

        //when
        int activeGeneIndex = genome.getActiveGeneIndex();

        //then
        for (int i=0; i<50; i++){
            assertTrue(activeGeneIndex>=0 && activeGeneIndex<genomeLength);
        }
    }

    @Test
    void isActiveGeneIndexIncrementedModuloGenomeSize(){
        //given
        int genomeLength = 7;
        AbstractGenome genome = new FullRandomMutationGenome(genomeLength);

        for (int i=0; i<genomeLength+1; i++){
            //given
            int activeGeneIndex = genome.getActiveGeneIndex();
            //when
            genome.incrementActiveGeneIndex();
            //then
            assertEquals(genome.getActiveGeneIndex(),(activeGeneIndex+1)%genomeLength);
            assertTrue(genome.getActiveGeneIndex()<genomeLength && genome.getActiveGeneIndex()>=0);}
    }

    @Test
    void doesGetActiveGeneReturnsGeneFromGenomeOnActiveGeneIndex(){
        //given
        int genomeLength = 6;
        AbstractGenome genome = new FullRandomMutationGenome(genomeLength);
        int activeGeneIndex = genome.getActiveGeneIndex();

        //when
        int gene = genome.getActiveGene();

        //then
        assertEquals(gene, genome.getGenome().get(activeGeneIndex));
    }

    @Test
    void doesGenomeBasedOnFatherAndMotherWithoutMutationsHasTheSameGenesAsParentsInProperBalance() {
        // given
        AbstractGenome fatherGenome = new FullRandomMutationGenome(10);
        AbstractGenome motherGenome = new FullRandomMutationGenome(10);
        Animal father = new Animal(new Vector2d(2, 2), fatherGenome, 6);
        Animal mother = new Animal(new Vector2d(2, 2), motherGenome, 4);

        List<Integer> firstPossible = IntStream.range(0, 10)
                .mapToObj(i -> i < 6 ? fatherGenome.getGenome().get(i) : motherGenome.getGenome().get(i))
                .toList();

        List<Integer> secondPossible = IntStream.range(0, 10)
                .mapToObj(i -> i < 4 ? motherGenome.getGenome().get(i) : fatherGenome.getGenome().get(i))
                .toList();

        // when
        List<Integer> childGenome = new FullRandomMutationGenome(father, mother,0,0).getGenome();

        // then
        assertTrue(childGenome.equals(firstPossible) || childGenome.equals(secondPossible));
    }
}

