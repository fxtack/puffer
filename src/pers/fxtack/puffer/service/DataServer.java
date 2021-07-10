package pers.fxtack.puffer.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 该接口声明数据操作的方法。
 *
 * @author Fxtack
 * @version 1.0
 */
public interface DataServer extends Closeable {

    Map<String, String> getAllKV();

    Map<String, String> getKVByK(String key);

    int deleteKVbyK(String key);

    void clearAll();

    int addKV(String key, String value) throws Exception;

    int updateKVByK(String key, String value) throws Exception;

    void submit() throws IOException;
}
