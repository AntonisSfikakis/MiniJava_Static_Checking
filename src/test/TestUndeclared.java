class TestUndeclared {
  public static void main(String[] args) {
  }
}

class A {
  public int foo() {
    int x;
    x = y + 1;
    return x;
  }
}
