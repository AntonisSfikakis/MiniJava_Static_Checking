class TestReturnClassAsInt {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B {
  public int bad() {
    return new A();
  }
}
