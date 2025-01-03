package agh.ics.darwin.model;

import java.util.Map;
import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    private static final Vector2d[] vectors = {
            new Vector2d(0, 1),
            new Vector2d(1,1),
            new Vector2d(1,0),
            new Vector2d(1, -1),
            new Vector2d(0,-1),
            new Vector2d(-1,-1),
            new Vector2d(-1,0),
            new Vector2d(-1,1)
    };

    private static final MapDirection[] mapDirectionValues = MapDirection.values();
    private static final Random RANDOM = new Random();

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "↑";
            case NORTHEAST -> "↗";
            case EAST -> "→";
            case SOUTHEAST -> "↘";
            case SOUTH -> "↓";
            case SOUTHWEST -> "↙";
            case WEST -> "←";
            case NORTHWEST -> "↖";
        };
    }

    public MapDirection rotate(int i) {
        if (i < 0 || i > 7)
            throw new IllegalArgumentException("Turn should be an integer in [0,7].");
        return mapDirectionValues[(ordinal() + i) % mapDirectionValues.length];
    }


    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> vectors[0];
            case NORTHEAST -> vectors[1];
            case EAST -> vectors[2];
            case SOUTHEAST -> vectors[3];
            case SOUTH -> vectors[4];
            case SOUTHWEST -> vectors[5];
            case WEST -> vectors[6];
            case NORTHWEST -> vectors[7];
        };
    }

    public MapDirection reverse() {
        return switch (this) {
            case NORTHEAST -> SOUTHEAST;
            case SOUTHEAST -> NORTHEAST;
            case NORTHWEST -> SOUTHWEST;
            case SOUTHWEST -> NORTHWEST;
            case SOUTH -> NORTH;
            case NORTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
        };
    }

    public static MapDirection getRandomDirection() {
        return mapDirectionValues[RANDOM.nextInt(mapDirectionValues.length)];
    }

}
