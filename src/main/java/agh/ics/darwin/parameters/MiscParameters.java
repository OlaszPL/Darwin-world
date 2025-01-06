package agh.ics.darwin.parameters;

import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;

public record MiscParameters(BehaviourType behaviourType, PlantGeneratorType plantGeneratorType,
                             int genomeLength, int startAnimalsNum, int startPlantsNum, int dailyPlantsNum,
                             int interval, boolean csvSave, String csvPath)  {
}