package ru.simplearink.udarapp;

import java.util.ArrayList;

public class MultipleGameConroller {
    private ArrayList<MultipleResultObject> result;

    public void MultipleGameController() {
        result = new ArrayList<>();
    }

    public void add(MultipleResultObject obj) {
        result.add(obj);
    }

    public MultipleResultObject get(int index) {
        return result.get(index);
    }
}
