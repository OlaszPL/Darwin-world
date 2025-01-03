package agh.ics.darwin.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomMutateGenePositionGenerator implements Iterable<Integer> {
    private int number_of_mutations;
    private final List<Integer> genesIndexes;

    public RandomMutateGenePositionGenerator(int genomeLength, int number_of_mutations){
        this.number_of_mutations = number_of_mutations;
        genesIndexes = new ArrayList<>();
        for (int i=0; i<genomeLength; i++){
            genesIndexes.add(i);
        }
        Collections.shuffle(genesIndexes);
    }

    @Override
    public Iterator<Integer> iterator(){
        return new Iterator<>(){
            @Override
            public boolean hasNext() {
                return number_of_mutations != 0;
            }

            @Override
            public Integer next() {
                return genesIndexes.get(--number_of_mutations);
            }
        };
    }

}
