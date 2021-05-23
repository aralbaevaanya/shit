public class example5 {
    public int a(int x, int y) {
        if( x > 0 && y > 0 && x < y){
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