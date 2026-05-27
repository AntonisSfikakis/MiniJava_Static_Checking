class TestNoOverride {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B extends A {
  public int bar() {
    return 2;
  }

  public boolean baz(int x) {
    return x < 10;
  }
}
