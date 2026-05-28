class TestOverloadMixed {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class Y extends X {
  public int val() {
    return 2;
  }
}

class A {
  public int foo(X x, int n) {
    return 1;
  }

  public int foo(Y y, boolean n) {
    return 2;
  }
}
