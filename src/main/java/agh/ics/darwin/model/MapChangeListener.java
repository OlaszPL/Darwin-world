package agh.ics.darwin.model;

import agh.ics.darwin.stats.StatsRecord;

public interface MapChangeListener {

    void mapChanged(WorldMap worldMap, String message);
    void updateStats(StatsRecord statsRecord);
}
