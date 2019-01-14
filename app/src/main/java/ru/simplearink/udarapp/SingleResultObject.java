package ru.simplearink.udarapp;

public class SingleResultObject {
    private int wordId;
    private String word;
    private String answer;
    private boolean userAnswer;

    SingleResultObject(int newWordId, String newWord, String newAnswer, boolean newUserAnswer) {
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

    public String getAnswer() {
        return answer;
    }

    public boolean getUserAnswer() {
        return userAnswer;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setUserAnswer(boolean userAnswer) {
        this.userAnswer = userAnswer;
    }

}