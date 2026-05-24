class TestExprMulBool {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int x;
    x = true * false;
    return x;
  }
}
