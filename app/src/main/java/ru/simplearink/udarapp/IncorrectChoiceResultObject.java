package ru.simplearink.udarapp;

import java.util.ArrayList;

public class IncorrectChoiceResultObject {
    private String answer;
    private String userAnswer;
    private ArrayList<String> wordsIDs;
    private ArrayList<String> words;
    private int size;

    IncorrectChoiceResultObject(int questionSize, ArrayList<String> newWordIds, ArrayList<String> newWords, String newAnswer, String newUserAnswer) {
        size = questionSize;
        wordsIDs = newWordIds;
        words = newWords;
        answer = newAnswer;
        userAnswer = newUserAnswer;
    }


    public ArrayList<String> getWordsIDs() {
        return wordsIDs;
    }

    public String getWordID(int index) {
        return wordsIDs.get(index);
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public String getWord(int index) {
        return words.get(index);
    }

    public int getSize() {
        return size;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isUserAnswerCorrect() {
        if (answer.equals(userAnswer)) return true;
        else return false;
    }

    public void setUserAnswer(String wordID) {
        userAnswer = wordID;
    }
}
