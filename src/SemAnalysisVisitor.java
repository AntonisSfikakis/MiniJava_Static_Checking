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
  public  SemAnalysisVisitor(HashMap<String, ClassInfo> Spy) {
    this.symbolTable = Spy;
  }
   /**

     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */

  /*
   * Inheritence type checking.
   * */
    @Override
    public String visit(ClassExtendsDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        String parent_class = n.f3.accept(this, argu);
        
        CurrentClass = classname;
      
        String current = parent_class; 
        while (current != null) {
          if (current.equals(classname)) 
            throw new Exception(" Class " + parent_class + " already extends " + classname);
          current = symbolTable.get(current).Parent_class;

        }

      return null;
    }


   @Override
   public String visit(BooleanType n, Void argu) {
        return "boolean";
    }   

   @Override
   public String visit(ArrayType n, Void argu) {
        return "int[]";
    }

   @Override
   public String visit(IntegerType n, Void argu) {
        return "int";
    }

  
   @Override
   public String visit(Identifier n, Void argu) {
        return n.f0.toString();
    }

}
