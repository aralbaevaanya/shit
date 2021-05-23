public class example4 {
    public int a(int x) {
        if( x > 0 ){
            if(x > 100){
                x = 10;
                return x;
            }
            return 200;
        }else {
            return 3;
        }
    }
}