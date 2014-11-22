package triviagame;

import java.util.List;

public class MultipleAnswersQuestion extends Question {
   
   private String question;
   private String correctAnswer;
   private Category category;
   private List<String> allAnswers;
   private QuestionDifficulty difficulty;
    
   public MultipleAnswersQuestion(String question, 
           List<String> allAnswers, String answer, QuestionDifficulty difficulty, Category category)
   {
       this.question = question;
       this.correctAnswer = answer;
       this.allAnswers = allAnswers;
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
       return correctAnswer;
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
