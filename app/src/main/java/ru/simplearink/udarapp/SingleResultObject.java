package ru.simplearink.udarapp;

public class SingleResultObject {
    private int wordId;
    private String word;
    private boolean answer;
    private boolean userAnswer;

    SingleResultObject(int newWordId, String newWord, boolean newAnswer, boolean newUserAnswer) {
        wordId = newWordId;
        word = newWord;
        answer = newAnswer;
        userAnswer = newUserAnswer;
    }

    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public boolean getAnswer() {
        return answer;
    }

    public boolean getUserAnswer() {
        return userAnswer;
    }

    public boolean isUserAnswerCorrect() {
        if (answer == userAnswer) return true;
        else return false;
    }
}
