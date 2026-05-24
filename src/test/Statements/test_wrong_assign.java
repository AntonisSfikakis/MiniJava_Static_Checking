class TestWrongAssign {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int x;
    x = true;
    return x;
  }
}
