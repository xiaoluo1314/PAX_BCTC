package cn.com.pax.engine;

import cn.com.pax.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

/**
 * 读业务逻辑配置文件
 *
 * @author luohl
 * @create 2017-12-12-20:53
 */
public class ReadEngineCfg {
    private static Properties properties = new Properties();
    private static final HashMap<String, BaseMoveWork> nameToRealObject = new HashMap<String, BaseMoveWork>();

    public static Properties getProperties() {
        return properties;
    }

    public static HashMap<String, BaseMoveWork> getNameToRealObject() {
        return nameToRealObject;
    }
    public static void readEng(){
        try {
            FileInputStream inputStream = new FileInputStream(new File(Constant.ENGINECFG));
            InputStreamReader reader = new InputStreamReader(inputStream,"utf-8");
            properties.load(reader);
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
    