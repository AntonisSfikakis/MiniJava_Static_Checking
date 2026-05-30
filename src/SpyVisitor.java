import syntaxtree.*;
import visitor.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

class SpyVisitor extends GJDepthFirst<String, Void> {
  private LinkedHashMap<String, ClassInfo> Spy = new LinkedHashMap<>();
  private String CurrentClass;
  private String CurrentMethod = null;
  public LinkedHashMap<String, ClassInfo> getSpy() {
    return Spy;
  }

  
   /**
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
        String classname = n.f1.accept(this, null);
        ClassInfo a = new ClassInfo(null);
        Spy.put(classname, a);
       
        CurrentClass  = classname;
        CurrentMethod = "main";
        Spy.get(CurrentClass).Methods.put("main", new ArrayList<>());
        Spy.get(CurrentClass).Methods.get("main").add(new MethodInfo("void"));      

        n.f14.accept(this, null);
        CurrentMethod = null;
        return null;
    }

    /*NOT DONE*/

    /*Lj*
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, argu);
        if (Spy.containsKey(classname)) 
          throw new Exception("Duplicate class: " + classname);

        Spy.put(classname, new ClassInfo(null));
        CurrentClass = classname;

        n.f3.accept(this, argu);
        n.f4.accept(this, argu);

        return null;
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
    @Override
    public String visit(ClassExtendsDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        String parent_class = n.f3.accept(this, argu);
        
        CurrentClass = classname;

        /* a extends b . b prepei na exei oristei */   
        if (!Spy.containsKey(parent_class)) 
          throw new Exception(" Class " + parent_class + " has not been declared"); 
          
 
        if (Spy.containsKey(classname)) 
          throw new Exception("Duplicate class: " + classname);

        Spy.put(classname, new ClassInfo(parent_class));

        n.f5.accept(this, argu);
        n.f6.accept(this, argu);

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   @Override
   public String visit(VarDeclaration n, Void argu) throws Exception {
        String _ret = null;
        String type = n.f0.accept(this, argu);
        String var  = n.f1.accept(this, argu);
       
        /*
         * Check if we are in a class -> CurrentMethod will be null
         * Else we are in a method.
         * */

        if (CurrentMethod == null) {
          if (Spy.get(CurrentClass).Field.containsKey(var)) 
           throw new Exception("Dulicate field initialization: " + var);           
            
          Spy.get(CurrentClass).Field.put(var, type);
        }
        else{
          List<MethodInfo> methodlist = Spy.get(CurrentClass).Methods.get(CurrentMethod);
          MethodInfo current = methodlist.get(methodlist.size() - 1);
          if (current.Local_vars.containsKey(var) || current.Parameters.containsKey(var))
            throw new Exception("Duplicate local variable: " + var + " in method " + CurrentMethod);
          current.Local_vars.put(var, type);
        } 

        return _ret;
   }

    /**
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
        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);

        CurrentMethod = myName; 
        if (!Spy.get(CurrentClass).Methods.containsKey(myName)) 
          Spy.get(CurrentClass).Methods.put(myName, new ArrayList<>());

        
        Spy.get(CurrentClass).Methods.get(myName).add(new MethodInfo(myType));

        String argumentList = n.f4.present() ? n.f4.accept(this, null) : "";
        /* Double walk here maybe control my flow of execution */
        n.f7.accept(this, null);
        CurrentMethod = null;

        return null;
    }


    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    @Override
    public String visit(FormalParameterList n, Void argu) throws Exception {
        String ret = n.f0.accept(this, null);

        if (n.f1 != null) {
            ret += n.f1.accept(this, null);
        }

        return ret;
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    @Override
    public String visit(FormalParameterTail n, Void argu) throws Exception {
        String ret = "";
        for ( Node node: n.f0.nodes) {
            ret += ", " + node.accept(this, null);
        }

        return ret;
    }

    /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public String visit(FormalParameterTerm n, Void argu) throws Exception {
        return n.f1.accept(this, argu);
    }


    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, Void argu) throws Exception{
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);

        List<MethodInfo> methodlist = Spy.get(CurrentClass).Methods.get(CurrentMethod);
        MethodInfo current = methodlist.get(methodlist.size() - 1); 
       
        if (current.Parameters.containsKey(name)) 
          throw new Exception("Duplicate parameter: " + name + " in method " + CurrentMethod);
        current.Parameters.put(name, type);

        return type + " " + name;
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
