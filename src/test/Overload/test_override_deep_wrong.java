class TestOverrideDeepWrong {
  public static void main(String[] args) {
  }
}

class A {
  public int foo(int x) {
    return x;
  }
}

class B extends A {
  public int bar() {
    return 1;
  }
}

class C extends B {
  public boolean foo(int x) {
    return true;
  }
}
