package pers.fxtack.puffer.util.io;

import pers.fxtack.puffer.util.annotation.PropertiesUsed;
import pers.fxtack.puffer.util.SingletonProperties;

import java.io.*;
import java.util.HashMap;

/**
 * 该类是一个用于读取 KeyValue 文件的输入类。本质上是通过 objectInputStream 读取文件, 获取 HashMap 对象。
 * 并将该对象进行缓存管理。
 * <br><br>
 * 该类使用了 @UserProperties 注解, 表示该类依赖注入了相关配置。并且在实例代码块中读取了相关配置。
 *
 * @author Fxtack
 * @version 1.0
 */
@PropertiesUsed
public class KeyValueReader implements Closeable {

    private final SingletonProperties properties = SingletonProperties.getInstance();
    private final int bufferSize = Integer.valueOf(properties.getProperty("buffer_size"));

    private final HashMap<String, String> keyValueTable;
    private ObjectInputStream objectInputStream;

    public KeyValueReader(File file) throws IOException, ClassNotFoundException {
        objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file),bufferSize));
        keyValueTable = (HashMap<String, String>) objectInputStream.readObject();
    }

    public HashMap<String, String> readKeyValueTable() {
        return keyValueTable;
    }

    @Override
    public void close() throws IOException {
        objectInputStream.close();
    }
}
