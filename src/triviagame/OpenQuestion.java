package triviagame;

public class OpenQuestion extends Question {
    
    private String question;
    private String answer;
    private Category category;
    private QuestionDifficulty difficulty;
    
    public OpenQuestion(String question, String answer, QuestionDifficulty difficulty, Category category)
    {
        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;
        this.category = category;
    }
    
    @Override
    String getQuestion() 
    {
        return question;
    }

    @Override
    String getAnswer() 
    {
        return answer;
    }

    @Override
    QuestionDifficulty getDifficulty() 
    {
        return difficulty;
    }
    
    @Override
    Category getCategory() 
    {
        return category;
    }
}
