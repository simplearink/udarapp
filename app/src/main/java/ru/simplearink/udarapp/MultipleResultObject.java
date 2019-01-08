package ru.simplearink.udarapp;

public class MultipleResultObject {
    private String answer;
    private String userAnswer;

    MultipleResultObject(int newWordId, String newWord, String newAnswer, String newUserAnswer) {
        answer = newAnswer;
        userAnswer = newUserAnswer;
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
}
