import  java.util.HashMap;
import  java.util.LinkedHashMap;
/**
 * A class has  : fileds , methods and has to know its parent
 * A method has : local variables, return type, paremeters (with order) 
 */

public class ClassInfo {
  public HashMap<String, String> Field;
  public HashMap<String, MethodInfo> Methods;
  public String Parent_class;

  public ClassInfo (String parent_class) {
    this.Field = new HashMap<>();
    this.Methods = new HashMap<>();
    this.Parent_class = parent_class; 
  }

}

class MethodInfo {
  public String Return_type;
  public LinkedHashMap<String, String> Parameters;
  public HashMap<String, String> Local_vars;

  MethodInfo(String return_type) {
    this.Return_type = return_type;
    this.Parameters  = new LinkedHashMap<>();
    this.Local_vars  = new HashMap<>();
  }

}

