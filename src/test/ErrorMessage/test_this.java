class TestThis {
  public static void main(String[] args) {
  }
}

class A {
  int x;

  public int getX() {
    return x;
  }

  public int doubleX() {
    int result;
    result = (this.getX()) + (this.getX());
    return result;
  }
}
