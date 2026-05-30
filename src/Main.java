import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import parser.*;
import syntaxtree.*;

public class Main {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java Main <inputFile>");
      System.exit(1);
    }
    int files = args.length;
    for (int j = 0; j < files; j++) {
      System.out.println(); 
      System.out.println("------------------------- file input : " + args[j] + " -------------------------");
      System.out.println(); 

      FileInputStream fis = null; 
      try {
        fis = new FileInputStream(args[j]);
        MiniJavaParser parser = new MiniJavaParser(fis);

        Goal root = parser.Goal();

        System.err.println("Program parsed successfully.");

        SpyVisitor spy = new SpyVisitor();
        root.accept(spy, null);

        SemAnalysisVisitor sem = new SemAnalysisVisitor(spy.getSpy());
        root.accept(sem, null);

        OffsetCalculator off = new OffsetCalculator(spy.getSpy());
        off.calculate();

        System.out.println("------------------------- END OF FILE -------------------------");       
        System.out.println();

      } catch (ParseException ex) {
        System.out.println(ex.getMessage());

      } catch (FileNotFoundException ex) {
        System.err.println(ex.getMessage());

      } catch (Exception ex) {
        System.err.println(ex.getMessage());

      } finally {
        try {
          if (fis != null)
            fis.close();
        } catch (IOException ex) {
          System.err.println(ex.getMessage());
        }
      }
    }
  }
}
