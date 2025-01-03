package agh.ics.darwin.model.animal;

import java.util.Random;

class ABitOfCrazinessBehaviour implements AnimalBehaviour{
    private final static Random RANDOM = new Random();

    public static void executeGene(Animal animal) {
        AbstractGenome animalGenome = animal.getGenome();
        if (Math.random() < 0.8){
            animal.setOrientation(animal.getOrientation().rotate(animalGenome.getActiveGene()));
        }
        else {
            animal.setOrientation(animal.getOrientation().rotate(RANDOM.nextInt(animalGenome.getGenome().size())));
        }
        animalGenome.incrementActiveGeneIndex();
    }
}
