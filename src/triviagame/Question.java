package triviagame;

import java.io.Serializable;

public abstract class Question implements Serializable {
    
    abstract String getQuestion();
    abstract String getAnswer();
    abstract Category getCategory();
    abstract QuestionDifficulty getDifficulty();
}