class TestArrayAssignBool {
  public static void main(String[] args) {
  }
}

class A {
  public int bad() {
    int[] a;
    a = new int[5];
    a[0] = true;
    return 1;
  }
}
