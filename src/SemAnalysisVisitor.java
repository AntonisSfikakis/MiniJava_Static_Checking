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
  private MethodInfo CurrentMethodInfo = null;
  private boolean isVariable = false;
  public  SemAnalysisVisitor(HashMap<String, ClassInfo> Spy) {
    this.symbolTable = Spy;
 }

  private String LookupType(String var) throws Exception {
    /* first check local vars */
    if (CurrentMethodInfo.Local_vars.containsKey(var))
        return CurrentMethodInfo.Local_vars.get(var);
    
    /* now check the parameters */
    if (CurrentMethodInfo.Parameters.containsKey(var))
      return CurrentMethodInfo.Parameters.get(var);

    /* now check the class */
    if (symbolTable.get(CurrentClass).Field.containsKey(var))
      return symbolTable.get(CurrentClass).Field.get(var);

    /* loop through parents to find the damn var */
    String current = symbolTable.get(CurrentClass).Parent_class;
    while (current != null) {
      if (symbolTable.get(current).Field.containsKey(var))
        return symbolTable.get(current).Field.get(var);

       current = symbolTable.get(current).Parent_class;
    } 

    throw new Exception("Variable: " + var + " has not beed declared" ); 


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


    /* Variable Usage */
/**
 * Grammar production:
 * f0 -> IntegerLiteral()
 *       | TrueLiteral()
 *       | FalseLiteral()
 *       | Identifier()
 *       | ThisExpression()
 *       | ArrayAllocationExpression()
 *       | AllocationExpression()
 *       | BracketExpression()
 */

 
 
   @Override 
   public String visit(PrimaryExpression n, Void argu) {
    isVariable = true;
    String id  = n.f0.accept(this, argu);
    isVariable = false;
    return id;
   }
  
   @Override
   public String visit(TrueLiteral n, Void argu) {
    return "boolean"; 
   }

   @Override
   public String visit(FalseLiteral n, Void argu) {
    return "boolean"; 
   }
  
   @Override
   public String visit(ThisExpression n, Void argu) {
    return CurrentClass; 
   } 
  
   @Override
   public String visit(IntegerLiteral n, Void argu) {
    return "int"; 
   }

   @Override
   public String visit(Identifier n, Void argu) {
     String name = n.f0.toString();
     if (isVariable)
       return LookupType(name);

     return name;
    }
  /**
  * Grammar production:
  * f0 -> "new"
  * f1 -> "int"
  * f2 -> "["
  * f3 -> Expression()
  * f4 -> "]"
  */
   @Override
   public String visit(IntegerArrayAllocationExpression n, Void argu) throws Exception {
     String expression = n.f3.accept(this, null);
     if (!expression.equals("int")) 
       throw new Exception("Not correct type in int[]");

     return "int[]";

   }
  
  /**
  * Grammar production:
  * f0 -> "new"
  * f1 -> Identifier()
  * f2 -> "("
  * f3 -> ")"
  */

   @Override
   public String visit(AllocationExpression n, Void argu) {
    isVariable = false;
    string id  = n.f1.accept(this, null);
    isVariable = true;
    return id; 

   }
 
  @Override
  public String visit(BracketExpression n, Void argu) {
    return n.f1.accept(this, null);

  }


}
