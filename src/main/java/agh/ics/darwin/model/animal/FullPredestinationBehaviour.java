package agh.ics.darwin.model.animal;

class FullPredestinationBehaviour implements AnimalBehaviour {
    public static void executeGene(Animal animal) {
        AbstractGenome animalGenome = animal.getGenome();
        animal.setOrientation(animal.getOrientation().rotate(animalGenome.getActiveGene()));
        animalGenome.incrementActiveGeneIndex();
    }
}
