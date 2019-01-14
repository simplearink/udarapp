package ru.simplearink.udarapp;

import java.util.ArrayList;

public class SingleGameController {
        private ArrayList<SingleResultObject> result;

        SingleGameController() {
            result = new ArrayList<>();
        }

        public void add(SingleResultObject obj) {
            result.add(obj);
        }

        public SingleResultObject get(int index) {
            return result.get(index);
        }

        public SingleResultObject getLast() {return result.get(result.size() - 1); }

        public int size() {
            return result.size();
        }
}
