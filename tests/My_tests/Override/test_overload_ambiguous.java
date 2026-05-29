class TestOverloadAmbiguous {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B extends A {
  public int foo() {
    return 2;
  }
}

class C {
  public int bar(A x) {
    return 1;
  }

  public int bar(B x) {
    return 2;
  }
}
