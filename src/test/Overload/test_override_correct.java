class TestOverrideCorrect {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x, int y) {
    return x + y;
  }

  public boolean bar() {
    return true;
  }

  public int noArgs() {
    return 1;
  }
}

class B extends A {
  public int foo(int a, int b) {
    return a * b;
  }

  public boolean bar() {
    return false;
  }

  public int noArgs() {
    return 2;
  }
}
