class TestReturnCompareAsInt {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  int y;

  public int badReturnCompare() {
    return x < y;
  }
}
