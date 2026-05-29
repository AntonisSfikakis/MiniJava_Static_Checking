class TestLookupOnInt {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int x;
    int y;
    x = 5;
    y = x[0];
    return y;
  }
}
