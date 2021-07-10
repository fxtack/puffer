package pers.fxtack.puffer.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * 自定义组件。
 * <br><br>
 *
 * 该类是一个自定义组件, 继承自 AnchorPane。该组件是用于显示与编辑一个键值对数据的组件。
 *
 * <br><br>
 * 该自定义界面的相关详细信息与 Tab 组件的相关说明, 参见项目说明文档。
 * @author Fxtack
 * @version 1.0
 */
public class KeyValueEditor extends AnchorPane implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private TextField text_key;

    @FXML
    private TextField text_value;

    public KeyValueEditor() {
        this("","");
    }

    public KeyValueEditor(String key, String value) {
        loadFXML();
        this.text_key.setText(key);
        this.text_value.setText(value);
        text_key.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case RIGHT:
                    text_value.requestFocus();
                    break;
            }
        });

        text_value.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case LEFT:
                    text_key.requestFocus();
                    break;
            }
        });
    }

    private void loadFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("keyValueEditer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public TextField getText_key() {
        return this.text_key;
    }

    public TextField getText_value() {
        return this.text_value;
    }

    public boolean isFocus() {
        return text_key.isFocused() || text_value.isFocused();
    }

    public void setOnContentChanged(EventHandler event) {
        text_key.setOnKeyPressed(event);
        text_value.setOnKeyPressed(event);
    }

    @Override
    public void requestFocus() {
        text_key.requestFocus();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
