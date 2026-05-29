class TestBlock {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int testBlock() {
    int y;
    {
      x = 1;
      y = 2;
    }
    return y;
  }

  public int testNestedWhile() {
    int i;
    boolean f;
    int j;
    i = 0;
    j = 0;
    while (i < 10) {
      j = j + i;
      i = i + 1;
    }
    return j;
  }
}
