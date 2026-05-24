class TestExprWrong1 {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int x;
    x = (true) + 1;
    return x;
  }
}
