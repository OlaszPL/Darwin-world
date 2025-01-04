package agh.ics.darwin.model;

public interface MapChangeListener {

    void mapChanged(WorldMap worldMap, String message);

    void setWorldMap(WorldMap map);
}
