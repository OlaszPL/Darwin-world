package agh.ics.darwin.parameters;

import java.util.ArrayList;
import java.util.List;

public class ParametersValidator {

    public static SimulationParameters validate(EnergyParameters energyParameters, MapParameters mapParameters,
                                                MutationParameters mutationParameters, MiscParameters miscParameters){

        List<String> errors = new ArrayList<>();

        if (energyParameters.initialAnimalEnergy() <= 0){
            errors.add("Initial animal energy should be positive!");
        }
        if (energyParameters.energyGivenToChild() <= 0){
            errors.add("Energy given to child should be positive!");
        }
        if (energyParameters.minReproduceEnergy() < energyParameters.energyGivenToChild()){
            errors.add("Minimal reproduce energy should be higher than energy given to child!");
        }
        if (energyParameters.onePlantEnergy() <= 0){
            errors.add("Energy given by single plant should be positive!");
        }
        if (mapParameters.height() <= 0 || mapParameters.width() <= 0){
            errors.add("Dimensions should be higher than 0!");
        }
        if (mutationParameters.minMutationsNum() < 0){
            errors.add("Minim mutations number cannot be negative!");
        }
        if (mutationParameters.maxMutationNum() < mutationParameters.minMutationsNum()){
            errors.add("Maximal mutations number can't be lower than minimal!");
        }
        if (miscParameters.dailyPlantsNum() < 0){
            errors.add("Daily plants number should not be negative!");
        }
        if (miscParameters.genomeLength() < 0){
            errors.add("Genome length should not be negative!");
        }
        if (miscParameters.genomeLength() < mutationParameters.minMutationsNum()){
            errors.add("Minimal mutations number can't be higher than genom length!");
        }
        if (miscParameters.startAnimalsNum() < 0){
            errors.add("Starting number of animals can't be negative!");
        }
        if (miscParameters.startPlantsNum() < 0){
            errors.add("Starting number of plants can't be negative!");
        }

        if (!errors.isEmpty()){
            throw new InvalidParametersException(String.join(", ", errors));
        }

        return new SimulationParameters(energyParameters, mapParameters,
                mutationParameters, miscParameters);
    }
}
