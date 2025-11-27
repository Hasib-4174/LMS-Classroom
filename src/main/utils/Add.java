package utils;

public class Add {
    public static int add(int ... n) {
        int sum = 0;
        for(int i=0;i<n.length;i++)
            sum+=n[i];
        return sum;
    }
}
