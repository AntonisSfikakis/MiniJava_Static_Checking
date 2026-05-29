class TestAssignTypes {
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

class C extends B {
  public int foo() {
    return 3;
  }
}

class TestOk {
  A a1;
  A a2;
  B b1;

  public int testSubtype() {
    a1 = new B();
    return 1;
  }

  public int testDeepSubtype() {
    a2 = new C();
    return 1;
  }

  public int testSameType() {
    b1 = new B();
    return 1;
  }
}
