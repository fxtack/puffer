package pers.fxtack.puffer.view.tab;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pers.fxtack.puffer.service.KeyValueData;
import pers.fxtack.puffer.util.ExceptionHandler;
import pers.fxtack.puffer.util.Revocable;
import pers.fxtack.puffer.util.UndoHandler;
import pers.fxtack.puffer.util.exception.RepeatKeyException;
import pers.fxtack.puffer.util.tool.Tool;
import pers.fxtack.puffer.view.KeyValueEditor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * 自定义组件。
 * <br><br>
 * 该类是一个自定义组件, 继承自 Tab。Tab 是一种需要放入到 TabPane 中的组件。
 *
 * <br><br>
 * 该自定义界面的相关详细信息与 Tab 组件的相关说明, 参见项目说明文档。
 * @author Fxtack
 * @version 1.0
 */

public class DataEditTab extends Tab implements Initializable, Revocable {

    @FXML
    private VBox scroll_vbox;

    @FXML
    private ScrollPane scroll_pane;


    private KeyValueData keyValueData;
    private final UndoHandler<ArrayList> undoHandler;
    private String filePath;
    private boolean isSaved = true;

    public DataEditTab(KeyValueData keyValueData) {
        this(keyValueData, "");
    }

    public DataEditTab(KeyValueData keyValueData, String filePath) {
        loadFXML();
        undoHandler = new UndoHandler<>(copyState());
        this.filePath = filePath;
        this.keyValueData = keyValueData;
        for(String key : keyValueData.getAllKV().keySet()) {
            scroll_vbox.getChildren().add(getNewKeyValueNoState(key, keyValueData.getAllKV().get(key)));
        }

    }

    public DataEditTab() {
        loadFXML();
        undoHandler = new UndoHandler<>(copyState());
        addNewKeyValue();
    }

    private void loadFXML(){
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dataEditTab.fxml"));

        fxmlLoader.setRoot(anchorPane);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            super.setContent(anchorPane);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void setKeyValueData(KeyValueData keyValueData) {
        this.keyValueData = keyValueData;
    }

    public KeyValueData getKeyValueData() { return this.keyValueData; }

    public void setFilePath(String filePath) {
        this.filePath =filePath;
        this.setText(getFileName());
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() { return Tool.filePathToName(filePath); }

    public boolean isSaved() {
        return this.isSaved;
    }

    public void addNewKeyValue() {
        addKeyValue(null, null);
    }

    private KeyValueEditor getNewKeyValueNoState(String key, String value) {
        KeyValueEditor keyValueEditor = new KeyValueEditor();
        keyValueEditor.getText_key().setText(key);
        keyValueEditor.getText_value().setText(value);
        keyValueEditor.setOnContentChanged(event -> {
            isSaved = false;
        });
        return keyValueEditor;
    }

    public void addKeyValue(String key, String value) {
        isSaved = false;
        KeyValueEditor keyValueEditor = getNewKeyValueNoState(key, value);
        scroll_vbox.getChildren().add(keyValueEditor);
        keyValueEditor.requestFocus();
        fitScroll();

        undoHandler.addState(copyState());
    }

    public boolean deleteKeyValue() {
        isSaved = false;
        if(this.getFocused() == null) {
            return false;
        }else {
            scroll_vbox.getChildren().remove(this.getFocused());
        }

        undoHandler.addState(copyState());

        return true;
    }

    public void fitScroll() {
        scroll_pane.setVvalue(scroll_pane.getVmax());
    }

    public Node getFocused() {
        for(Node node : scroll_vbox.getChildren()) {
            if(node instanceof KeyValueEditor) {
                if(((KeyValueEditor) node).isFocus()){
                    return node;
                }
            }
        }
        return null;
    }

    public void saveKeyValue() throws RepeatKeyException{
        isSaved = true;
        keyValueData.clearAll();
        for(Node node : this.scroll_vbox.getChildren()){
            if(node instanceof KeyValueEditor) {
                try {
                    keyValueData.addKV(((KeyValueEditor) node).getText_key().getText(), ((KeyValueEditor) node).getText_value().getText());
                }catch (RepeatKeyException e){
                    ((KeyValueEditor)node).getText_key().requestFocus();
                    e.setKey_editor(node);
                    throw e;
                }
            }
        }
        try {
            keyValueData.submit();
        } catch (IOException e) {
            ExceptionHandler.getInstance().handle(e);
        }
    }

    @Override
    public void undo() {
        isSaved = false;
        if(undoHandler.hasPreviousState()) {
            scroll_vbox.getChildren().setAll(undoHandler.getPreviousState());
        }
    }

    @Override
    public void redo() {
        isSaved = false;
        if(undoHandler.hasNextState()) {
            scroll_vbox.getChildren().setAll(undoHandler.getNextState());
        }
    }

    private ArrayList copyState() {
        ArrayList<Node> list = new ArrayList<>();
        for(Node n : scroll_vbox.getChildren()) {
            list.add(n);
        }
        return list;
    }

    @Override
    public boolean canUndo() {
        return undoHandler.hasPreviousState();
    }

    @Override
    public boolean canRedo() {
        return undoHandler.hasNextState();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
