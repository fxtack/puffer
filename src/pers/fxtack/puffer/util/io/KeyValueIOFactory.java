package pers.fxtack.puffer.util.io;

import pers.fxtack.puffer.service.DataServer;
import pers.fxtack.puffer.service.KeyValueData;
import pers.fxtack.puffer.util.ExceptionHandler;

import java.io.*;
import java.util.HashMap;

/**
 * 该类是一个用于管理文件 IO 资源的类, 通过该类可以打开文件的输入输出资源与关闭文件的输入输出资源。
 * @author Fxtack
 * @version 1.0
 */

public class KeyValueIOFactory implements ResourceFactory<DataServer> {

    /**
     * 该 HashMap 是作为管理文件与文件输入输出资源的管理池, 键为文件的 CanonicalPath 路径 (file.getCanonicalPath()的值),
     * 值为 DataServer。{@link DataServer} 是一个接口, 该接口中定义了对键值对数据的增删查改方法。该项目中仅有该接口的一个实现类
     * KeyValueData, 该类封装了 {@link KeyValueReader} 与 {@link KeyValueWriter}。
     */
    private final HashMap<String, DataServer> filePool = new HashMap<>();

    private ExceptionHandler handler = ExceptionHandler.getInstance();

    private static KeyValueIOFactory keyValueIOFactory;

    public static KeyValueIOFactory getInstance() {
        if(keyValueIOFactory == null) {
            keyValueIOFactory = new KeyValueIOFactory();
        }
        return keyValueIOFactory;
    }

    private KeyValueIOFactory() { }

    /**
     * 清空 filePool 并关闭其中的相关资源。
     * @throws IOException IO异常
     */
    @Override
    public void clear() {
        try {
            for (String file : filePool.keySet()) {
                DataServer data = filePool.get(file);
                data.close();
            }
        }catch (IOException e) {
            handler.handle(e);
        }
        filePool.clear();
    }

    /**
     * 打开文件。该方法将打开文件的输入输出资源并封装为 KeyValueData 对象, 存入 filePool 中进行管理。
     * @param file 要打开的文件
     * @return  返回数据操作对象
     * @throws IOException IO 异常
     * @throws ClassNotFoundException 类找不到异常
     */
    @Override
    public DataServer openFile(File file) {
        try {
            // 返回了KeyValueData对象, 这里直接耦合了KeyValueData
            DataServer keyValueData = new KeyValueData(new KeyValueWriter(file), new KeyValueReader(file));
            filePool.put(file.getCanonicalPath(), keyValueData);
            System.out.println("已打开文件:" + file.getCanonicalPath());
            return keyValueData;
        }catch (IOException | ClassNotFoundException e) {
            handler.handle(e);
        }
        return null;
    }

    public DataServer openFile(String file) {
        return openFile(new File(file));
    }

    /**
     * 通过传入的 file 对象获取该对象的数据操作对象。
     * @param file 文件对象
     * @return 数据操作对象
     * @throws IOException IO 异常
     */
    @Override
    public DataServer getFile(File file) {
        try {
            return filePool.get(file.getCanonicalPath());
        }catch (IOException e){
            handler.handle(e);
        }
        return null;
    }

    public DataServer getFile(String file) {
        return getFile(new File(file));
    }

    /**
     * 关闭文件与相关的文件资源。
     * @param file 要关闭的文件资源。
     * @throws IOException IO 异常
     */
    @Override
    public void closeFile(File file) {
        try {
            filePool.get(file.getCanonicalPath()).close();
            filePool.remove(file.toString());
            System.out.println("已关闭文件:" + file.getCanonicalPath());
        } catch (IOException e) {
            handler.handle(e);
        }
        /*
            该处显现了作者对 JVM 与 GC 知识的薄弱。作者曾听说只要一个对象不再被任何引用所指 (即所有原来指向该对象的引用为 null)。
            该对象就会在 GC 心情好的时候被回收, 从而节省内存资源。这样写可以确保该方法的重载方法传入传入的 File 对象能够有机会被回收 (吗?)。
        */
        file = null;
    }

    public void closeFile(String file) {
        closeFile(new File(file));
    }
}
