package agh.ics.darwin.model.animal;

class FullPredestinationBehaviour implements AnimalBehaviour {

    public void executeGene(Animal animal) {
        AbstractGenome animalGenome = animal.getGenome();
        int activeGeneIndex = animalGenome.getActiveGeneIndex();
        animal.setOrientation(animal.getOrientation().rotate(animalGenome.getGenome().get(activeGeneIndex)));
    }
}
