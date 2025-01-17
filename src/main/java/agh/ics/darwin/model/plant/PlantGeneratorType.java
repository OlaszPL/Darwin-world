package agh.ics.darwin.model.plant;

public enum PlantGeneratorType {
    EQUATORIAL_FOREST,
    CREEPING_JUNGLE;

    @Override
    public String toString(){
        return switch(this){
            case EQUATORIAL_FOREST -> "Equatorial Forest";
            case CREEPING_JUNGLE -> "Creeping Jungle";
        };
    }

}