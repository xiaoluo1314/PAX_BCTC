package cn.com.pax.table;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 文件过滤器
 *
 * @author luohl
 * @create 2017-12-06-13:48
 */
public class MyFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {

        System.out.println("f.getName()的值是：---"+ f.getName() + "，当前方法=accept.MyFileFilter()");
        if (f.isDirectory()){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
    