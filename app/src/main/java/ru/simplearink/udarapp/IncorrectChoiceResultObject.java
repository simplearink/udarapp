package ru.simplearink.udarapp;

import java.util.ArrayList;

public class IncorrectChoiceResultObject {
    private int answer;
    private int userAnswer;
    private ArrayList<String> wordsIDs;
    private ArrayList<String> words;
    private int size;
    boolean correctness;

    private String statsWord = "";

    IncorrectChoiceResultObject(int questionSize, ArrayList<String> newWordIds, ArrayList<String> newWords, int answerPos, int userPos, boolean correct) {
        size = questionSize;
        wordsIDs = newWordIds;
        words = newWords;
        answer = answerPos;
        userAnswer = userPos;
        correctness = correct;
    }

    IncorrectChoiceResultObject(String word, int correctPosition, int userPosition) {
        statsWord = word;
        answer = correctPosition;
        userAnswer = userPosition;
        if (answer == userAnswer) {
            correctness = true;
        } else {
            correctness = false;
        }
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

    public int getAnswer() {
        return answer;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public boolean isUserAnswerCorrect() {
        return correctness;
    }

    public void setUserAnswer(int wordPos) {
        userAnswer = wordPos;
    }

    public void setCorrectness(boolean cor) {
        correctness = cor;
    }

    public String getStatsWord() {
        return statsWord;
    }
}
