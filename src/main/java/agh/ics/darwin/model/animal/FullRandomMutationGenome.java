package agh.ics.darwin.model.animal;

import agh.ics.darwin.model.util.RandomMutateGenePositionGenerator;
import java.util.Random;

public class FullRandomMutationGenome extends AbstractGenome {
    private final Random random = new Random();

    public FullRandomMutationGenome(Animal father, Animal mother, int minMutations, int maxMutations){
        super(father, mother);
        if (maxMutations > 0){
            this.mutate(minMutations, maxMutations);
        }
    }

    public FullRandomMutationGenome(int genomeLength){
        super(genomeLength);
    }

    private void mutate(int minMutations, int maxMutations){
        int mutations = (minMutations == maxMutations) ? minMutations : random.nextInt((maxMutations-minMutations+1)) + minMutations;
        RandomMutateGenePositionGenerator randomGenerator = new RandomMutateGenePositionGenerator(this.genes.size(), mutations);

        for (int position : randomGenerator){
            this.genes.set(position, (this.genes.get(position) + random.nextInt(7) + 1) % 8);
        }
    }


}
