package triviagame;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TriviaGame {

    public static final String TriviaDataFilePath = "src/TriviaData.txt";
    public static final int AddQuestion = 1;
    public static final int RemoveQuestion = 2;
    public static final int Play = 3;
    public static final int Save = 4;

    public static final int OpenQuestion = 1;
    public static final int YesOrNoQuestion = 2;
    public static final int MultipleAnswerQuestion = 3;

    private static final String divider = "-------------------------"
            + "---------------------";
    private static final String welcomeMessage = "Welcome to the Trivia Game!\n"
            + divider;
    private static final String menu = "Press the number of the action you "
            + "would like to "
            + "perform from the menu\n"
            + "1) Add a question\n"
            + "2) Remove a question\n"
            + "3) Play the game\n"
            + "4) Save the data changes\n"
            + "Or you can type 'exit' to exit the game\n"
            + "Press a number from 1 to 4 and then press Enter:";
    private static final String wrongChoice = "The option that you have pressed does "
            + "not exist. Please try again";
    private static final String Exit = "exit";
    private static final String chooseCategory = "Press the number of the category "
            + "you would like to play and then press Enter\n"
            + "At any time type '" + Exit + "' to stop the game";
    private static final String QuestionDisplay = "The question is:";
    private static final String SelectQuestionType = "Please chose the kind of question to add:\n"
             +"Enter 1 for open questions.\n"
             +"Enter 2 for yes or no questions.\n"
             +"Enter 3 for multiple answer questions.";
    private static final String CategoryToAdd = "Press the number of the category that"
            + "you would like to add "; 
    private static final String DifficultyToAdd = "Press the number of question difficulty that "
            + "you would like to add ";

    public static void main(String[] args) {

        try 
        {
            Scanner scanner = new Scanner(System.in);
            FileManager fileManager = new FileManager(TriviaDataFilePath);
            TriviaManager triviaManager = new TriviaManager(fileManager.GetAllDataFromFile());

            System.out.println(welcomeMessage);
            boolean userExited = false;

            while (!userExited) 
            {
                System.out.println(menu);
                String userOption = scanner.nextLine();

                if (userOption.equalsIgnoreCase(Exit))
                {
                    userExited = true;
                    break;
                }

                int option = GetUserValidOption(userOption, scanner);

                if (option == AddQuestion) 
                {
                    Question questionToAdd = AddNewQuestion(scanner);
                    triviaManager.AddQuestion(questionToAdd);
                } else if (option == RemoveQuestion) 
                {
                    Question questionToRemove = RemoveAQuestion(scanner, triviaManager.getTriviaDataByDifficulty());
                    triviaManager.DeleteQuestion(questionToRemove);
                } 
                else if (option == Play) 
                {
                    Play(scanner, triviaManager);
                }
                else if (option == Save) 
                {
                    SaveData(triviaManager, fileManager);
                }
                System.out.println();
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

    private static void Play(Scanner scanner, TriviaManager triviaManager) 
    {
        Category[] allCategories = Category.values();
        PrintCategoryMenu(allCategories);
        String questionsCategory = scanner.nextLine();
        Category category = GetValidCategory(questionsCategory, allCategories, scanner, true);

        if (category != null)
        {
            List<Question> questions = triviaManager.getQuestionsByCategory(category);
            Collections.shuffle(questions);

            for (Question question : questions) 
            {
                String answer = DisplayQuestion(question, scanner);

                if (answer == null || answer.equalsIgnoreCase(Exit)) 
                {
                    break;
                }

                if (answer.equalsIgnoreCase(question.getAnswer())) 
                {
                    System.out.println("Correct Answer!!");
                } 
                else 
                {
                    System.out.println("Wrong! The correct answer is: \n" + question.getAnswer());
                }
            }
            System.out.println("No more questions in this category!");
        }
    }

    private static String DisplayQuestion(Question question, Scanner scanner) 
    {
        String answer = null;

        if (question instanceof YesOrNoQuestion) 
        {
            YesOrNoQuestion questionInstance  = (YesOrNoQuestion) question;
            answer = DisplayYesOrNoQuestion(questionInstance, scanner);
        } 
        else if (question instanceof MultipleAnswersQuestion) 
        {
            MultipleAnswersQuestion questionInstance = (MultipleAnswersQuestion) question;
            answer = DisplayMultipleAnswersQuestion(questionInstance, scanner);
        } 
        else if (question instanceof OpenQuestion) 
        {
            OpenQuestion questionInstance = (OpenQuestion) question;
            answer = DisplayOpenQuestion(questionInstance, scanner);
        }

        return answer;
    }

    private static String DisplayMultipleAnswersQuestion(MultipleAnswersQuestion question, Scanner scanner) 
    {
        System.out.println(QuestionDisplay + question.getQuestion());
        String displayQuestionMessage = "Choose one of the following answers "
                + "(press the number and "
                + "then press enter):\n";

        Object[] allAnswers = question.getAllAnswers().toArray();

        for (int i = 0; i < allAnswers.length; i++) 
        {
            int currentOption = i + 1;
            displayQuestionMessage += currentOption + ") " + allAnswers[i] + "\n";
        }

        System.out.println(displayQuestionMessage);
        String answer = scanner.nextLine();
        int answerNum = Integer.parseInt(ValidateAnswer(answer, allAnswers.length,
                displayQuestionMessage, scanner));
        return (String) (allAnswers[(answerNum - 1)]);
    }

    private static String ValidateAnswer(String answer, int numberOfAnswers,
            String displayQuestionMessage, Scanner scanner) 
    {
        if (answer.equalsIgnoreCase(Exit)) 
        {
            return null;
        }

        boolean answerValid = true;
        int intAnswer;
        try 
        {
            intAnswer = Integer.parseInt(answer);

            if (intAnswer < 1 || intAnswer > numberOfAnswers) 
            {
                answerValid = false;
            } 
            else 
            {
                return answer;
            }
        } 
        catch (NumberFormatException nfe) 
        {
            answerValid = false;
        }

        if (!answerValid) 
        {
            System.out.println(wrongChoice);
            System.out.println(displayQuestionMessage);
            String newAnswer = scanner.nextLine();
            return ValidateAnswer(newAnswer, numberOfAnswers, displayQuestionMessage, scanner);
        }

        return null;
    }

    private static String DisplayYesOrNoQuestion(YesOrNoQuestion question, Scanner scanner) 
    {
        System.out.println(QuestionDisplay + question.getQuestion());
        System.out.println("Press 'T' or 'F' to determine true or false for the question:");
        String answer = scanner.nextLine();
        boolean isOptionValid = false;

        while (!isOptionValid) 
        {
            if (answer.equalsIgnoreCase(Exit)) 
            {
                isOptionValid = true;
                return null;
            } 
            else if (!answer.equalsIgnoreCase("T") && !answer.equalsIgnoreCase("F")) 
            {
                System.out.println(wrongChoice);
                answer = scanner.nextLine();
            } 
            else 
            {
                isOptionValid = true;
            }
        }

        if (answer.equalsIgnoreCase("T")) 
        {
            return Boolean.toString(true);
        } 
        else 
        {
            return Boolean.toString(false);
        }
    }

    private static String DisplayOpenQuestion(OpenQuestion question, Scanner scanner) 
    {
        System.out.println(QuestionDisplay + question.getQuestion());
        System.out.println("Enter the EXACT answer:");
        return scanner.nextLine();
    }

    private static void PrintCategoryMenu(Category[] allCategories) 
    {
        System.out.println(chooseCategory);

        for (Category category : allCategories) 
        {
            int categoryInt = category.ordinal() + 1;
            System.out.println(categoryInt + ")"  + category.name());
        }
    }

    private static Category GetValidCategory(String option, Category[] allCategories, Scanner scanner, boolean isInGame) 
    {
        if (option.equalsIgnoreCase(Exit)) 
        {
            return null;
        }

        boolean optionValid = true;
        int intOption;
        try 
        {
            intOption = Integer.parseInt(option) - 1;

            if (intOption >= allCategories.length || intOption < 0) 
            {
                optionValid = false;
            } 
            else 
            {
                return allCategories[intOption];
            }
        } 
        catch (NumberFormatException nfe) 
        {
            optionValid = false;
        }

        if (!optionValid) 
        {
            System.out.println(wrongChoice);
            if (isInGame) 
            {
              PrintCategoryMenu(allCategories);  
            }
            else
            {
                PrintAllCategoriesToAdd(allCategories);
            }
            
            String userOption = scanner.nextLine();
            return GetValidCategory(userOption, allCategories, scanner, isInGame);
        }

        return allCategories[0];
    }

    private static void SaveData(TriviaManager triviaManager, FileManager fileManager)
            throws IOException
    {
        List<Object> allQuestions = GetAllQuestionsForSave(triviaManager.getTriviaDataByDifficulty());
        fileManager.WriteAllDataToFile(allQuestions);
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

            if (intOption != AddQuestion && intOption != RemoveQuestion
                    && intOption != Play && intOption != Save) 
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
            return GetUserValidOption(userOption, scanner);
        }

        return 0;
    }

    private static List<Object> GetAllQuestionsForSave(Map<Type, Map<QuestionDifficulty, List<Question>>> triviaData) 
    {
        List<Object> allQuestions = new ArrayList();

        for (Map.Entry<Type, Map<QuestionDifficulty, List<Question>>> entryType : triviaData.entrySet()) 
        {
            for (Map.Entry<QuestionDifficulty, List<Question>> entry : entryType.getValue().entrySet()) 
            {
                allQuestions.addAll(entry.getValue());
            }
        }

        return allQuestions;
    }

    private static Question AddNewQuestion(Scanner scanner) 
    {
        Question questionToAdd = null;
        System.out.println(SelectQuestionType);
        String userOption = scanner.nextLine();
        int questionTypeToAdd = GetQuestionType(userOption, scanner);

        Category category = GetCategory(scanner ,Category.values());

        QuestionDifficulty difficulty = GetQuestionDifficulty(scanner, QuestionDifficulty.values());

        System.out.println("Please enter the question to add");
        String questionString = scanner.nextLine();

        if (questionTypeToAdd == OpenQuestion) 
        {
            questionToAdd = CreateOpenQuestion(questionString, difficulty, category, scanner);
        }
        else if (questionTypeToAdd == YesOrNoQuestion) 
        {
            
            questionToAdd = CreateYesOrNoQuestion(questionString, difficulty, category, scanner);
        } 
        else if (questionTypeToAdd == MultipleAnswerQuestion) 
        {
            questionToAdd = CreateMultipleAnswerQuestion(questionString, difficulty, category, scanner);
        }
        return questionToAdd;
    }

    private static Category GetCategory(Scanner scanner, Category[] allCategories) 
    {
        PrintAllCategoriesToAdd(allCategories);
        String category = scanner.nextLine();
        return GetValidCategory(category, allCategories, scanner, false);
    }

    private static void PrintAllCategoriesToAdd(Category[] allCategories) 
    { 
        System.out.println(CategoryToAdd);
        
        for (Category category : allCategories)
        {
            int categoryInt = category.ordinal() + 1;
            System.out.println(categoryInt + ")"  + category.name());
        }
    }
    
    private static QuestionDifficulty GetQuestionDifficulty(Scanner scanner, QuestionDifficulty[] allDifficulties)
    {
        PrintQuestionDifficutltyOptionsToAdd(allDifficulties);
        
        String questionDifficulty = scanner.nextLine();
        return GetValidDifficulty(questionDifficulty, allDifficulties, scanner);
    }

    private static void PrintQuestionDifficutltyOptionsToAdd(QuestionDifficulty[] allDifficulties) 
    {
        System.out.println(DifficultyToAdd);
        
        for (QuestionDifficulty difficulty : allDifficulties) 
        {
            int difficultyInt = difficulty.ordinal() + 1;
            System.out.println(difficultyInt + ")"  + difficulty.name());
        }
    }
    
     private static QuestionDifficulty GetValidDifficulty(String option, QuestionDifficulty[] allDifficulties, Scanner scanner) 
    {
        boolean optionValid = true;
        int intOption;
        try 
        {
            intOption = Integer.parseInt(option) - 1;

            if (intOption >= allDifficulties.length || intOption < 0) 
            {
                optionValid = false;
            } 
            else 
            {
                return allDifficulties[intOption];
            }
        } 
        catch (NumberFormatException nfe) 
        {
            optionValid = false;
        }

        if (!optionValid) 
        {
            System.out.println(wrongChoice);
            PrintQuestionDifficutltyOptionsToAdd(allDifficulties);
            String userOption = scanner.nextLine();
            return GetValidDifficulty(userOption, allDifficulties, scanner);
        }

        return allDifficulties[0];
    }
     
    private static int GetQuestionType(String option, Scanner scanner) 
    {
        boolean optionValid = true;
        int intOption;
        try 
        {
            intOption = Integer.parseInt(option);

            if (intOption != OpenQuestion && intOption != YesOrNoQuestion && intOption != MultipleAnswerQuestion ) 
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
            System.out.println(SelectQuestionType);
            String userOption = scanner.nextLine();
            return GetUserValidOption(userOption, scanner);
        }

        return 0;
    }

    private static OpenQuestion CreateOpenQuestion(String questionString, QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.nextLine();

        OpenQuestion openQuestion = new OpenQuestion(questionString, questionAnswer, difficulty, category);
        return openQuestion;
    }

    private static YesOrNoQuestion CreateYesOrNoQuestion(String questionString, QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.nextLine();
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

    private static MultipleAnswersQuestion CreateMultipleAnswerQuestion(String questionString, QuestionDifficulty difficulty, Category category, Scanner scanner) 
    {
        System.out.println("Please enter the answer to the question");
        String questionAnswer = scanner.nextLine();
        List<String> allAnswers = new ArrayList<>();
        allAnswers.add(questionAnswer);
        System.out.println("Please enter a wrong answer to the question or end to finish");
        String answer = scanner.nextLine();

        while (!answer.equals("end")) 
        {
            allAnswers.add(answer);
            System.out.println("Please enter a wrong answer to the question or end to finish");
            answer = scanner.nextLine();
        }

        Collections.shuffle(allAnswers);
        MultipleAnswersQuestion multipleAnswerQuestion = new MultipleAnswersQuestion(questionString, allAnswers, questionAnswer, difficulty, category);
        return multipleAnswerQuestion;
    }

    private static Question RemoveAQuestion(Scanner scanner, Map<Type, Map<QuestionDifficulty, List<Question>>> triviaData) 
    {
        List<Map<QuestionDifficulty, List<Question>>> list = new ArrayList<>(triviaData.values());
        List<List<Question>> allListQuestions = new ArrayList<>();
        for (Map<QuestionDifficulty, List<Question>> list1 : list) 
        {
            allListQuestions.addAll(list1.values());
        }
        //list.stream().forEach((currentMap) -> { allListQuestions.addAll(currentMap.values());});
        List<Question> allQuestions = new ArrayList<>();
        for (List<Question> allListQuestion : allListQuestions) 
        {
            allQuestions.addAll(allListQuestion);
        }
        // allListQuestions.stream().forEach((allListQuestion) -> {allQuestions.addAll(allListQuestion);});

        for (int i = 0; i < allQuestions.size(); i++) 
        {
            int number = i + 1;
            System.out.println(number + ") " + allQuestions.get(i).getQuestion());
        }

        System.out.println("Please enter a question number to delete");
        int questionNumnber = scanner.nextInt();
        scanner.nextLine();
        return allQuestions.get(questionNumnber--);
    }
}
