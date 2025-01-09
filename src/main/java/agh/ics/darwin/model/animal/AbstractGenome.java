package agh.ics.darwin.model.animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractGenome {

    protected List<Integer> genome = new ArrayList<>();
    protected int activeGeneIndex;

    public AbstractGenome(int genomeLength){
        Random random = new Random();
        for (int i = 0; i < genomeLength; i++){
            this.genome.add(random.nextInt(8));
        }
        activeGeneIndex = random.nextInt(genomeLength);
    }

    public AbstractGenome (Animal father, Animal mother){
        int fatherEnergy = father.getEnergy();
        int motherEnergy = mother.getEnergy();
        Random random = new Random();
        boolean genesOrder = random.nextBoolean();
        int genomeLength = father.getGenome().genome.size();
        if (genesOrder){
            int splitPoint = (int) (((double) fatherEnergy / (double) (fatherEnergy+motherEnergy)) * genomeLength);
            this.genome = IntStream.range(0, genomeLength)
                    .map(i -> i < splitPoint ? father.getGenome().getGenome().get(i) : mother.getGenome().getGenome().get(i))
                    .boxed()
                    .collect(Collectors.toList());
        }
        else {
            int splitPoint = (int) (((double) motherEnergy / (double) (fatherEnergy+motherEnergy)) * genomeLength);
            this.genome = IntStream.range(0, genomeLength)
                    .map(i -> i < splitPoint ? mother.getGenome().getGenome().get(i) : father.getGenome().getGenome().get(i))
                    .boxed()
                    .collect(Collectors.toList());
        }
        activeGeneIndex = random.nextInt(genomeLength);
    }

    public List<Integer> getGenome() {
        return genome;
    }
    public int getActiveGene(){
        return genome.get(activeGeneIndex);
    }
    public int getActiveGeneIndex(){
        return activeGeneIndex;
    }
    public void incrementActiveGeneIndex(){
        this.activeGeneIndex++;
        this.activeGeneIndex %= genome.size();
    }
}
