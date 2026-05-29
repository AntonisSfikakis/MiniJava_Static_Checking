class TestOverloadUnrelated {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B {
  public int bar() {
    return 2;
  }
}

class C {
  public int test(A x) {
    return 1;
  }

  public int test(B x) {
    return 2;
  }
}
