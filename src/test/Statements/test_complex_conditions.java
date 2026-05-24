class TestComplexConditions {
  public static void main(String[] args) {
  }
}

class A {
  int x;
  int y;
  boolean flag;

  public int testIfCompare() {
    int result;
    if (x < y)
      result = 1;
    else
      result = 2;
    return result;
  }

  public int testIfAnd() {
    int result;
    if (flag && (x < y))
      result = 1;
    else
      result = 0;
    return result;
  }

  public int testIfNot() {
    int result;
    if (!flag)
      result = 1;
    else
      result = 0;
    return result;
  }

  public int testWhile() {
    int i;
    i = 0;
    while (i < 10)
      i = i + 1;
    return i;
  }

  public int testNestedIf() {
    int result;
    if (flag)
      if (x < y)
        result = 1;
      else
        result = 2;
    else
      result = 3;
    return result;
  }
}
