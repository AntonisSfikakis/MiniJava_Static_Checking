class TestExprNotInt {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    boolean b;
    b = !(1 + 2);
    return 1;
  }
}
