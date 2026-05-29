class TestAssignSupertype {
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
}

class C {
  B myB;

  public int bad() {
    myB = new A();
    return 1;
  }
}
