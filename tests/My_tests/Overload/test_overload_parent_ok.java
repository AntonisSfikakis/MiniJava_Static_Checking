class TestOverloadParentOk {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class Y {
  public int val() {
    return 2;
  }
}

class A {
  public int foo(X x) {
    return 1;
  }
}

class B extends A {
  public int foo(Y y) {
    return 2;
  }
}
