class TestExpressions {
  public static void main(String[] args) {
  }
}

class Correct {
  int x;
  int y;
  boolean flag;

  public int testArithmetic() {
    int a;
    int b;
    int c;
    a = 1 + 2;
    b = 3 - 1;
    c = a * b;
    return c;
  }

  public boolean testCompare() {
    int a;
    int b;
    boolean result;
    result = a < b;
    return result;
  }

  public boolean testAnd() {
    boolean a;
    boolean b;
    boolean c;
    c = a && b;
    return c;
  }

  public boolean testNot() {
    boolean a;
    boolean b;
    b = !a;
    return b;
  }
}

class NotError {
  public int bad() {
    int x;
    x = !5;
    return x;
  }
}

class AndError {
  public boolean bad() {
    boolean x;
    x = 1 && 2;
    return x;
  }
}

class CompareError {
  public boolean bad() {
    boolean x;
    x = true < false;
    return x;
  }
}
