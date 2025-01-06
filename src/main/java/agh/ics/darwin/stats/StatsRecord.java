package agh.ics.darwin.stats;

import java.util.List;

public record StatsRecord(int days, int animalsNumber, int plantsNumber, int freeFields, List<String> mostPopularGenotypes,
                          double averageEnergyLevel, double averageLifespan, double averageNumberOfChildren) {
}