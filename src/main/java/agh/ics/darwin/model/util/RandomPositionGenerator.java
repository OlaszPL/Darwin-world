package agh.ics.darwin.model.util;

// algorytm Fisher-Yates Shuffle na bieżąco
// w tym przypadku złożoność to O(W*H), gdzie W to szerokość, a H to wysokość mapy.
// dla W, H rzędu sqrt(10 * N) złożoność to O(N)

import agh.ics.darwin.model.Vector2d;

import java.util.Iterator;
import java.util.Random;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth, grassNo, totalPositions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassNo) {
        this.maxWidth = maxWidth;
        this.grassNo = grassNo;
        totalPositions = maxWidth * maxHeight;

        if (grassNo > totalPositions) {
            throw new IllegalArgumentException("Liczba traw przekracza możliwe różne pozycje");
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {

            private final int[] indices; // Tablica z indeksami, które będziemy tasować
            private int currIdx = 0;

            {
                // Inicjalizacja tablicy indeksów
                indices = new int[totalPositions];
                for (int i = 0; i < totalPositions; i++) {
                    indices[i] = i;
                }

                // Tasowanie tablicy
                Random random = new Random();
                for (int i = 0; i < grassNo; i++) {
                    int swapIdx = i + random.nextInt(totalPositions - i);
                    int tmp = indices[i];
                    indices[i] = indices[swapIdx];
                    indices[swapIdx] = tmp;
                }
            }

            @Override
            public boolean hasNext() {
                return currIdx < grassNo;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new IllegalStateException("Brak możliwych następnych pozycji");
                }

                int index = indices[currIdx];
                int x = index % maxWidth;
                int y = index / maxWidth;
                currIdx++;

                return new Vector2d(x, y);
            }
        };
    }
}