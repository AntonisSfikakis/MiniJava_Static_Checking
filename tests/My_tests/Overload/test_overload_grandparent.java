class TestOverloadGrandparent {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(A x) {
    return 1;
  }
}

class B extends A {
  public int bar() {
    return 2;
  }
}

class C extends B {
  public int foo(B x) {
    return 3;
  }
}
