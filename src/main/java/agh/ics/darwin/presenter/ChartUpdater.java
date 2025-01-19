package agh.ics.darwin.presenter;

import agh.ics.darwin.stats.StatsRecord;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChartUpdater {
    private final XYChart.Series<Number, Number> animalsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> plantsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> freeFieldsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageEnergySeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageLifeSpan = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> averageChildrenNumber = new XYChart.Series<>();

    public void initializeCharts(LineChart<Number, Number> animalsChart,
                                 LineChart<Number, Number> energyChart) {
        setupChart(animalsChart);
        setupChart(energyChart);

        animalsChart.getData().addAll(List.of(animalsSeries, plantsSeries, freeFieldsSeries));
        energyChart.getData().addAll(List.of(averageEnergySeries, averageLifeSpan, averageChildrenNumber));

        setupSeriesNames();
    }

    private void setupChart(LineChart<Number, Number> chart) {
        chart.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/chart-styles.css")).toExternalForm()
        );
        chart.setCreateSymbols(false);
    }

    private void setupSeriesNames() {
        animalsSeries.setName("Animals");
        plantsSeries.setName("Plants");
        freeFieldsSeries.setName("Free Fields");
        averageEnergySeries.setName("Energy");
        averageLifeSpan.setName("Lifespan");
        averageChildrenNumber.setName("Children");
    }

    public void updateCharts(StatsRecord stats, TextArea popularGenotypesLabel) {
        updatePopulationSeries(stats);
        updateAverageSeries(stats);
        popularGenotypesLabel.setText("Most Popular Genotypes: " + stats.mostPopularGenotypes());
    }

    private void updatePopulationSeries(StatsRecord stats) {
        addDataToSeries(animalsSeries, stats.days(), stats.animalsNumber(), "Animals");
        addDataToSeries(plantsSeries, stats.days(), stats.plantsNumber(), "Plants");
        addDataToSeries(freeFieldsSeries, stats.days(), stats.freeFields(), "Free fields");
    }

    private void updateAverageSeries(StatsRecord stats) {
        double avgEnergy = formatDouble(stats.averageEnergyLevel());
        double avgLifespan = formatDouble(stats.averageLifespan());
        double avgChildren = formatDouble(stats.averageNumberOfChildren());

        addDataToSeries(averageEnergySeries, stats.days(), avgEnergy, "Energy");
        addDataToSeries(averageLifeSpan, stats.days(), avgLifespan, "Lifespan");
        addDataToSeries(averageChildrenNumber, stats.days(), avgChildren, "Children");
    }

    private void addDataToSeries(XYChart.Series<Number, Number> series,
                                 int day, double value, String name) {
        series.getData().add(new XYChart.Data<>(day, value));
        series.setName(name + "\n" + value);
    }

    private double formatDouble(double value) {
        return Double.parseDouble(String.format(Locale.US, "%.2f", value));
    }
}
