class TestWrongCount {
  public static void main(String[] args) {

  } 
}

class A {
  public int foo(int x) {
    return x;
  }
} 

class B {
  A myA;

  public int bad() {
    int result;
    result = myA.foo(1,2);
    return result;
  }
}
