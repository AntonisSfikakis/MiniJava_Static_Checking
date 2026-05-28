class TestInheritedOverload {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(boolean x) {
    return 1;
  }
}

class B extends A {
  public int foo(int x) {
    return 2;
  }
}

class C {
  B myB;

  public int testCallInherited() {
    int result;
    result = myB.foo(true);
    return result;
  }

  public int testCallOwn() {
    int result;
    result = myB.foo(5);
    return result;
  }
}
