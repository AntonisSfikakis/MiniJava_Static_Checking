class TestExprIntAnd {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int x;
    boolean b;
    b = (1 < 2) && 5;
    return 1;
  }
}
