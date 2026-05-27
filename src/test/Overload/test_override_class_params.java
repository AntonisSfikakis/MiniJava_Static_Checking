class TestOverrideClassParams {
  public static void main(String[] args) {
  }
}

class X {
  public int val() {
    return 1;
  }
}

class A {
  public int foo(X obj, int n) {
    return n;
  }
}

class B extends A {
  public int foo(X obj, int n) {
    return n + 1;
  }
}
