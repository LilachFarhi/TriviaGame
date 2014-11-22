package triviagame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.lang.reflect.Type;

public class TriviaGame {

    public static final String TriviaDataFilePath = "C:\\Temp\\New.txt";
    public static final int AddQuestion = 1;
    public static final int RemoveQuestion = 2;
    public static final int Play = 3;
    public static final int Save = 4;
    public static final int OpenQuestion = 1;
    public static final int YesOrNoQuestion = 2;
    public static final int MultipleAnswerQuestion = 3;
    
    private static final String divider = "-------------------------" +
            "---------------------";
    private static final String welcomeMessage = "Welcome to the Trivia Game!" + 
            divider;
    private static final String menu = "Press the number of the action you" +
            "would like to "
                + "perform from the menu\n"
                + "1) Add a question\n"
                + "2) Remove a question\n"
                + "3) Play the game\n"
                + "4) Save the data changes\n"
                + "Press a number from 1 to 4 and then press Enter:\n"; 
    private static final String wrongChoice = "The option that you have pressed does "
                + "not exist. Please try again\n" + menu;

    public static void main(String[] args) 
    {
        try 
        {
            Scanner scanner = new Scanner(System.in);
            FileManager fileManager = new FileManager(TriviaDataFilePath);
            TriviaManager triviaManager = new TriviaManager(fileManager.GetAllDataFromFile());

            System.out.println(welcomeMessage);
            System.out.println(menu);
            String userOption = scanner.nextLine();
            int option = GetUserValidOption(userOption, scanner);
            
            if (option == AddQuestion)
            {
                Question questionToAdd = AddNewQuestion(scanner);
                triviaManager.AddQuestion(questionToAdd);
            }
            else if (option == RemoveQuestion)
            {
                Question questionToRemove = RemoveQuestion(scanner, triviaManager.getTriviaData());
            }
            else if (option == Play)
            {
                
            }
            else if (option == Save)
            {
                
            }
            
        } 
        catch (IOException ex) 
        {
            ShowErrorMessage(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            ShowErrorMessage(ex);
        }
    }

    private static void ShowErrorMessage(Exception ex) 
    {
        System.out.println("An error occurred while trying to connect "
                + "to the trivia data file : \'" + TriviaDataFilePath
                + "\'.\n"
                + "Please check the file location or contact "
                + "the system administrator.\n"
                + "Error data : " + ex);
    }

    private static int GetUserValidOption(String option, Scanner scanner) 
    {
        boolean optionValid = true;
        int intOption;
        try 
        {
            intOption = Integer.parseInt(option);
            
            if (intOption != AddQuestion && intOption != RemoveQuestion &&
                    intOption != Play && intOption != Save)
            {
                optionValid = false;
            }
            else
            {
                return intOption;
            }
        } 
        catch (NumberFormatException nfe)
        {
            optionValid = false;
        }
        
        if (!optionValid)
        {
            System.out.println(wrongChoice);
            System.out.println(menu);
            String userOption = scanner.nextLine();
            GetUserValidOption(userOption, scanner);
        }
        
        return 0;
    }
    
    private static Question AddNewQuestion(Scanner scanner)
    {
        Question questionToAdd = null;
        System.out.println("Please chose the kind of question to add:");
        System.out.println("Enter 1 for open questions.");
        System.out.println("Enter 2 for yes or no questions.");
        System.out.println("Enter 3 for multiple answer questions.");
        int questionTypeToAdd = scanner.nextInt();
        
        System.out.println("Please chose the category of the question");
        System.out.println("Enter 1 for Fashion.");
        System.out.println("Enter 2 for History.");
        System.out.println("Enter 3 for Movies.");
        System.out.println("Enter 4 for Music.");
        System.out.println("Enter 5 for Sports.");
        System.out.println("Enter 6 for Television.");
        int questionCategory = scanner.nextInt();
        Category category = Category.values()[questionCategory-1];
        
        System.out.println("Please enter the question's difficulty");
        System.out.println("Enter 1 for Easy.");
        System.out.println("Enter 2 for Medium.");
        System.out.println("Enter 3 for Hard.");
        int questionDifficulty = scanner.nextInt();
        QuestionDifficulty difficulty = QuestionDifficulty.values()[questionDifficulty-1];
        
        System.out.println("Please enter the question to add");
        String questionString = scanner.next();
        
        if(questionTypeToAdd == OpenQuestion)
        {
            questionToAdd = CreateOpenQuestion(questionString, difficulty, category, scanner);
        }
        else if(questionTypeToAdd == YesOrNoQuestion)
        {
            questionToAdd = CreateYesOrNoQuestion(questionString, difficulty, category, scanner);
        }
        else if(questionTypeToAdd == MultipleAnswerQuestion)
        {
            questionToAdd = CreateMultipleAnswerQuestion(questionString, difficulty, category, scanner);
        }
        return questionToAdd;
    }
    
    private static OpenQuestion CreateOpenQuestion(String questionString,  QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.next();
        
        OpenQuestion openQuestion = new OpenQuestion(questionString, questionAnswer, difficulty,category);
        return openQuestion;
    }

    private static YesOrNoQuestion CreateYesOrNoQuestion(String questionString,  QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.next();
        boolean answer = false;
        
        if (questionAnswer.equals("yes") || questionAnswer.equals("true")) 
        {
            answer = true;
        }
        if (questionAnswer.equals("no") || questionAnswer.equals("false")) 
        {
            answer = false;
        }
        
        YesOrNoQuestion yesOrNoQuestion = new YesOrNoQuestion(questionString, answer, difficulty, category);
        return yesOrNoQuestion;
    }

    private static MultipleAnswersQuestion CreateMultipleAnswerQuestion(String questionString,  QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.next();
        List<String> allAnswers = new ArrayList<>();
        allAnswers.add(questionAnswer);
        System.out.println("Please enter a wrong answer to the question or end to finish");
        String answer = scanner.next();
        
        while(!answer.equals("end"))
        {
            allAnswers.add(answer);
            System.out.println("Please enter a wrong answer to the question or end to finish");
            answer = scanner.next();
        }
        
        Collections.shuffle(allAnswers);
        MultipleAnswersQuestion multipleAnswerQuestion = new MultipleAnswersQuestion(questionString, allAnswers, answer, difficulty, category);
        return multipleAnswerQuestion;
    }
    
    private static Question RemoveQuestion(Scanner scanner, Map<Type, Map<QuestionDifficulty, List<Question>>> triviaData)
    {
        List<Map<QuestionDifficulty, List<Question>>> list = new ArrayList<>(triviaData.values());
        List<List<Question>> allListQuestions = new ArrayList<>();
        list.stream().forEach((currentMap) -> { allListQuestions.addAll(currentMap.values());});
        List<Question> allQuestions = new ArrayList<>();
        allListQuestions.stream().forEach((allListQuestion) -> {allQuestions.addAll(allListQuestion);});
         
        for(int i = 0; i < allQuestions.size(); i++)
        {
            int number = i + 1;
            System.out.println(number + ":" + allQuestions.get(i).getQuestion());
        }
        
        System.out.println("Please enter a question number to delete");
        int questionNumnber = scanner.nextInt();
        
        return allQuestions.get(questionNumnber--);
    }
}
