import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Assignment {
    String name, qFileName, aFileName, path;
    DNASeq sequence;
    PolySeq protein;
    Mutation mutation;
    TrueFalse TF;
    LongAnswer LA;
    FileInputStream answersTemplate, questionsTemplate;
    ArrayList<String> TFQ, TFA, LAQ;

    public Assignment(String name, String path) throws IOException, InvalidFormatException {
        this.name = name;
        this.path = path;
        String firstName = name.substring(0, name.indexOf(" "));
        String lastName = name.substring(name.indexOf(" ") + 1);
        qFileName = lastName + ", " + firstName + " - Genetics Assignment.docx";
        aFileName = lastName + ", " + firstName + " - Genetics Assignment ANSWERS.docx";

        questionsTemplate = new FileInputStream(Generator.getStringPath("QTemplate.docx"));
        answersTemplate = new FileInputStream(Generator.getStringPath("ATemplate.docx"));

        sequence = new DNASeq(104);
        protein = new PolySeq(sequence.getRNA());
        mutation = new Mutation(sequence.getSequence(), sequence.getLandmarks());
        TF = new TrueFalse(5);
        LA = new LongAnswer(4);
        TFQ = TF.getQuestions();
        TFA = TF.getAnswers();
        LAQ = LA.getQuestions();

        generateQuestions();
        generateAnswers();
    }

    private void generateQuestions() throws IOException, InvalidFormatException {
        XWPFDocument questions =  new XWPFDocument(OPCPackage.open(questionsTemplate));

        replaceTextFor(questions, "#{NAME}", name);
        replaceTextFor(questions, "#{CODINGSTRAND}", sequence.getTop());
        replaceTextFor(questions, "#{TEMPLATESTRAND}", sequence.getQuestion());
        replaceTextFor(questions, "#{POINTMUTATION}", mutation.getPointMutationQuestion());
        replaceTextFor(questions, "#{DELETIONMUTATION}", mutation.getDeletionMutationQuestion());

        replaceTextFor(questions, "TRUEFALSE0", TFQ.get(0));
        replaceTextFor(questions, "TF1", TFQ.get(1));
        replaceTextFor(questions, "TF2", TFQ.get(2));
        replaceTextFor(questions, "TF3", TFQ.get(3));
        replaceTextFor(questions, "TF4", TFQ.get(4));

        for (int i = 0; i < LAQ.size(); i++)
            replaceTextFor(questions, "LONGANSWER" + i, LAQ.get(i));

        saveWord(path, qFileName, questions);
    }

    private void generateAnswers() throws IOException, InvalidFormatException {
        XWPFDocument answers =  new XWPFDocument(OPCPackage.open(answersTemplate));

        replaceTextFor(answers, "#{NAME}", name);
        replaceTextFor(answers, "#{CODINGSTRAND}", sequence.getTop());
        replaceTextFor(answers, "#{TEMPLATESTRAND}", sequence.getQuestion());
        replaceTextFor(answers, "#{POINTMUTATION}", mutation.getPointMutationQuestion());
        replaceTextFor(answers, "#{DELETIONMUTATION}", mutation.getDeletionMutationQuestion());

        replaceTextFor(answers, "TRUEFALSE0", TFQ.get(0));
        replaceTextFor(answers, "TF1", TFQ.get(1));
        replaceTextFor(answers, "TF2", TFQ.get(2));
        replaceTextFor(answers, "TF3", TFQ.get(3));
        replaceTextFor(answers, "TF4", TFQ.get(4));

        for (int i = 0; i < LAQ.size(); i++)
            replaceTextFor(answers, "LONGANSWER" + i, LAQ.get(i));

        replaceTextFor(answers, "*DNA", "5' " + sequence.getAnswer() + " 3'");
        replaceTextFor(answers, "*RNA", sequence.getRNA());
        replaceTextFor(answers, "*PROTEIN", protein.getPolypeptide());
        replaceTextFor(answers, "q8a", "TATA Box starts at base " + sequence.getPromoterRegion());
        replaceTextFor(answers, "q9a", "Transcription begins at base " + sequence.getTranscriptionStart());
        replaceTextFor(answers, "q10a", "Transcription ends at base " + sequence.getTranscriptionEnd());
        replaceTextFor(answers, "q12a", "Translation begins at base " + sequence.getTranslationStart());
        replaceTextFor(answers, "q14a", "Translation ends at base " + sequence.getTranslationEnd());
        replaceTextFor(answers, "q15a", mutation.getPointMutationAnswer());
        replaceTextFor(answers, "q16a", mutation.getDeletionMutationAnswer());

        replaceTextFor(answers, "TRUEFALSEANSWER0", TFA.get(0));
        replaceTextFor(answers, "TFA1", TFA.get(1));
        replaceTextFor(answers, "TRUEFALSEA2", TFA.get(2));
        replaceTextFor(answers, "TFA3", TFA.get(3));
        replaceTextFor(answers, "TRUEFALSEANSWER4", TFA.get(4));

        saveWord(path, aFileName, answers);
    }

    private static void replaceTextFor(XWPFDocument doc, String findText, String replaceText){
        doc.getParagraphs().forEach(p ->{
            p.getRuns().forEach(run -> {
                String text = run.text();
                if(text.contains(findText)) {
                    run.setText(text.replace(findText, replaceText), 0);
                }
            });
        });
    }

    private static void saveWord(String path, String fileName, XWPFDocument doc){
        try (FileOutputStream out = new FileOutputStream(path + fileName)) {
            doc.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
