class TestWrongArgs {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x, int y) {
    return x + y;
  }
}

class B {
  A myA;

  public int bad() {
    int result;
    result = myA.foo(1, true);
    return result;
  }
}
