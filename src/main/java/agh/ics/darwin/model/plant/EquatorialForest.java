package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.util.Boundary;

import java.util.ArrayList;
import java.util.List;

public class EquatorialForest extends AbstractPlantGenerator{

    public EquatorialForest(EarthGlobeMap map, int noOfStartingPlants) {
        super(map, noOfStartingPlants);
    }

    @Override
    public List<Vector2d> getPreferredFields() {
        List<Vector2d> preferredFields = new ArrayList<>();
        Boundary bounds = map.getCurrentBounds();
        Vector2d lowerLeft = bounds.lowerLeft();
        Vector2d upperRight = bounds.upperRight();
        int n = upperRight.getY() - lowerLeft.getY() + 1;
        int rows_per_side = (int) ((n * PREFERRED_PERCENTAGE) / 2);

        if (n % 2 == 0){
            int center_down = (n / 2) - 1;
            int center_up = (n / 2);

            for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
                Vector2d vect1 = new Vector2d(x, center_down);
                Vector2d vect2 = new Vector2d(x, center_up);

                if (map.plantAt(vect1) == null) preferredFields.add(vect1);
                if (map.plantAt(vect2) == null) preferredFields.add(vect2);
            }

            for (int i = 1; i <= rows_per_side; i++){
                for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
                    Vector2d vect1 = new Vector2d(x, center_down - i);
                    Vector2d vect2 = new Vector2d(x, center_up + i);

                    if (map.plantAt(vect1) == null) preferredFields.add(vect1);
                    if (map.plantAt(vect2) == null) preferredFields.add(vect2);
                }
            }

        }
        else{
            int center = n / 2;

            for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
                Vector2d vect = new Vector2d(x, center);
                if (map.plantAt(vect) == null) preferredFields.add(vect);
            }

            for (int i = 1; i <= rows_per_side; i++){
                for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++){
                    Vector2d vect1 = new Vector2d(x, center + i);
                    Vector2d vect2 = new Vector2d(x, center - i);

                    if (map.plantAt(vect1) == null) preferredFields.add(vect1);
                    if (map.plantAt(vect2) == null) preferredFields.add(vect2);
                }
            }
        }

        return preferredFields;
    }
}
