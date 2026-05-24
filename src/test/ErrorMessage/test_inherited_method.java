class TestInheritedMethod {
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

  public int test() {
    int result;
    result = myB.foo();
    return result;
  }
}
