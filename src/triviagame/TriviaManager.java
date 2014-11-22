package triviagame;

import java.util.Scanner;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TriviaManager 
{
      private Map<Type, Map<QuestionDifficulty, List<Question>>> triviaData;
      private Scanner scanner;
      
      public Map<Type, Map<QuestionDifficulty, List<Question>>> getTriviaData() 
    {
        return new HashMap<>(triviaData);
    }
    
    public TriviaManager(List<Object> triviaData)
    {
        this.triviaData = new HashMap();
        this.triviaData.put(MultipleAnswersQuestion.class, new HashMap());
        this.triviaData.put(YesOrNoQuestion.class, new HashMap());
        this.triviaData.put(OpenQuestion.class, new HashMap());
        
        for(Map.Entry<Type, Map<QuestionDifficulty, List<Question>>> entry : this.triviaData.entrySet()) 
        {
            entry.getValue().put(QuestionDifficulty.EASY, new ArrayList());
            entry.getValue().put(QuestionDifficulty.MEDIUM, new ArrayList());
            entry.getValue().put(QuestionDifficulty.HARD, new ArrayList());
        }
        
        CreateTriviaData(triviaData);
        scanner = new Scanner(System.in);
    }

    private void CreateTriviaData(List<Object> triviaData) 
    {
        for (Object currentObject : triviaData)
        {
            AddQuestionToTriviaData(currentObject);
        }
    }
    
    public void AddQuestion(Question questionToAdd)
    {
        if (questionToAdd != null) 
        {
            AddQuestionToTriviaData(questionToAdd); 
        }
    }
    
     public void DeleteQuestion(Question questionToDelete)
    {   
        if (questionToDelete instanceof MultipleAnswersQuestion)
        {
            this.getTriviaData().get(MultipleAnswersQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
        }
        else if (questionToDelete instanceof YesOrNoQuestion)
        {
            this.getTriviaData().get(YesOrNoQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
        }
        else if (questionToDelete instanceof OpenQuestion)
        {
           this.getTriviaData().get(OpenQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
        }
    }

    private void AddQuestionToTriviaData(Object currentObject) 
    {
        if (currentObject instanceof MultipleAnswersQuestion)
        {
            MultipleAnswersQuestion question = (MultipleAnswersQuestion)currentObject;
            this.getTriviaData().get(MultipleAnswersQuestion.class).get(question.getDifficulty()).add(question);
        }
        else if (currentObject instanceof YesOrNoQuestion)
        {
            YesOrNoQuestion question = (YesOrNoQuestion)currentObject;
            this.getTriviaData().get(YesOrNoQuestion.class).get(question.getDifficulty()).add(question);
        }
        else if (currentObject instanceof OpenQuestion)
        {
            OpenQuestion question = (OpenQuestion)currentObject;
            this.getTriviaData().get(OpenQuestion.class).get(question.getDifficulty()).add(question);
        }
    }
    
   
}
