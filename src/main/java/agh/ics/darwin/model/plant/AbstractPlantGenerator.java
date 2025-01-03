package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import agh.ics.darwin.model.util.Boundary;

import java.util.*;

public abstract class AbstractPlantGenerator {
    protected final EarthGlobeMap map;

    public AbstractPlantGenerator(EarthGlobeMap map){
        this.map = map;
    }

    public abstract List<Vector2d> getPreferredFields();

    private List<Vector2d> getLessPreferredList(List<Vector2d> preferredFields){
        Set<Vector2d> preferredSet = new HashSet<>(preferredFields);
        List<Vector2d> lessPreferred = new ArrayList<>();
        Boundary bounds = map.getCurrentBounds();
        Vector2d lowerLeft = bounds.lowerLeft();
        Vector2d upperRight = bounds.upperRight();

        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++){
            for (int j = lowerLeft.getY(); j <= upperRight.getY(); j++){
                Vector2d vect = new Vector2d(i, j);
                if (!preferredSet.contains(vect) && map.plantAt(vect) == null){
                    lessPreferred.add(vect);
                }
            }
        }
        return lessPreferred;
    }

    public void generate(int num){
        List<Vector2d> preferred = getPreferredFields();
        List<Vector2d> lessPreferred = getLessPreferredList(preferred);
        Random random = new Random();

        Collections.shuffle(preferred);
        Collections.shuffle(lessPreferred);

        for (int i = 0; i < num; i++){
            Vector2d position;
            if (!preferred.isEmpty() && !lessPreferred.isEmpty()){
                if (random.nextDouble() < 0.8){
                    position = preferred.removeLast();
                }
                else{
                    position = lessPreferred.removeLast();
                }
            }
            else if (!preferred.isEmpty()) position = preferred.removeLast();
            else if (!lessPreferred.isEmpty()) position = lessPreferred.removeLast();
            else { break; }
            map.addPlant(new Plant(position));
        }
    }
}