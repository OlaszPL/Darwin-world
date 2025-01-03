package agh.ics.darwin.model.animal;

import java.util.Random;

class ABitOfCrazinessBehaviour implements AnimalBehaviour{
    private final Random random = new Random();

    public void executeGene(Animal animal) {
        AbstractGenome animalGenome = animal.getGenome();
        if (Math.random() < 0.8){
            animal.setOrientation(animal.getOrientation().rotate(animalGenome.getActiveGene()));
        }
        else {
            animal.setOrirentation(animal.getOrientation().rotate(random.nextInt(animalGenome.getGenome().size())));
        }
        animalGenome.incrementActiveGeneIndex();
    }
}
