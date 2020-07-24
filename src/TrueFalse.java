import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TrueFalse {
    private int numOfQuestions;
    private ArrayList<String> questionBank = new ArrayList<>();
    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<Integer> questionIndex = new ArrayList<>();
    private ArrayList<String> answerBank = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();

    public TrueFalse (int num) throws FileNotFoundException {
        numOfQuestions = num;
        organizeQuestions();
        genQuestions();
    }

    private void organizeQuestions() throws FileNotFoundException {
        Scanner input = new Scanner(new File(Generator.getStringPath("TrueFalseQuestions.txt")));
        input.useDelimiter("[\t\n]");
        while (input.hasNext()){
            questionBank.add(input.next());
            answerBank.add(input.next());
        }
    }

    private void genQuestions(){
        Random gen = new Random();
        int tCount = 0;
        while (tCount != (numOfQuestions / 2)) {
            questionIndex.clear();
            questions.clear();
            answers.clear();
            tCount = 0;

            if (numOfQuestions <= questionBank.size()){
                for (int i = 0; i < numOfQuestions; i++)
                    questionIndex.add(gen.nextInt(questionBank.size()));

                for (int x = 0; x < questionBank.size(); x++)
                    for (int a = 0; a < questionIndex.size(); a++)
                        for (int b = 0; b < questionIndex.size(); b++)
                            if (questionIndex.get(a).equals(questionIndex.get(b)) && a != b)
                                questionIndex.set(b, gen.nextInt(questionBank.size()));

                for (int i = 0; i < numOfQuestions; i++) {
                    questions.add(questionBank.get(questionIndex.get(i)));
                    answers.add(answerBank.get(questionIndex.get(i)));
                    if (answers.get(i).equals("T"))
                        tCount++;
                }
            }
        }
    }

    public ArrayList<String> getQuestions(){return questions;}

    public ArrayList<String> getAnswers(){return answers;}

}