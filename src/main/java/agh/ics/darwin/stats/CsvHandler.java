package agh.ics.darwin.stats;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CsvHandler {
    private CSVWriter writer;
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.getDefault());
    static {
        SYMBOLS.setDecimalSeparator(',');
    }
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", SYMBOLS);

    public CsvHandler(String filePath) {
        try{
            writer = new CSVWriter(new FileWriter(filePath, true),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            String[] header = {"Days", "Number of animals", "Number of plants", "Free fields", "Most popular genotypes",
                    "Average energy level", "Average lifespan", "Average number of children"};
            writer.writeNext(header);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void addRecord(StatsRecord statsRecord) {
        String[] record = {
                String.valueOf(statsRecord.days()),
                String.valueOf(statsRecord.animalsNumber()),
                String.valueOf(statsRecord.plantsNumber()),
                String.valueOf(statsRecord.freeFields()),
                statsRecord.mostPopularGenotypes().toString(),
                DECIMAL_FORMAT.format(statsRecord.averageEnergyLevel()),
                DECIMAL_FORMAT.format(statsRecord.averageLifespan()),
                DECIMAL_FORMAT.format(statsRecord.averageNumberOfChildren())
        };
        writer.writeNext(record);
    }

    public void close() {
        try{
            writer.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}