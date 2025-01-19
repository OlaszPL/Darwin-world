package agh.ics.darwin.model.animal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DescendantIncrementer {

    public static void incrementDescendantsNum(Animal animal){
        Set<Animal> visited = new HashSet<>();
        List<Animal> queue = new LinkedList<>();

        queue.addLast(animal);

        while (!queue.isEmpty()){
            Animal curr = queue.getFirst();
            queue.removeFirst();

            if (curr.getParents().isPresent()){
                for (Animal parent : curr.getParents().get()){
                    if (!visited.contains(parent)){
                        visited.add(parent);
                        parent.incrementNumberOfDescendants();
                        queue.addLast(parent);
                    }
                }
            }
        }
    }
}
