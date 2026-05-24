class TestReturnAndAsInt {
  public static void main(String[] args) {
  }
}

class A {
  boolean a;
  boolean b;

  public int badReturnAnd() {
    return a && b;
  }
}
