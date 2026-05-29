class TestNewCall {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 42;
  }
}

class B {
  public int test() {
    int result;
    result = new A().foo();
    return result;
  }
}
