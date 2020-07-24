import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LongAnswer {
    private int numOfQuestions;
    private ArrayList<String> questionBank = new ArrayList<>();
    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<Integer> questionIndex = new ArrayList<>();

    public LongAnswer (int num) throws FileNotFoundException {
        numOfQuestions = num;
        organizeQuestions();
        genQuestions();
    }

    private void organizeQuestions() throws FileNotFoundException {
        Scanner input = new Scanner(new File(Generator.getStringPath("LongAnswerQuestions.txt")));
        while (input.hasNext())
            questionBank.add(input.nextLine());
    }

    private void genQuestions(){
        Random gen = new Random();
        if (numOfQuestions <= questionBank.size()){
            for (int i = 0; i < numOfQuestions; i++)
                questionIndex.add(gen.nextInt(questionBank.size()));

            for (int x = 0; x < questionBank.size(); x++)
                for (int a = 0; a < questionIndex.size(); a++)
                    for (int b = 0; b < questionIndex.size(); b++)
                        if (questionIndex.get(a).equals(questionIndex.get(b)) && a != b)
                            questionIndex.set(b, gen.nextInt(questionBank.size()));

            for (int i = 0; i < numOfQuestions; i++)
                questions.add(questionBank.get(questionIndex.get(i)));
        }
    }

    public ArrayList<String> getQuestions(){return questions;}
}
