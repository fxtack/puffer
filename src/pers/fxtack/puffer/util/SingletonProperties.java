package pers.fxtack.puffer.util;

import java.io.*;
import java.util.Properties;

/**
 * 该类是一个配置类, 特殊的是这是一个单例的类。这使得所有要使用配置类依赖注入的地方, 使用的都是同一个配置对象。
 * <br><br>
 * <b>该类不是线程安全的。</b>
 *
 * @author Fxtack
 * @version 1.0
 */
public class SingletonProperties extends Properties {

    private static SingletonProperties singletonProperties;

    public static SingletonProperties getInstance() {

        // 这是一个懒汉式的单例
        if(singletonProperties == null) {
            singletonProperties = new SingletonProperties();
            try {
                // 配置文件是直接耦合在代码中的, 因此不能随意改变配置文件的路径
                singletonProperties.load(new InputStreamReader(SingletonProperties.class.getResourceAsStream("../config/config.properties"), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return singletonProperties;
    }

    private SingletonProperties() {}
}
