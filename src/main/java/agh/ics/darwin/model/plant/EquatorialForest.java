package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;

import java.util.List;

public class EquatorialForest extends AbstractPlantGenerator{

    public EquatorialForest(EarthGlobeMap map) {
        super(map);
    }

    @Override
    public List<Vector2d> getPreferredFields() {
        return List.of();
    }
}
