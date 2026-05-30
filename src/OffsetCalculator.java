import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.StyledEditorKit.BoldAction;

import java.util.AbstractMap;


import syntaxtree.*;
import visitor.*;

class OffsetCalculator {
  private record Pair(int x, int y) {}
  private HashMap<String, Pair>  class_offsets = new HashMap<>();
  private LinkedHashMap<String, ClassInfo> offset_table;
  private HashMap<String, Set<String>> Over_load_ride_check = new HashMap<>(); 
  public OffsetCalculator(LinkedHashMap<String, ClassInfo> Spy) {
      this.offset_table = Spy;
  }
  
  protected void calculate()  {
    int field_offset = 0; 
    int method_offset = 0;
    boolean isMain = true; 

    for (String _class : offset_table.keySet()) {
        ClassInfo current = offset_table.get(_class);
        System.out.println("------------Class " + _class + " --------------");

        if (isMain) { isMain = false; continue;}
        
        Set<String> s;
        if (current.Parent_class != null && Over_load_ride_check.containsKey(current.Parent_class)) 
           s = new HashSet<>(Over_load_ride_check.get(current.Parent_class));
        else
           s = new HashSet<>();

        field_offset = 0;
        method_offset = 0; 
        if (current.Parent_class != null) {
            field_offset = class_offsets.get(current.Parent_class).x;
            method_offset = class_offsets.get(current.Parent_class).y;
        } 
      
        /*---------Field_Calculation----------*/
        System.out.println("------------ Variables ------------");
        for (String _field : current.Field.keySet()) {
          int bytes = CalculateBytes(current.Field.get(_field));
          System.out.println(_class + "." + _field + " : " + field_offset);
          field_offset += bytes;
        }
      
        /*---------Method_Calculation----------*/
        System.out.println("------------ Methods ------------");
        for (String _method : current.Methods.keySet()) {
          List<MethodInfo>  methodlist = offset_table.get(_class).Methods.get(_method);
          for (MethodInfo method_params : methodlist) {
              String ret = _method;
              String[] parameters = method_params.Parameters.values().toArray(new String[0]);
              for (int i = 0; i < parameters.length; i++) {       
                ret += "_" + parameters[i];
              }

              if (s.contains(ret)) continue;
              s.add(ret);

              System.out.println(_class + "." + ret + " : " + method_offset);
              method_offset += 8;
          }

          Over_load_ride_check.put(_class, s);
        } 
          
        System.out.println();
        System.out.println();
        class_offsets.put(_class, new Pair(field_offset, method_offset));
    
    }   
  }

  /* this functions gets a type an returns its bytes */ 
  private int CalculateBytes(String type) {
    if (type.equals("int")) return 4;
    if (type.equals("boolean")) return 1;

    return 8;
  }

}

