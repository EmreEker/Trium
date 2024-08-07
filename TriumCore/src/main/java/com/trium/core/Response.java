package com.trium.core;

import java.util.Collections;
import java.util.List;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Response<E> {
    private final List<E> elements;
    private final int elementSize;
    private final Map<E, Double> scores;

    public Response(List<E> items, Map<E, Double> scores) {
        this.elements = Collections.unmodifiableList(items);
        this.elementSize = items.size();
        this.scores = scores;
    }

    public List<E> getElements() {
        return elements;
    }

    public int getElementSize() {
        return elementSize;
    }

    public Map<E, Double> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "{" +
                "  \"elements\": " + elements +
                ", \"elementSize\": " + elementSize +
                ", \"scores\": " + scores +
                "}";
    }
}