package pers.fxtack.puffer.util.io;

import pers.fxtack.puffer.util.annotation.PropertiesUsed;
import pers.fxtack.puffer.util.SingletonProperties;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类是一个用于写出 KeyValue 文件的输出类。本质上是通过管理缓存的 HashMap 对象来保存于管理数据,
 * 再通过 ObjectOutputStream 将该对象写出到文件中实现。
 * <br><br>
 * 该类使用了 @UserProperties 注解, 表示该类依赖注入了相关配置。并且在实例代码块中读取了相关配置。
 *
 * @author Fxtack
 * @version 1.0
 */

@PropertiesUsed
public class KeyValueWriter implements Closeable {

    private final SingletonProperties properties = SingletonProperties.getInstance();
    private final int bufferSize = Integer.valueOf(properties.getProperty("buffer_size"));

    private File file;
    private Map<String, String> keyValueTable;
    private ObjectOutputStream objectOutputStream;
    private KeyValueReader keyValueReader;

    public KeyValueWriter(File file) throws IOException, ClassNotFoundException {
        this.file = file;
        if(!file.exists()) {
            keyValueTable = new HashMap<>();
            flush();
        }else {
            keyValueReader = new KeyValueReader(file);
            keyValueTable = keyValueReader.readKeyValueTable();
            keyValueReader.close();
        }
    }

    public void writeKeyValue(String key, String value) {
        keyValueTable.put(key,value);
    }

    public void writeKeyValueTable(Map<String, String> data) {
        this.keyValueTable = data;
    }


    /**
     * 该方法的命名可额有歧义, 项目使用者可根据自身需求重构该方法名。
     * 该方法的作用是将当前类中管理的数据写出到文件中。在用使用者看来, 该方法实现了一次文件保存。
     * @throws IOException
     */
    public void flush() throws IOException {
        objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(this.file),bufferSize));
        objectOutputStream.writeObject(keyValueTable);
        objectOutputStream.close();
    }

    @Override
    public void close() throws IOException {
        flush();
        keyValueTable = null;
        file = null;
    }
}
