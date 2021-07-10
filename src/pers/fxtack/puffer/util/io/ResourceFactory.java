package pers.fxtack.puffer.util.io;

import java.io.File;

/**
 * 资源工厂的抽象接口，本项目中只有 {@link KeyValueIOFactory} 实现该接口
 *
 * @param <S> 资源类型
 */
public interface ResourceFactory<S> {

    S openFile(File file);
    S getFile(File file);
    void closeFile(File file);
    void clear();
}
