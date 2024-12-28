package agh.ics.darwin.model;

public enum MapDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    private static final Vector2d[] vectors = {new Vector2d(0, 1), new Vector2d(0, -1),
            new Vector2d(1, 0), new Vector2d(-1, 0)};

    private static final String[] directions = {"N", "S", "E", "W"};

    @Override
    public String toString(){
        return switch (this){
            case NORTH -> directions[0];
            case SOUTH -> directions[1];
            case EAST -> directions[2];
            case WEST -> directions[3];
        };
    }

    public MapDirection next(){
        return switch (this){
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> vectors[0];
            case SOUTH -> vectors[1];
            case EAST -> vectors[2];
            case WEST -> vectors[3];
        };
    }

}
