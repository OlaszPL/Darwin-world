package agh.ics.darwin.stats;

import java.util.List;

public record SelectedAnimalStats(List<Integer> genome, int activeGene, int energy, int eatenPlants, int childrenNumber,
                                  int descendantsNumber, int age, java.util.Optional<Integer> dayOfDeath ) {

}
