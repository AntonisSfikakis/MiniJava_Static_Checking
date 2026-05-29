class TestExprMethodMix {
  public static void main(String[] args) {
  }
}

class A {
  public boolean getFlag() {
    return true;
  }

  public int bad() {
    int x;
    x = (this.getFlag()) + 1;
    return x;
  }
}
