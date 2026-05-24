class TestExprCompareBool {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    boolean b;
    b = true < false;
    return 1;
  }
}
