package agh.ics.darwin.parameters;

import agh.ics.darwin.model.animal.BehaviourType;
import agh.ics.darwin.model.plant.PlantGeneratorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParametersValidatorTest {

    @Test
    void testValidateWithValidParameters() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        assertDoesNotThrow(() -> ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters));
    }

    @Test
    void testValidateWithNegativeInitialAnimalEnergy() {
        EnergyParameters energyParameters = new EnergyParameters(-1, -10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Initial animal energy should be positive!"));
    }

    @Test
    void testValidateWithNegativeDailyPlantsNum() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, -1, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Daily plants number should not be negative!"));
    }

    @Test
    void testValidateWithNullBehaviourType() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(null, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Behaviour type cannot be null!"));
    }

    @Test
    void testValidateWithCsvSaveAndInvalidPath() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, true, "invalid_path");

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Csv path not specified or does not end with .csv extension!"));
    }

    @Test
    void testValidateWithNegativeEnergyGivenToChild() {
        EnergyParameters energyParameters = new EnergyParameters(100, -10, 50, -5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Energy given to child should be positive!"));
    }

    @Test
    void testValidateWithMinReproduceEnergyLowerThanEnergyGivenToChild() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 5, 6, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Minimal reproduce energy should be higher than energy given to child!"));
    }

    @Test
    void testValidateWithNegativeOnePlantEnergy() {
        EnergyParameters energyParameters = new EnergyParameters(-100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Energy given by single plant should be positive!"));
    }

    @Test
    void testValidateWithNegativeMapDimensions() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(-10, 10);
        MutationParameters mutationParameters = new MutationParameters(1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Dimensions should be higher than 0!"));
    }

    @Test
    void testValidateWithNegativeMinMutationsNum() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(-1, 5);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Minim mutations number cannot be negative!"));
    }

    @Test
    void testValidateWithMaxMutationsNumLowerThanMin() {
        EnergyParameters energyParameters = new EnergyParameters(100, 10, 50, 5, 1);
        MapParameters mapParameters = new MapParameters(10, 10);
        MutationParameters mutationParameters = new MutationParameters(5, 1);
        MiscParameters miscParameters = new MiscParameters(BehaviourType.A_BIT_OF_CRAZINESS_BEHAVIOUR, PlantGeneratorType.EQUATORIAL_FOREST, 10, 10, 10, 10, 1, false, null);

        Exception exception = assertThrows(InvalidParametersException.class, () ->
                ParametersValidator.validate(energyParameters, mapParameters, mutationParameters, miscParameters)
        );
        assertTrue(exception.getMessage().contains("Maximal mutations number can't be lower than minimal!"));
    }
}