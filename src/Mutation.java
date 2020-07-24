import java.util.ArrayList;
import java.util.Random;

public class Mutation {
    ArrayList<Integer> seq;
    ArrayList<Integer> marks;
    int pointBase, pointNucleotide, deletionBase;
    String origRNA, pointRNA, deletionRNA;
    private char[] DNAComplementary = new char[]{'T', 'G', 'A', 'C'};

    public Mutation(ArrayList<Integer> sequence, ArrayList<Integer> landmarks) {
        seq = sequence;
        marks = landmarks;
        origRNA = DNASeq.generateRNA(seq, marks);

        // point mutation
        pointBase = randomBase();
        pointNucleotide = randomNucleotide(pointBase);
        ArrayList<Integer> pointSequence = modifySeq(seq, pointBase, pointNucleotide);
        pointRNA = DNASeq.generateRNA(pointSequence, marks);

        // deletion mutation
        deletionBase = randomBase();
        while (deletionBase == pointBase || deletionBase > (landmarks.get(4) - 5)) // will not allow same base as point, or a mutation too close to the stop codon
            deletionBase = randomBase();
        ArrayList<Integer> deletionSequence = deleteSeq(seq, deletionBase); // removes the base from the seq
        ArrayList<Integer> deletionMarks = marks;
        deletionMarks.set(2, deletionMarks.get(2) - 1); // when removing a base, it moves transcription end 1 back
        deletionRNA = DNASeq.generateRNA(deletionSequence, marks);
    }

    private int randomBase() {
        Random gen = new Random();
        return gen.nextInt((marks.get(4)) - (marks.get(3) + 3)) + (marks.get(3) + 3);
    }

    private int randomNucleotide(int base) {
        Random gen = new Random();
        int x = gen.nextInt(4);
        while (x == seq.get(base))
            x = gen.nextInt(4);
        return x;
    }

    private ArrayList<Integer> modifySeq(ArrayList<Integer> seq, int base, int nucleotide) {
        ArrayList<Integer> s = seq;
        s.set(base, nucleotide);
        return s;
    }

    private ArrayList<Integer> deleteSeq(ArrayList<Integer> s, int b) {
        s.remove(b);
        return s;
    }

    public String getPointMutationQuestion() {
        return "What would be the effect if a point mutation in the template strand" +
                " changed the base at position " + (pointBase + 1) + " to " + DNAComplementary[pointNucleotide] + ", and what mutation would" +
                " it be classified as?";
    }

    public String getPointMutationAnswer() {
        String answer = "Sense mutation (";
        for (int i = origRNA.indexOf("AUG"); i < origRNA.length(); i += 3) {
            String oldCodon = origRNA.substring(i, i + 3);
            String newCodon = pointRNA.substring(i, i + 3);
            String oldPoly = PolySeq.getAminoAcid(origRNA.substring(i, i + 3));
            String newPoly = PolySeq.getAminoAcid(pointRNA.substring(i, i + 3));
            if (!oldCodon.equals(newCodon)) { // if it's a sense mutation it adds the codons/poly
                answer = answer.concat(oldCodon + "/" + oldPoly + " -> " + newCodon + "/" + newPoly + ")");
            }
            if (!oldPoly.equals(newPoly)) { // if the amino sequences are different
                if (newPoly.equals("STOP")) { // and the new one is STOP, it is a nonsense
                    answer = "Nonsense mutation (" + oldCodon + "/" + oldPoly + " -> " + newCodon + "/" + newPoly + ")";
                } else { // otherwise, it is a missense
                    answer = "Missense mutation (" + oldCodon + "/" + oldPoly + " -> " + newCodon + "/" + newPoly + ")";
                }
                break;
            }
            if (oldPoly.equals("STOP")) { // finding no different amino sequences, and hitting stop codon, loop breaks
                break;
            }
        }
        return answer;
    }

    public String getDeletionMutationQuestion() {
        return "What would be the effect if a point mutation in the template strand" +
                " deleted the base at position " + (deletionBase + 1) + ", and what mutation would" +
                " it be classified as?";
    }

    public String getDeletionMutationAnswer() {
        String answer = "Missense mutation (No stop codon)";
        for (int i = deletionRNA.indexOf("AUG"); i < deletionRNA.length(); i += 3) {
            String oldCodon = origRNA.substring(i, i + 3);
            String newCodon = deletionRNA.substring(i, i + 3);
            String oldPoly = PolySeq.getAminoAcid(origRNA.substring(i, i + 3));
            String newPoly = PolySeq.getAminoAcid(deletionRNA.substring(i, i + 3));
            if (newPoly.equals("STOP")) {
                answer = "Frameshift mutation by deletion causing nonsense mutation (Early stop codon: " + oldCodon + "/" + oldPoly + " -> " + newCodon + "/" + newPoly + ")";
                break;
            }
            else if (oldPoly.equals("STOP")){
                answer = "Frameshift mutation by deletion causing missense mutation (No stop codon or late stop codon)";
                break;
            }
        }
        return answer;
    }
}

