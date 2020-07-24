import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class PolySeq {
    private String polypeptide = "";
    private static ArrayList<String> letters = new ArrayList<>();
    private static ArrayList<String> aminoAcids = new ArrayList<>();

    public PolySeq(String RNA) throws FileNotFoundException {
        geneticCode();
        amino(RNA);
    }

    private void geneticCode() throws FileNotFoundException {
        Scanner input = new Scanner(new File(Generator.getStringPath("GeneticCode.txt")));
        input.useDelimiter("[\t\n]");
        while (input.hasNext()) {
            letters.add(input.next());
            aminoAcids.add(input.next());
        }
    }

    private void amino(String RNA) {
        int start = RNA.indexOf("AUG");
        for (int i = start; i < RNA.length(); i+=3){
            String temp = "" + RNA.charAt(i) + RNA.charAt(i + 1) + RNA.charAt(i + 2);

            if (temp.equals("UAA") || temp.equals("UAG") || temp.equals("UGA")) {
                polypeptide += "STOP";
                break;
            }else {
                polypeptide += aminoAcids.get(letters.indexOf(temp));
                polypeptide += "-";
            }
        }
    }

    public static String getAminoAcid(String codon){return aminoAcids.get(letters.indexOf(codon));}

    public String getPolypeptide(){return polypeptide;}
}
