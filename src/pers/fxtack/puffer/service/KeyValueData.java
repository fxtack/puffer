package pers.fxtack.puffer.service;

import pers.fxtack.puffer.util.exception.RepeatKeyException;
import pers.fxtack.puffer.util.io.KeyValueReader;
import pers.fxtack.puffer.util.io.KeyValueWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 该类实现了 DataServer, 具体化了数据的操作, 实现了所有的数据操作。
 * 该类通过 {@link KeyValueReader} 和 {@link KeyValueWriter} 来实现对数据文件进行操作。
 * 该类内缓存了一个 HashMap 对象用来管理键值对数据信息。但是在 KeyValueReader 和 KeyValueWriter 中还有一个 HashMap 的数据管理缓存。
 * 该处的设计有些冗余。后续会进行修改以提升性能。项目使用者也可以自己尝试进行优化修改。
 *
 * @author Fxtack
 * @version 1.0
 */
public class KeyValueData implements DataServer {

    private KeyValueReader keyValueReader;
    private KeyValueWriter keyValueWriter;
    private Map<String, String> data;

    public KeyValueData(KeyValueWriter keyValueWriter, KeyValueReader keyValueReader) {
        this.keyValueReader = keyValueReader;
        this.keyValueWriter = keyValueWriter;
        try {
            data = keyValueReader.readKeyValueTable();
            keyValueReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getAllKV() {
        return data;
    }

    @Override
    public Map<String, String> getKVByK(String key) {
        if(data.get(key) == null) {
            return null;
        }else {
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
            result.put(key, data.get(key));
            return result;
        }
    }

    @Override
    public int deleteKVbyK(String key) {
        if(data.get(key) == null) {
        }else {
            data.remove(key);
        }
        return data.size();
    }

    @Override
    public void clearAll() {
        data.clear();
    }

    @Override
    public int addKV(String key, String value) throws RepeatKeyException {
        if(data.get(key) == null) {
            data.put(key, value);
        }else {
            throw new RepeatKeyException(key, value);
        }
        return 0;
    }

    @Override
    public int updateKVByK(String key, String value) throws Exception{
        if(data.get(key) == null) {
            throw new Exception("不存在该键，无法更新值，可进行添加操作.");
        }else {
            data.put(key,value);
        }
        return data.size();
    }

    @Override
    public void submit() throws IOException {
        this.keyValueWriter.writeKeyValueTable(this.data);
        this.keyValueWriter.flush();
    }

    public void coverData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void close() throws IOException {
        this.keyValueWriter.writeKeyValueTable(this.data);
        this.keyValueWriter.close();
        this.keyValueReader.close();
        this.keyValueWriter = null;
        this.keyValueReader = null;
    }
}
