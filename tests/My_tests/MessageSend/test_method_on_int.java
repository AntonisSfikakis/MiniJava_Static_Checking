class TestMethodOnInt {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int bad() {
    int result;
    result = x.foo();
    return result;
  }
}
