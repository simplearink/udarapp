package ru.simplearink.udarapp;

import java.util.ArrayList;

public class CheckerGameController {
        private ArrayList<CheckerResultObject> result;

        CheckerGameController() {
            result = new ArrayList<>();
        }

        public void add(CheckerResultObject obj) {
            result.add(obj);
        }

        public CheckerResultObject get(int index) {
            return result.get(index);
        }

        public CheckerResultObject getLast() {return result.get(result.size() - 1); }

        public int size() {
            return result.size();
        }

        public void remove(int index) {
            result.remove(index);
        }
}
