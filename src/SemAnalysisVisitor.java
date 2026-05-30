import java.beans.Expression;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.LongUnaryOperator;


import syntaxtree.*;
import visitor.*;

class SemAnalysisVisitor extends GJDepthFirst<String, Void> {
  private LinkedHashMap<String, ClassInfo> symbolTable;
  private String CurrentClass;
  private String CurrentMethod = null;
  private MethodInfo CurrentMethodInfo = null;
  private boolean isVariable = false;
  private HashMap<String, Integer> methodIndex = new HashMap<>();

  public SemAnalysisVisitor(LinkedHashMap<String, ClassInfo> Spy) {
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

    throw new Exception("Variable: " + var + " has not beed declared");
  }

  private boolean IsPrimitive(String type) {
    return type.equals("int") || type.equals("int[]") || type.equals("boolean");
  }

  private boolean LookupFather(String _class, String father) {
    if (_class.equals(father))
      return true;

    String current = symbolTable.get(_class).Parent_class;
    while (current != null) {
      if (current.equals(father))
        return true;

      current = symbolTable.get(current).Parent_class;
    }

    return false;
  }
 
  /*
   * This functions get a class and a method and creats a big list of all methods of his fathers
   * 
   * */
  private List<MethodInfo> GetAllMethods(String _class, String method) {
    List<MethodInfo> all_methods = new ArrayList<>();

    String current = _class;
    while (current != null) {
      List<MethodInfo> methodlist = symbolTable.get(current).Methods.get(method);
      if (methodlist != null) 
        all_methods.addAll(methodlist);

      current = symbolTable.get(current).Parent_class;
    }

    return all_methods.isEmpty() ? null : all_methods; 
  }

  private boolean isCompatible(String from, String to) {
    if (from.equals(to))
      return true;
    if (IsPrimitive(from) || IsPrimitive(to))
      return false;
    return LookupFather(from, to);
  }

  private boolean CheckOverride(String methodname) {
    /**
     * for this method -> take the args
     * CurrentCLassMethod has the method 
     */
    if (CurrentMethodInfo  == null)
      System.out.println("CurrentMethod info is null something is wrong here.");

    String parent = symbolTable.get(CurrentClass).Parent_class;
    if (parent == null) return true;

    List<MethodInfo> parent_methods = GetAllMethods(parent, methodname);
    if (parent_methods == null) return true;

    String[] Method_args = CurrentMethodInfo.Parameters.values().toArray(new String[0]);
    /* now i have to iterate the list an check if they have exact the same type */      
    for (MethodInfo method : parent_methods) {
        String[] parameters = method.Parameters.values().toArray(new String[0]);
        boolean match = false; 
        if (parameters.length != Method_args.length) continue;

        if (Method_args.length == 0) {
           if (!CurrentMethodInfo.Return_type.equals(method.Return_type))
             return false;
           else return true;
        } 

        for (int i = 0; i < Method_args.length; i++) {
          if (!Method_args[i].equals(parameters[i])) break; 
          if (i == Method_args.length - 1) match = true; 
        }
        
        if (match) {
          if (!CurrentMethodInfo.Return_type.equals(method.Return_type))
            return false;
          else return true;
        }

    }

    return true;
  }

  private boolean CheckOverload(String methodname) throws  Exception {
    /*
     * for each method of the  i have to compare thy type with the method name. If there is no realationship
     * everything okay i should move. If everyone has some kond -> error
     * */

    List<MethodInfo> methodlist = GetAllMethods(CurrentClass, methodname);
    String[] Method_args = CurrentMethodInfo.Parameters.values().toArray(new String[0]);
    for (MethodInfo method : methodlist) {
      String[] parameters = method.Parameters.values().toArray(new String[0]);
      if (method == CurrentMethodInfo) continue;

      if (parameters.length != Method_args.length) continue;
      boolean all_comp = true;
      for (int i = 0; i < parameters.length; i++) {
         if (!isCompatible(parameters[i], Method_args[i]) && !isCompatible(Method_args[i], parameters[i])) {
           all_comp = false;
           break;
         }

      }

      if (all_comp) {
        boolean all_exact = true;
        for (int i = 0; i < parameters.length; i++) {
          if (!parameters[i].equals(Method_args[i])) {
            all_exact = false;
            break;
          }
        }

        if (all_exact) {
          List<MethodInfo> currentMethods = symbolTable.get(CurrentClass).Methods.get(methodname);
          if (currentMethods != null && currentMethods.contains(method))
            /* exact same , duplicate inside the same function */
            return false;
         /* its an override */ 
          continue; 
        }
        
        return false;
      }
    }

    return true;
  }
 
