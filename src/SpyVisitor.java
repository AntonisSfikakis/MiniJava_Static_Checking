import syntaxtree.*;
import visitor.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

class SpyVisitor extends GJDepthFirst<String, Void>{
  private HashMap<String, ClassInfo> Spy = new HashMap<>();
  private String CurrentClass;
  private String CurrentMethod = null;
  public HashMap<String, ClassInfo> getSpy() {
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
        ClassInfo a = new ClassInfo(" ");
        Spy.put(classname, a);
         

        super.visit(n, argu);
        System.out.println();

        return null;
    }

    /*NOT DONE*/

    /**
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
        Spy.put(classname, new ClassInfo(" "));
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

        if (CurrentMethod == null)
          Spy.get(CurrentClass).Field.put(var, type);
        else 
          Spy.get(CurrentClass).Methods.get(CurrentMethod).Local_vars.put(var, type);

        // System.out.println(var + " " + type);

        super.visit(n, argu);
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
        Spy.get(CurrentClass).Methods.put(myName, new MethodInfo(myType));

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
        
        Spy.get(CurrentClass).Methods.get(CurrentMethod).Parameters.put(name, type);

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
