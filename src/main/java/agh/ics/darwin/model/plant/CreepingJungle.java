package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.util.Boundary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreepingJungle extends AbstractPlantGenerator{

    public CreepingJungle(EarthGlobeMap map, int noOfStartingPlants) {
        super(map, noOfStartingPlants);
    }

    @Override
    public List<Vector2d> getPreferredFields() {
        Set<Vector2d> preferredFields = new HashSet<>();
        // jeżeli mapa jest ogołocona to zwraca pustą listę miejsc preferowanych dla pełnej losowości
        List<Plant> plants = map.getPlants();
        if (plants.isEmpty()) return new ArrayList<>();

        Boundary bounds = map.getCurrentBounds();
        Vector2d lowerLeft = bounds.lowerLeft();
        Vector2d upperRight = bounds.upperRight();

        for (Plant plant : plants){
            Vector2d pos = plant.position();
            for (MapDirection direction : MapDirection.values()){
                Vector2d toCheck = pos.add(direction.toUnitVector());
                if (toCheck.follows(lowerLeft) && toCheck.precedes(upperRight) && map.plantAt(toCheck) == null){
                    preferredFields.add(toCheck);
                }
            }
        }
        return preferredFields.stream().toList();
    }
}
