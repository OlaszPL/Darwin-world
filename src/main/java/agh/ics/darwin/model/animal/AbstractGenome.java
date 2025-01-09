package agh.ics.darwin.model.animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractGenome {

    protected List<Integer> genes = new ArrayList<>();
    protected int activeGeneIndex;

    public AbstractGenome(int genomeLength){
        Random random = new Random();
        for (int i = 0; i < genomeLength; i++){
            this.genes.add(random.nextInt(8));
        }
        activeGeneIndex = random.nextInt(genomeLength);
    }

    public AbstractGenome (Animal father, Animal mother){
        int fatherEnergy = father.getEnergy();
        int motherEnergy = mother.getEnergy();
        Random random = new Random();
        boolean genesOrder = random.nextBoolean();
        int genomeLength = father.getGenome().genes.size();
        if (genesOrder){
            int splitPoint = (int) Math.round((double) fatherEnergy / (double) (fatherEnergy+motherEnergy) * genomeLength);
            this.genes = IntStream.range(0, genomeLength)
                    .map(i -> i < splitPoint ? father.getGenes().get(i) : mother.getGenes().get(i))
                    .boxed()
                    .collect(Collectors.toList());
        }
        else {
            int splitPoint = (int) Math.round((double) motherEnergy / (double) (fatherEnergy+motherEnergy) * genomeLength);
            this.genes = IntStream.range(0, genomeLength)
                    .map(i -> i < splitPoint ? mother.getGenes().get(i) : father.getGenes().get(i))
                    .boxed()
                    .collect(Collectors.toList());
        }
        activeGeneIndex = random.nextInt(genomeLength);
    }

    public List<Integer> getGenes() {
        return genes;
    }
    public int getActiveGene(){
        return genes.get(activeGeneIndex);
    }
    public int getActiveGeneIndex(){
        return activeGeneIndex;
    }
    public void incrementActiveGeneIndex(){
        this.activeGeneIndex++;
        this.activeGeneIndex %= genes.size();
    }
}