  /* Main class Override */

  /**
   * Grammar production:
   * f0 -> "class"
   * f1 -> Identifier()
   * f2 -> "{"
   * f3 -> "public"
   * f4 -> "static"
   * f5 -> "void"
   * f6 -> "main"
   * f7 -> "("
   * f8 -> "String"
   * f9 -> "["
   * f10 -> "]"
   * f11 -> Identifier()
   * f12 -> ")"
   * f13 -> "{"
   * f14 -> ( VarDeclaration() )*
   * f15 -> ( Statement() )*
   * f16 -> "}"
   * f17 -> "}"
   */

  @Override 
  public String visit(MainClass n, Void argu) throws Exception {
    isVariable = false;
    CurrentClass =  n.f1.accept(this , null); 
    List<MethodInfo> mainlist = symbolTable.get(CurrentClass).Methods.get("main");
    CurrentMethodInfo = mainlist.get(0);

    isVariable = true;
    n.f15.accept(this, null);
    isVariable = false;
    
    return null;
  }

  /*------------------------ClassInfo and MethodInfo
   * declarartion-------------------------*/
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
   */
  @Override
  public String visit(ClassExtendsDeclaration n, Void argu) throws Exception {
    isVariable = false;
    String classname = n.f1.accept(this, null);
    String parent_class = n.f3.accept(this, argu);

    CurrentClass = classname;
    methodIndex.clear();

    /* Legit extend */
    String current = parent_class;
    while (current != null) {
      if (current.equals(classname))
        throw new Exception(" Class " + parent_class + " already extends " +
                            classname);

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
    isVariable = false;
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
    isVariable = false;

    n.f1.accept(this, null);
    String methodname = n.f2.accept(this, null);
    if (methodIndex.containsKey(methodname))
      methodIndex.put(methodname, methodIndex.get(methodname) + 1);
    else
      methodIndex.put(methodname, 0);

 
    /* Store by order , return the last one in the list */
    int index = methodIndex.get(methodname);
    CurrentMethodInfo =
        symbolTable.get(CurrentClass).Methods.get(methodname).get(index);
    
    if (!CheckOverride(methodname))
      throw new Exception("Override fucntion: " + methodname + " should have the same return type as its parent of its class");

    if (!CheckOverload(methodname))
      throw new Exception(methodname + " ambiguity problem. This functions might be declared again with exact a same argumetn or they have a  super/subtype relationship");


    isVariable = true;

    String return_expression = n.f10.accept(this, null);
    String type = CurrentMethodInfo.Return_type;
    
    if (!isCompatible(return_expression, type))
      throw new Exception("Method " + methodname + " should return : " + type + ".Not " + return_expression);

     n.f8.accept(this, null);
     isVariable = false;

     return null;
  }

  /*-----------------------------Variable Declaration-----------------------------------*/

  /**
   * Grammar production:
   * f0 -> Type()
   * f1 -> Identifier()
   * f2 -> ";"
   */
  @Override
  public String visit(VarDeclaration n, Void argu) throws Exception {
    isVariable = false;
    n.f0.accept(this, null);
    n.f1.accept(this, null);


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
    isVariable = true;
    String left = n.f0.accept(this, null);
    String right = n.f2.accept(this, null);
    isVariable = false;

    if (!isCompatible(right, left))
      throw new Exception(left + " is different type from " + right);

    return left;
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
    isVariable = true;
    String left = n.f0.accept(this, null);
    String index = n.f2.accept(this, null);
    String right = n.f5.accept(this, null);

    if (!index.equals("int") || !left.equals("int[]") || !right.equals("int"))
      throw new Exception("Exceptino rasied in : " + left + " " + index + " " +
                          right);
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
      throw new Exception("Wrong condition in if statement");

    String Statement = n.f4.accept(this, null);
    String Statement_else = n.f6.accept(this, null);

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
      throw new Exception("System.out.println accepts only int");

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
    /* Primary expression should be a class */
    /* class_type.method(input) */
    String _class = n.f0.accept(this, null);
    if (IsPrimitive(_class))
      throw new Exception(_class + " should be a class type");

    isVariable = false;
    String method = n.f2.accept(this, null);
    isVariable = true;

    /* method call should belong to the class */
    List<MethodInfo> methodlist = GetAllMethods(_class, method);
    if (methodlist == null)
      throw new Exception("ERROR " + _class + " " + method);

    String arg = n.f4.accept(this, null);
    String[] args;
    if (arg == null || arg.equals(""))
      args = new String[0];
    else
      args = arg.split(",");

    /**
     * arg = [int, boolean, A]
     * method = [int, boolean, B]
     * this is correct if B extends A -> A is parent of B
     */

    /*
     * for each method that matches we should check its type or a super/subtype
     * relation
     */
    /* Iterate trough the method list */
    for (MethodInfo Method : methodlist) {
      boolean match = false;
      String[] parameters = Method.Parameters.values().toArray(new String[0]);

      if (args.length != parameters.length)
        continue;
      if (args.length == 0 && parameters.length == 0)
        return Method.Return_type;

      for (int i = 0; i < parameters.length; i++) {
        // check if there is super/subtype relation
        if (!isCompatible(args[i], parameters[i])) {
          match = false;
          break;
        }


        if (i == parameters.length - 1)
          match = true;
      }

      if (match)
        return Method.Return_type;
    }

    throw new Exception("ERROR " + _class + "." + method + "()");
  }

  /**
   * f0 -> Expression()
   * f1 -> ExpressionTail()
   */
  @Override
  public String visit(ExpressionList n, Void argu) throws Exception {
    /* a type will be returned */
    String first_arg = n.f0.accept(this, null);

    if (n.f1 != null)
      return first_arg += n.f1.accept(this, null);
    ;

    return first_arg;
  }

  /**
   * f0 -> (ExpressionTerm())*
   */

  @Override
  public String visit(ExpressionTail n, Void argu) throws Exception {
    String ret = "";
    for (Node node : n.f0.nodes) {
      ret += "," + node.accept(this, null);
    }

    return ret;
  }

  @Override
  public String visit(ExpressionTerm n, Void argu) throws Exception {
    return n.f1.accept(this, null);
  }

  /*-----------------------------Expression
   * checking------------------------------------*/
  @Override
  public String visit(PlusExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);
    String type2 = n.f2.accept(this, null);

    if (!type1.equals("int") || !type2.equals("int"))
      throw new Exception("Wrong types  in "
                          + " expression " + type1 + type2);

    return "int";
  }

