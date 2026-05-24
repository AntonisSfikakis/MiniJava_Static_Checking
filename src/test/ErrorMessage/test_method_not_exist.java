class TestMethodNotExist {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    return 1;
  }
}

class B {
  A myA;

  public int bad() {
    int result;
    result = myA.bar();
    return result;
  }
}
