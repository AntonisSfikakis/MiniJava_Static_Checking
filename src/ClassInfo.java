import  java.util.HashMap;
import  java.util.LinkedHashMap;
import  java.util.List;
/**
 * A class has  : fileds , methods and has to know its parent
 * A method has : local variables, return type, paremeters (with order) 
 */

public class ClassInfo {
  public HashMap<String, String> Field;
  public HashMap<String, List<MethodInfo>> Methods;
  public String Parent_class;

  public ClassInfo (String parent_class) {
    this.Field = new HashMap<>();
    this.Methods = new HashMap<>();
    this.Parent_class = parent_class; 
  }
 
  @Override
  public String toString() {
     return "ClassInfo{" +
        "Parent=" + Parent_class +
        ", Fields=" + Field +
        ", Methods=" + Methods +
        "}";
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
 
  @Override
  public String toString() {
     return "MethodInfo{" +
        "Return=" + Return_type +
        ", Params=" + Parameters +
        ", LocalVars=" + Local_vars +
        "}";
    }
  
}

