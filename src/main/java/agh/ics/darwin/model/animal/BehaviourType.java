package agh.ics.darwin.model.animal;

public enum BehaviourType {
    FULL_PREDESTINATION_BEHAVIOUR,
    A_BIT_OF_CRAZINESS_BEHAVIOUR;

    @Override
    public String toString() {
        return switch (this) {
            case FULL_PREDESTINATION_BEHAVIOUR -> "Full Predestination";
            case A_BIT_OF_CRAZINESS_BEHAVIOUR -> "A Bit of Craziness";
        };
    }
}
