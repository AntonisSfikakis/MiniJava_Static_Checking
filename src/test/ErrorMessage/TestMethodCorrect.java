class TestMethodCorrect {
    public static void main (String[] args) {
    }
} 


class A {
  int x;
  public int getX() { return x; }
  public int add(int a, int b) { return a + b;}
}

class B {
  A myA;

  public int test() {
   int result;
   result = myA.getX();
   result = myA.add(1,2);
   return result;
  }

} 
