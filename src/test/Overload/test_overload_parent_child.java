class TestOverloadParentChild {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(A x) {
    return 1;
  }
}

class B extends A {
  public int foo(B x) {
    return 2;
  }
}
