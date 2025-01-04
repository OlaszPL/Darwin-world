package agh.ics.darwin.model.util;

import agh.ics.darwin.model.MapChangeListener;
import agh.ics.darwin.model.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.printf("MapID: %s%n", worldMap.getId());
            System.out.printf("Update Number: %d -> %s%n", ++updateCount, message);
            System.out.println(worldMap);
        }
    }
}