package pers.fxtack.puffer.util;

import java.util.LinkedList;

/**
 * 该类通过链表保存状态对象来实现撤回与重做, 详细原理可查看相关说明文件。
 *
 * @param <S> 状态对象的类型
 *
 * @author Fxtack
 * @version 1.0
 */
public class UndoHandler<S> {

    int size = 5;

    LinkedList<S> stateLink = new LinkedList<>();
    int pointer = -1;

    public UndoHandler(S initState) {
        stateLink.addFirst(initState);
        pointer ++;
    }

    public void addState(S state) {
        if(stateLink.size() >= size) {
            stateLink.removeFirst();
            stateLink.add(++pointer, state);
        }
        stateLink.add(++pointer, state);
    }

    public S getPreviousState() {
        if(hasPreviousState()) {
            System.out.println(stateLink.get(pointer));
            return stateLink.get(--pointer);
        }
        return null;
    }

    public S getNextState() {
        if(hasNextState()) {
            return stateLink.get(++pointer);
        }
        return null;
    }

    public boolean hasPreviousState() {
        if(pointer == 0) {
            return false;
        }
        return true;
    }

    public boolean hasNextState() {
        if(pointer >= stateLink.size()-1) {
            return false;
        }
        return true;
    }
}
