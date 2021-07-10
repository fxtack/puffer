package pers.fxtack.puffer.util.exception;

import javafx.scene.Node;

/**
 * 重复键异常。一般情况下, 向 Map 子类对象中加入重复键值是可以的, 键值不会发生改变而值会覆盖原有的值。
 * 但这不符合项目的功能需要, 所以当输入重复键值时声明一个异常抛出, 方便处理。
 *
 * @author Fxtack
 * @version 1.0
 */
public class RepeatKeyException extends Exception{

    private String key, value;

    private Node key_editor;

    public RepeatKeyException() {}

    public RepeatKeyException(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getKey_editor() {
        return key_editor;
    }

    public void setKey_editor(Node key_editor) {
        this.key_editor = key_editor;
    }
}
