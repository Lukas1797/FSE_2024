package util;

public class Assert {

    public static void notNull(Object o){
        if (o==null) throw new IllegalArgumentException("Reffernce must not be null");
    }


}
