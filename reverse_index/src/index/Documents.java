package index;

import java.io.File;

/**
 * 文档的初步处理
 * Created by Henry on 2015/6/6.
 */
public class Documents {

    /**
     * 静态全局区域
     */
    static long total=0;

    public static void setTotal(long docs){
        total = docs;
    }

    static File direction;
    static File[] files;

    public static void initial(String dir){
        direction =  new File(dir);
        files = direction.listFiles();
    }

    /**
     * 类区域
     */
}
