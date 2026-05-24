class TestExprEdge {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  boolean flag;
  int[] arr;

  public int testNestedArith() {
    int a;
    int b;
    a = (1 + 2) * 3;
    b = (a - 1) + (a * 2);
    return b;
  }

  public int testCompareInIf() {
    int result;
    if ((x + 1) < (x * 2))
      result = 1;
    else
      result = 0;
    return result;
  }

  public int testNotCompare() {
    int result;
    if (!(x < 5))
      result = 1;
    else
      result = 0;
    return result;
  }

  public int testAndCompare() {
    int result;
    if ((x < 10) && (x < 20))
      result = 1;
    else
      result = 0;
    return result;
  }

  public int testArrayInExpr() {
    int result;
    arr = new int[10];
    arr[0] = 5;
    result = arr[0] + 1;
    return result;
  }

  public int testPrintExpr() {
    System.out.println(x + 1);
    return 1;
  }

  public int testWhileComplex() {
    int i;
    i = 0;
    while (!((i + 1) < 10))
      i = i + 1;
    return i;
  }
}