  @Override
  public String visit(MinusExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);
    String type2 = n.f2.accept(this, null);

    if (!type1.equals("int") || !type2.equals("int"))
      throw new Exception("Wrong types  in "
                          + " expression " + type1 + type2);

    return "int";
  }

  @Override
  public String visit(CompareExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);
    String type2 = n.f2.accept(this, null);

    if (!type1.equals("int") || !type2.equals("int"))
      throw new Exception("Wrong types  in "
                          + " expression " + type1 + type2);

    return "boolean";
  }

  @Override
  public String visit(TimesExpression n, Void argu) throws Exception {
    String type1 = n.f0.accept(this, null);
    String type2 = n.f2.accept(this, null);

    if (!type1.equals("int") || !type2.equals("int"))
      throw new Exception("Wrong types  in "
                          + " expression " + type1 + type2);

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
    isVariable = true;
    String array = n.f0.accept(this, null);
    String index = n.f2.accept(this, null);

    if (!(index.equals("int") && array.equals("int[]")))
      throw new Exception("Wrong type in " + array + " " + index + " ");

    isVariable = false;
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

  /*---------------------------Leafes
   * literals-------------------------------------------*/
  /**
   * Grammar production:
   * f0 -> IntegerLiteral()
   * | TrueLiteral()
   * | FalseLiteral()
   * | Identifier()
   * | ThisExpression()
   * <IDENTIFIER>| ArrayAllocationExpression()
   * | AllocationExpression()
   * | BracketExpression()
   */

  @Override
  public String visit(PrimaryExpression n, Void argu) throws Exception {
    isVariable = true;
    String id = n.f0.accept(this, argu);
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
  public String visit(IntegerArrayAllocationExpression n, Void argu)
      throws Exception {
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
    String id = n.f1.accept(this, null);
    if (!symbolTable.containsKey(id))
      throw new Exception("Class " + id + " does not exitst");
    isVariable = true;
    return id;
  }

  @Override
  public String visit(BracketExpression n, Void argu) throws Exception {
    return n.f1.accept(this, null);
  }
}
