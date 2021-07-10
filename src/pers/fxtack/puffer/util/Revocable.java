package pers.fxtack.puffer.util;

/**
 * 可撤回控件需要实现该接口
 *
 * @author Fxtack
 * @version 1.0
 */
public interface Revocable {

    void undo();

    void redo();

    boolean canUndo();

    boolean canRedo();
}
