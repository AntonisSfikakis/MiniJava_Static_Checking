class TestReturnSubtype {
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
  public A getA() {
    return new B();
  }
}
