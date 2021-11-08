package yrs.yvhn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try
        {
            PascalStateMachineLexicalAnalyzer analyzer = new PascalStateMachineLexicalAnalyzer("input.txt");
            String res = analyzer.ReadTokens();
            System.out.println(res);

            String outputFileName = "output.txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            writer.write(res);
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getStackTrace());
        }
    }
}
