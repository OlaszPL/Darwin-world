package agh.ics.darwin.presenter;

import agh.ics.darwin.Simulation;
import agh.ics.darwin.model.WorldElementBox;
import agh.ics.darwin.model.animal.Animal;
import agh.ics.darwin.stats.SelectedAnimalStats;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AnimalInfoUpdater {
    public static void updateSelectedAnimalInfo(VBox selectedElement, Simulation simulation,
                                         Label genomeLabel, Label activeGeneLabel,
                                         Label energyLabel, Label eatenPlantsLabel,
                                         Label childrenLabel, Label descendantsLabel,
                                         Label ageLabel, Label dayOfDeathLabel) {
        if (selectedElement == null) return;

        Animal animal = (Animal) ((WorldElementBox) selectedElement).getElement();
        SelectedAnimalStats stats = simulation.generateAnimalStats(animal);

        updateLabels(stats, genomeLabel, activeGeneLabel, energyLabel,
                eatenPlantsLabel, childrenLabel, descendantsLabel,
                ageLabel, dayOfDeathLabel);
    }

    public static void cleanSelectedAnimalInfo(Label genomeLabel, Label activeGeneLabel,
                                        Label energyLabel, Label eatenPlantsLabel,
                                        Label childrenLabel, Label descendantsLabel,
                                        Label ageLabel, Label dayOfDeathLabel) {
        genomeLabel.setText("");
        activeGeneLabel.setText("");
        energyLabel.setText("");
        eatenPlantsLabel.setText("");
        childrenLabel.setText("");
        descendantsLabel.setText("");
        ageLabel.setText("");
        dayOfDeathLabel.setText("");
    }

    private static void updateLabels(SelectedAnimalStats stats,
                              Label genomeLabel, Label activeGeneLabel,
                              Label energyLabel, Label eatenPlantsLabel,
                              Label childrenLabel, Label descendantsLabel,
                              Label ageLabel, Label dayOfDeathLabel) {
        genomeLabel.setText(stats.genome().toString());
        activeGeneLabel.setText(String.valueOf(stats.activeGene()));
        energyLabel.setText(String.valueOf(stats.energy()));
        eatenPlantsLabel.setText(String.valueOf(stats.eatenPlants()));
        childrenLabel.setText(String.valueOf(stats.childrenNumber()));
        descendantsLabel.setText(String.valueOf(stats.descendantsNumber()));
        ageLabel.setText(String.valueOf(stats.age()));
        dayOfDeathLabel.setText(stats.dayOfDeath().map(String::valueOf).orElse(""));
    }
}
