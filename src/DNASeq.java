import java.util.ArrayList;
import java.util.Random;

public class DNASeq {
    private String top, bottom, rna, question, answer;
    private ArrayList<Integer> sequence = new ArrayList<>();
    private ArrayList<Integer> landmarks = new ArrayList<>(); // stores important indices (length, TATA, transcription end, start codon, end codon)

    public DNASeq(int length){
        Random gen = new Random();

        // creating the original sequence
        for (int i = 0; i < length; i++) {
            sequence.add(gen.nextInt(4));
        }

        // creating the landmarks for this DNA
        landmarks.add(length);
        landmarks.add(gen.nextInt(8) + 2); // TATA Box start
        landmarks.add(length - (gen.nextInt(11) + 5)); // Transcription end
        landmarks.add(landmarks.get(1) + (gen.nextInt(6) + 30)); // translation start (start codon AUG)
        landmarks.add(landmarks.get(2) - (gen.nextInt(6) + 10)); // translation end (stop codon)
        while ((landmarks.get(4) - landmarks.get(3)) % 3 != 0) // making sure codon fits codon format
            landmarks.set(4, landmarks.get(2) - (gen.nextInt(6) + 10));

        // adding regions
        addRegions();

        // checking sequence for extra regions
        checkRegions();

        // generating DNA
        top = generateCodeDNA(sequence);
        bottom = generateTempDNA(sequence);

        // generating RNA
        rna = generateRNA(sequence, landmarks);

        // creating the DNA Sequence Question
        generateDNAQuestion();
    }

    private void addRegions(){
        Random gen = new Random();

        // Promoter (2-10) TATA = 2020
        int[] TATA = new int[]{2,0,2,0};
        for (int i = 0; i < 4; i++)
            sequence.set(i + landmarks.get(1), TATA[i]);

        // Transcription End (15 before end - 5 before end) AATAAA = 002000
        int[] transcription = new int[]{0,0,2,0,0,0};
        for (int i = 0; i < 6; i++)
            sequence.set(landmarks.get(2) - (6-i), transcription[i]);

        // Start Codon Translation (30 - 35 after TATA) ATG = 023
        int[] startCodon = new int[]{0,2,3};
        for (int i = 0; i < 3; i++)
            sequence.set(i + landmarks.get(3), startCodon[i]);

        // End Codon Translation (10 - 15 before transcription end) TAG = 203, TAA = 200, TGA = 230
        int[][] endCodon = new int[][]{
                {2, 0, 3},
                {2, 0, 0},
                {2, 3, 0}
        };
        int chosenEndCodon = gen.nextInt(3);
        for (int i = 0; i < 3; i++)
            sequence.set(i + landmarks.get(4), endCodon[chosenEndCodon][i]);

    }

    private void checkRegions(){
        Random gen = new Random();

        // making sure there are no early TATA boxes
        for (int i = 0; i < landmarks.get(1); i++){
            if (sequence.get(i) == 2)
                if (sequence.get(i + 1) == 0){
                    sequence.set(i + 1, gen.nextInt(3) + 1);
                    System.out.println("possible early tata removed");
                }
        }

        // making sure there are no late TATA boxes
        for (int i = landmarks.get(1) + 4; i < (landmarks.get(0) - 3); i++) {
            if (sequence.get(i) == 2)
                if (sequence.get(i + 1) == 0)
                    if (sequence.get(i + 2) == 2)
                        if (sequence.get(i + 3) == 0) {
                            int x = 0;
                            while (x == 0)
                                x = gen.nextInt(4);
                            sequence.set(i + 1, x);
                            System.out.println("late tata removed");
                        }
        }

        // making sure there are no early start codons
        for (int i = landmarks.get(1); i < landmarks.get(3); i++) {
            if (sequence.get(i) == 0)
                if (sequence.get(i + 1) == 2)
                    if (sequence.get(i + 2) == 3) {
                        int x = 2;
                        while (x == 2)
                            x = gen.nextInt(4);
                        sequence.set(i + 1, x);
                        System.out.println("extra start codon removed");
                    }
        }

        // making sure there are no early stop codons
        for (int i = landmarks.get(3); i < landmarks.get(4) - 1; i+=3){
            int codon = (sequence.get(i) * 100) + (sequence.get(i + 1) * 10) + sequence.get(i + 2);
            if (codon == 200 || codon == 203 || codon == 230) {
                sequence.set(i + 1, 1);
                System.out.println("extra stop codon removed");
            }
        }

        // making sure there are no early transcription endings
        for (int i = landmarks.get(1) + 25; i < (landmarks.get(2) - 6); i++){
            if (sequence.get(i) == 0)
                if (sequence.get(i + 1) == 0)
                    if (sequence.get(i + 2) == 2)
                        if (sequence.get(i + 3) == 0)
                            if (sequence.get(i + 4) == 0)
                                if (sequence.get(i + 5) == 0){
                                    int x = 2;
                                    while (x == 2)
                                        x = gen.nextInt(4);
                                    sequence.set(i + 2, x);
                                    System.out.println("extra aataaa removed");
                                }
        }
    }

    public static String generateCodeDNA(ArrayList<Integer> sequence){
        char[] DNAOptions = new char[]{'A', 'C', 'T', 'G'};
        String dna = "";

        for (int i : sequence){
            dna = dna.concat(String.valueOf(DNAOptions[i]));
        }

        return dna;
    }

    public static String generateTempDNA(ArrayList<Integer> sequence){
        char[] DNAComplementary = new char[]{'T', 'G', 'A', 'C'};
        String dna = "";

        for (int i : sequence){
            dna = dna.concat(String.valueOf(DNAComplementary[i]));
        }

        return dna;
    }

    public static String generateRNA(ArrayList<Integer> sequence, ArrayList<Integer> landmarks){
        char[] RNAComplementary = new char[]{'A', 'C', 'U', 'G'};
        String rna = "";

        for (int i : sequence){
            rna = rna.concat(String.valueOf(RNAComplementary[i]));
        }
        rna = rna.substring(landmarks.get(1) + 25, landmarks.get(2));

        return rna;
    }

    private void generateDNAQuestion(){
        Random gen = new Random();
        int x = gen.nextInt(landmarks.get(0) - (2 * (landmarks.get(0)) / 5)) + (landmarks.get(0) / 5);

        question = bottom.substring(0, x) + "          " + bottom.substring(x + 10);

        answer = bottom.substring(x, x + 10);
        for (int i = 0; i < x; i++)
            answer = " ".concat(answer);
        for (int i = landmarks.get(0); i > (x + 10); i--)
            answer = answer.concat(" ");

    }

    public String getRNA(){
        for (int i = 0; i < landmarks.get(1) + 25; i++){
            rna = " ".concat(rna);
        }

        for (int i = landmarks.get(2); i < landmarks.get(0); i++){
            rna = rna.concat(" ");
        }
        return rna;
    }

    public String getTop(){return top;}

    public String getBottom(){return bottom;}

    public String getQuestion(){return question;}

    public String getAnswer(){
        StringBuilder temp = new StringBuilder(answer);
        temp.reverse();
        return String.valueOf(temp);
    }

    public ArrayList<Integer> getSequence(){return sequence;}

    public ArrayList<Integer> getLandmarks(){return landmarks;}

    public int getPromoterRegion(){return landmarks.get(1) + 1;}

    public int getTranscriptionStart(){return (landmarks.get(1) + 1) + 25;}

    public int getTranscriptionEnd(){return landmarks.get(2);}

    public int getTranslationStart(){return landmarks.get(3) + 1;}

    public int getTranslationEnd(){return landmarks.get(4) + 1;}

}
