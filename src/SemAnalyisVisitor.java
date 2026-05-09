import syntaxtree.*;
import visitor.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;


class SemAnalysisVisitor extends  GJDepthFirst<String, Void> {
  private HashMap<String, ClassInfo> symbolTable;
  private String CurrentClass;
  private String CurrentMethod = null;
  public  SementicAnalysisVisitor(HashMap<String, ClassInfo> Spy) {
    this.symbolTable = Spy;
  }

}
