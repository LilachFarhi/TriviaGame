package triviagame;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TriviaManager {
    
    private Map<Type, Map<QuestionDifficulty, List<Question>>> triviaDataByDifficulty;
    private Map<Type, Map<Category, List<Question>>> triviaDataByCategory;
    
    public TriviaManager(List<Object> triviaData)
    {
        this.triviaDataByDifficulty = new HashMap();
        this.triviaDataByDifficulty.put(MultipleAnswersQuestion.class, new HashMap());
        this.triviaDataByDifficulty.put(YesOrNoQuestion.class, new HashMap());
        this.triviaDataByDifficulty.put(OpenQuestion.class, new HashMap());
        
        this.triviaDataByCategory = new HashMap();
        this.triviaDataByCategory.put(MultipleAnswersQuestion.class, new HashMap());
        this.triviaDataByCategory.put(YesOrNoQuestion.class, new HashMap());
        this.triviaDataByCategory.put(OpenQuestion.class, new HashMap());
        
        for(Entry<Type, Map<QuestionDifficulty, List<Question>>> entry : 
                this.triviaDataByDifficulty.entrySet()) {
            entry.getValue().put(QuestionDifficulty.EASY, new ArrayList());
            entry.getValue().put(QuestionDifficulty.MEDIUM, new ArrayList());
            entry.getValue().put(QuestionDifficulty.HARD, new ArrayList());
        }
        
        for(Entry<Type, Map<Category, List<Question>>> entry : 
                this.triviaDataByCategory.entrySet()) {
            entry.getValue().put(Category.FASHION, new ArrayList());
            entry.getValue().put(Category.HISTORY, new ArrayList());
            entry.getValue().put(Category.MOVIES, new ArrayList());
            entry.getValue().put(Category.MUSIC, new ArrayList());
            entry.getValue().put(Category.SPORTS, new ArrayList());
            entry.getValue().put(Category.TELEVISION, new ArrayList());
        }
        
        CreateTriviaData(triviaData);
    }

    private void CreateTriviaData(List<Object> triviaData) 
    {
        for (Object currentObject : triviaData)
        {
            if (currentObject instanceof MultipleAnswersQuestion)
            {
                MultipleAnswersQuestion question = 
                        (MultipleAnswersQuestion)currentObject;
                triviaDataByDifficulty.get(MultipleAnswersQuestion.class)
                        .get(question.getDifficulty()).add(question);
                triviaDataByCategory.get(MultipleAnswersQuestion.class)
                        .get(question.getCategory()).add(question);
            }
            else if (currentObject instanceof YesOrNoQuestion)
            {
                YesOrNoQuestion question = 
                        (YesOrNoQuestion)currentObject;
                triviaDataByDifficulty.get(YesOrNoQuestion.class)
                        .get(question.getDifficulty()).add(question);
                triviaDataByCategory.get(YesOrNoQuestion.class)
                        .get(question.getCategory()).add(question);
            }
            else if (currentObject instanceof OpenQuestion)
            {
                OpenQuestion question = 
                        (OpenQuestion)currentObject;
                triviaDataByDifficulty.get(OpenQuestion.class)
                        .get(question.getDifficulty()).add(question);
                triviaDataByCategory.get(OpenQuestion.class)
                        .get(question.getCategory()).add(question);
            }
        }
    }

    public Map<Type, Map<QuestionDifficulty, List<Question>>> getTriviaDataByDifficulty() {
        return new HashMap<>(triviaDataByDifficulty);
    }
    
    public Map<Type, Map<Category, List<Question>>> getTriviaDataByCategory() {
        return new HashMap<>(triviaDataByCategory);
    }
    
    public List<Question> getQuestionsByCategory(Category category)
    {
        List<Question> allQuestionsByCategory = new ArrayList();

        for (Map.Entry<Type, Map<Category, List<Question>>> entryType
                : triviaDataByCategory.entrySet()) {
            allQuestionsByCategory.addAll(entryType.getValue().get(category));
        }

        return allQuestionsByCategory;
    }
    
    public List<Question> getQuestionsByQuestionDifficulty(QuestionDifficulty questionDifficulty)
    {
        List<Question> allQuestionsByQuestionDifficulty = new ArrayList();

        for (Map.Entry<Type, Map<QuestionDifficulty, List<Question>>> entryType
                : triviaDataByDifficulty.entrySet()) {
            allQuestionsByQuestionDifficulty.addAll(entryType.getValue().get(questionDifficulty));
        }

        return allQuestionsByQuestionDifficulty;
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
            triviaDataByDifficulty.get(MultipleAnswersQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
            triviaDataByCategory.get(MultipleAnswersQuestion.class).get(questionToDelete.getCategory()).remove(questionToDelete);
        }
        else if (questionToDelete instanceof YesOrNoQuestion)
        {
            triviaDataByDifficulty.get(YesOrNoQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
            triviaDataByCategory.get(YesOrNoQuestion.class).get(questionToDelete.getCategory()).remove(questionToDelete);
        }
        else if (questionToDelete instanceof OpenQuestion)
        {
           triviaDataByDifficulty.get(OpenQuestion.class).get(questionToDelete.getDifficulty()).remove(questionToDelete);
           triviaDataByCategory.get(OpenQuestion.class).get(questionToDelete.getCategory()).remove(questionToDelete);
        }
    }

    private void AddQuestionToTriviaData(Object currentObject) 
    {
        if (currentObject instanceof MultipleAnswersQuestion)
        {
            MultipleAnswersQuestion question = (MultipleAnswersQuestion)currentObject;
            triviaDataByDifficulty.get(MultipleAnswersQuestion.class).get(question.getDifficulty()).add(question);
            triviaDataByCategory.get(MultipleAnswersQuestion.class).get(question.getCategory()).add(question);
        }
        else if (currentObject instanceof YesOrNoQuestion)
        {
            YesOrNoQuestion question = (YesOrNoQuestion)currentObject;
            triviaDataByDifficulty.get(YesOrNoQuestion.class).get(question.getDifficulty()).add(question);
            triviaDataByCategory.get(YesOrNoQuestion.class).get(question.getCategory()).add(question);
        }
        else if (currentObject instanceof OpenQuestion)
        {
            OpenQuestion question = (OpenQuestion)currentObject;
            triviaDataByDifficulty.get(OpenQuestion.class).get(question.getDifficulty()).add(question);
            triviaDataByCategory.get(OpenQuestion.class).get(question.getCategory()).add(question);
        }
    }
}
