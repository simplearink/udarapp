package ru.simplearink.udarapp;

import java.util.ArrayList;

public class IncorrectChoiceGameController {
    private ArrayList<IncorrectChoiceResultObject> result;

    IncorrectChoiceGameController() {
        result = new ArrayList<>();
    }

    public int getSize() {
        return result.size();
    }

    public void add(IncorrectChoiceResultObject obj) {
        result.add(obj);
    }

    public IncorrectChoiceResultObject get(int index) {
        return result.get(index);
    }
}
