class TestOverloadSameClass {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x) {
    return x;
  }

  public int foo(int x, int y) {
    return x + y;
  }
}
