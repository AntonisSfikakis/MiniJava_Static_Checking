class TestReturnExprFail1 {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  int y;

  public boolean badReturnAdd() {
    return x + y;
  }
}
