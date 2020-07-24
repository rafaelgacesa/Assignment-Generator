import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        DNASeq sequence = new DNASeq(100);
        System.out.println();

        String codingStrand = "5' " + sequence.getTop() + " 3'";
        String templateStrand = "3' " + sequence.getBottom() + " 5'";
        String rna = "   " + sequence.getRNA() + "   ";
        String DNAQuestion = "3' " + sequence.getQuestion() + " 5'";
        String DNAAnswer = "3' " + sequence.getAnswer() + " 5'";

        System.out.println("DNA: " + codingStrand);
        System.out.println("DNA: " + templateStrand);
        System.out.println("RNA: " + rna);
        System.out.println();

        System.out.println("Transcription begins at base " + sequence.getTranscriptionStart());
        System.out.println("Transcription ends at base " + sequence.getTranscriptionEnd());
        System.out.println("Translation begins at base " + sequence.getTranslationStart());
        System.out.println("Translation ends at base " + sequence.getTranslationEnd());
        System.out.println();

        System.out.println("Fill in the missing blank, indicate direction: ");
        System.out.println(codingStrand);
        System.out.println(DNAQuestion);
        System.out.println("Answer:");
        System.out.println(DNAAnswer);
        System.out.println();

        PolySeq protein = new PolySeq(rna);
        System.out.println("Polypeptide Sequence: ");
        String polypeptide = protein.getPolypeptide();
        System.out.println(polypeptide);
        System.out.println();

        Mutation mutation = new Mutation(sequence.getSequence(), sequence.getLandmarks());
        System.out.println(mutation.getPointMutationQuestion());
        System.out.println(mutation.getPointMutationAnswer());
        System.out.println();
        System.out.println(mutation.getDeletionMutationQuestion());
        System.out.println(mutation.getDeletionMutationAnswer());


    }
}
