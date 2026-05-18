class TestCircular {
  public static void main(String[] args) {
  }
}

class A extends B {
  public int foo() {
    return 1;
  }
}

class B extends A {
  public int bar() {
    return 2;
  }
}
