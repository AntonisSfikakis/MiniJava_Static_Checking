class TestReturnArrayFromInt {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int[] a;
    a = new int[5];
    return a;
  }
}
