import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Generator {
    public static String getStringPath(String f) {
        String p = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = (new File(p)).getParentFile().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);

        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win"))
            path += "\\" + f;
        else
            path += "/" + f;

        return path;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        String p;
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            p = Generator.getStringPath("Assignments\\");
        else
            p = Generator.getStringPath("Assignments/");

        Generate(p);

        try {
            Desktop.getDesktop().open(new File(p));
        } catch (IOException ignored) {}
    }

    public static void Generate(String p) throws IOException, InvalidFormatException {
        Scanner nameInput = new Scanner(new File(getStringPath("names.txt")));
        ArrayList<String> names = new ArrayList<>();

        // input
        while (nameInput.hasNext())
            names.add(nameInput.nextLine());

        // running loop
        for (String n : names)
            new Assignment(n, p);
    }

}
