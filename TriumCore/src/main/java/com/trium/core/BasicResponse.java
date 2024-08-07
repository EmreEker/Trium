package com.trium.core;

import java.util.Collections;
import java.util.List;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicResponse<E> {
    private final List<E> elements;
    private final int elementSize;


    public BasicResponse(List<E> items) {
        this.elements = Collections.unmodifiableList(items);
        this.elementSize = items.size();
      
    }

    public List<E> getElements() {
        return elements;
    }

    public int getElementSize() {
        return elementSize;
    }

    

    @Override
    public String toString() {
        return "{" +
                "  \"elements\": " + elements +
                ", \"elementSize\": " + elementSize +
                "}";
    }
}