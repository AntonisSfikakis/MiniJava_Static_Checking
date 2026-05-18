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
  private HashMap<String, Integer> methodIndex  =  new HashMap<>(); 
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
  
/*------------------------ClassInfo and MethodInfo declarartion-------------------------*/
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
      methodIndex.clear();

      String current = parent_class; 
      while (current != null) {
        if (current.equals(classname)) 
          throw new Exception(" Class " + parent_class + " already extends " + classname);
        current = symbolTable.get(current).Parent_class;

      }

      n.f5.accept(this, null);
      n.f6.accept(this, null);
        
    return null;
  }

  /**
   * Grammar production:
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> ( VarDeclaration() )*""
   * f4 -> ( MethodDeclaration() )*
   * f5 -> "}"
   */
  @Override
  public String visit(ClassDeclaration n, Void argu) throws Exception {
      String classname = n.f1.accept(this, null);
      CurrentClass = classname;
      methodIndex.clear();
      n.f3.accept(this, null);
      n.f4.accept(this, null);
      return null;

  }


  /**
   * Grammar production:
   * f0 -> "public"
   * f1 -> Type()
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( FormalParameterList() )?
   * f5 -> ")"
   * f6 -> "{"
   * f7 -> ( VarDeclaration() )*
   * f8 -> ( Statement() )*
   * f9 -> "return"
   * f10 -> Expression()
   * f11 -> ";"
   * f12 -> "}"
   */
  @Override
  public String visit(MethodDeclaration n, Void argu) throws Exception {
     String methodname =  n.f2.accept(this, null);
     if (methodIndex.containsKey(methodname)) 
       methodIndex.put(methodname, methodIndex.get(methodname) + 1);
     else 
       methodIndex.put(methodname, 0);
      
     /* Store by order , return the last one in the list */
     int index  = methodIndex.get(methodname);
     CurrentMethodInfo = symbolTable.get(CurrentClass).Methods.get(methodname).get(index);
     
     n.f4.accept(this, null);
     n.f7.accept(this, null);
     n.f8.accept(this, null);
     n.f10.accept(this, null);
     return null;
  }

/*-----------------------------Statement checking-------------------------------------*/

  /**
   * Grammar production:
   * f0 -> Identifier()
   * f1 -> "="
   * f2 -> Expression()
   * f3 -> ";"
   */
  @Override
  public String visit(AssignmentStatement n, Void argu) throws Exception {
    String variableName  = n.f0.accept(this, null);     
    String left  = LookupType(variableName);
    String right = n.f2.accept(this, null);
    
    if (!left.equals(right))
      throw new Exception(left + " is different type : " + right);

    return  left;
  }

  /**
   * Grammar production:
   * f0 -> Identifier()
   * f1 -> "["
   * f2 -> Expression()
   * f3 -> "]"
   * f4 -> "="
   * f5 -> Expression()
   * f6 -> ";"
   */
  @Override
  public String visit(ArrayAssignmentStatement n, Void argu) throws Exception {
    String variableName  = n.f0.accept(this, null);     
    String left  = LookupType(variableName);
    String index = n.f2.accept(this, null);
    String right = n.f5.accept(this, null);

    if (!index.equals("int") || !left.equals("int[]") || !right.equals("int")) 
      throw new Exception("Exceptino rasied in : " + left + " " + index + " " + right);
    return "int";
 
   }

  /**
   * Grammar production:
   * f0 -> "if"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   * f5 -> "else"
   * f6 -> Statement()
   */
  @Override
  public String visit(IfStatement n, Void argu) throws Exception {
    String condition = n.f2.accept(this, null);
    
    if (!condition.equals("boolean"))
      throw new Exception("Wrong condition in if statement")
    
    String Statement = n.f4.accept(this, null);
    String Statement = n.f6.accept(this, null);

    return null; 
   }
  /**
   * Grammar production:
   * f0 -> "System.out.println"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> ";"
   */

  @Override
  public String visit(PrintStatement n, Void argu) throws Exception {
    String expression = n.f2.accept(this, null);
    if (!expression.equals("int"))
      throw new Exception("System.out.println accepts only in");

    return null; 
   }

  /**
   * Grammar production:
   * f0 -> "while"
   * f1 -> "("
   * f2 -> Expression()
   * f3 -> ")"
   * f4 -> Statement()
   */
  @Override
  public String visit(WhileStatement n, Void argu) throws Exception {
    String condition = n.f2.accept(this, null);
    
    if (!condition.equals("boolean"))
      throw new Exception("Wrong condition in while statement");
    
    String Statement = n.f4.accept(this, null);
    return null; 
   }




  /**
   * Grammar production:
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> Identifier()
   * f3 -> "("
   * f4 -> ( ExpressionList() )?
   * f5 -> ")"
   */
  @Override
  public String visit(MessageSend n, Void argu) throws Exception {
     

  }
 


