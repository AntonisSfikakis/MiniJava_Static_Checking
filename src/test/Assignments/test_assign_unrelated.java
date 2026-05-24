class TestAssignUnrelated {
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
  A myA;

  public int bad() {
    myA = new B();
    return 1;
  }
}
