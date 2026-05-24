class TestShadowing {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int testShadow() {
    int x;
    x = 10;
    return x;
  }

  public int testField() {
    return x;
  }
}