/*-----------------------------Expression checking------------------------------------*/
  @Override 
  public String visit(PlusExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);       
    String type2 = n.f2.accept(this, null);       
    
    if (!type1.equals("int") || !type2.equals("int")) 
      throw new Exception("Wrong types  in " + " expression " + type1 + type2);


    return "int";
  }

  @Override 
  public String visit(MinusExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);       
    String type2 = n.f2.accept(this, null);       
    
    if (!type1.equals("int") || !type2.equals("int")) 
      throw new Exception("Wrong types  in " + " expression " + type1 + type2);


    return "int";
  }
  
  @Override 
  public String visit(CompareExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);       
    String type2 = n.f2.accept(this, null);       
    
    if (!type1.equals("int") || !type2.equals("int")) 
      throw new Exception("Wrong types  in " + " expression " + type1 + type2);


    return "boolean";
  }
 

  @Override 
  public String visit(TimesExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);       
    String type2 = n.f2.accept(this, null);       
    
    if (!type1.equals("int") || !type2.equals("int")) 
      throw new Exception("Wrong types  in " + " expression " + type1 + type2);


    return "int";
  }
 
  /**
   * Grammar production:
   * f0 -> "!"
   * f1 -> Clause()
   */
  @Override 
  public String visit(NotExpression n, Void argu) throws Exception {
    String clause = n.f1.accept(this, null);
    if (!clause.equals("boolean"))
      throw new Exception("Not expression should return a boolean");

    return "boolean";
  }

  /**
   * Grammar production:
   * f0 -> Clause()
   * f1 -> "&&"
   * f2 -> Clause()
   */

  @Override 
  public String visit(AndExpression n, Void argu) throws Exception {
   String clause1 = n.f0.accept(this, null);   
   String clause2 = n.f2.accept(this, null);   

   if (!clause1.equals("boolean") || !clause2.equals("boolean"))
      throw new Exception("AND expression should return a boolean");

   return "boolean";
  
  }

  /**
   * Grammar production:
   * f0 -> PrimaryExpression()
   * f1 -> "["
   * f2 -> PrimaryExpression()
   * f3 -> "]"
   */
  @Override
  public String visit(ArrayLookup n, Void argu) throws Exception {
    String array = n.f0.accept(this, null);
    String index = n.f2.accept(this, null);

    if (!(index.equals("int") && array.equals("int[]"))) 
      throw new Exception("Wrong type in " + array + " " + index + " ");

    return "int";
  }


  /**
   * Grammar production:
   * f0 -> PrimaryExpression()
   * f1 -> "."
   * f2 -> "length"
   */
  @Override
  public String visit(ArrayLength n, Void argu) throws Exception {
    String array = n.f0.accept(this, null);
    if (!array.equals("int[]"))
      throw new Exception(array + " needs to be type int");

    return "int";
  }
 
 /*---------------------------Leafes literals-------------------------------------------*/ 
  /**
   * Grammar production:
   * f0 -> IntegerLiteral()
   *       | TrueLiteral()
   *       | FalseLiteral()
   *       | Identifier()
   *       | ThisExpression()
   *       <IDENTIFIER>| ArrayAllocationExpression()
   *       | AllocationExpression()
   *       | BracketExpression()
   */

   
 
   @Override 
   public String visit(PrimaryExpression n, Void argu) throws Exception {
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
   public String visit(Identifier n, Void argu) throws Exception {
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
   public String visit(AllocationExpression n, Void argu) throws Exception {
    isVariable = false;
    String id  = n.f1.accept(this, null);
    isVariable = true;
    return id; 

   }
 
  @Override
  public String visit(BracketExpression n, Void argu) throws Exception{
    return n.f1.accept(this, null);

  }


}
