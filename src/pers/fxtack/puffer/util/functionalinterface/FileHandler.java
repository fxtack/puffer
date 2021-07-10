package pers.fxtack.puffer.util.functionalinterface;

import java.io.File;

/**
 * 函数式接口, 在 {@link pers.fxtack.puffer.view.dialog.FileChooserDialog FileChooserDialog } 中使用。
 *
 * @author Fxtack
 * @version 1.0
 */
@FunctionalInterface
public interface FileHandler {
    void handle(File file);
}
