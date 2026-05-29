class TestOverrideNoArgsWrong {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B extends A {
  public boolean foo() {
    return true;
  }
}
