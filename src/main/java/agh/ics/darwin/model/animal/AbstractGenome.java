package agh.ics.darwin.model.animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGenome {

    protected List<Integer> genome = new ArrayList<>();

    public AbstractGenome(int genomeLength){
        Random random = new Random();
        for (int i = 0; i < genomeLength; i++){
            this.genome.add(random.nextInt(8));
        }
    }

    public AbstractGenome(List<Integer> genome){
        this.genome = genome;
    }

    public AbstractGenome (Animal father, Animal mother){
        int fatherEnergy = father.getEnergy();
        int motherEnergy = mother.getEnergy();
        Random random = new Random();
        boolean genesOrder = random.nextBoolean();
        int genomeLength = father.getGenome().genome.size();
        if (genesOrder){
            int splitPoint = (int) ((double) fatherEnergy / (double) (fatherEnergy+motherEnergy)) * genomeLength;
            for (int i=0; i<splitPoint; i++){
                this.genome.add(father.getGenome().genome.get(i));
            }
            for (int i = splitPoint; i< genomeLength; i++){
                this.genome.add(mother.getGenome().genome.get(i));
            }
        }
        else {
            int splitPoint = (int) ((double) motherEnergy / (double) (fatherEnergy+motherEnergy)) * genomeLength;
            for (int i=0; i<splitPoint; i++){
                this.genome.add(mother.getGenome().genome.get(i));
            }
            for (int i = splitPoint; i< genomeLength; i++){
                this.genome.add(mother.getGenome().genome.get(i));
            }
        }
    }
}
