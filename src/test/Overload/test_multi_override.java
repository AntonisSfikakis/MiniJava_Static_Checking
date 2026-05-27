class TestMultiOverride {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }

  public boolean bar(int x) {
    return x < 10;
  }

  public int baz(int a, int b) {
    return a + b;
  }
}

class B extends A {
  public int foo() {
    return 2;
  }

  public boolean bar(int x) {
    return x < 20;
  }

  public int baz(int a, int b) {
    return a * b;
  }
}
