class TestTypeError {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int bad() {
    int a;
    boolean b;
    a = a + b;
    return a;
  }
}
