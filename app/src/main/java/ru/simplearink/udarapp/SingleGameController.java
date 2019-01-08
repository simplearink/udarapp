package ru.simplearink.udarapp;

import java.util.ArrayList;

public class SingleGameController {
        private ArrayList<SingleResultObject> result;


        public void SingleGameController() {
            result = new ArrayList<>();
        }

        public void add(SingleResultObject obj) {
            result.add(obj);
        }

        public SingleResultObject get(int index) {
            return result.get(index);
        }
}
