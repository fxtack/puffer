package pers.fxtack.puffer.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import pers.fxtack.puffer.util.tool.Tool;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 自定义组件。
 * <br><br>
 *
 * 该类是一个自定义控件, 继承自 HBox。该控件中含有 3 个 Label 子组件: catalog_number, catalog_file_name, catalog_file_size。
 * 该类的组件表示一个目录标签。用于放置在主界面的左置 VBox 中, 表示一个打开的文件。
 * <br><br>
 *
 * 该自定义界面的相关详细信息说明, 参见项目说明文档。
 * @author Fxtack
 * @version 1.0
 */
public class CatalogLabel extends HBox implements Initializable {

    @FXML
    Label catalog_number;

    @FXML
    Label catalog_file_name;

    @FXML
    Label catalog_file_size;

    public CatalogLabel(String number, String fileName, String fileSize) {
        loadFXML();
        HBox.setHgrow(this, Priority.ALWAYS);
        catalog_number.setText(number);
        catalog_file_name.setText(fileName);
        catalog_file_size.setText(fileSize);
    }

    public void setContextMenu(ContextMenu menu) {
        catalog_number.setContextMenu(menu);
        catalog_file_name.setContextMenu(menu);
        catalog_file_size.setContextMenu(menu);
    }

    public CatalogLabel(String fileName, String fileSize) {
        this("", fileName, fileSize);
    }

    public void setNumber(int number) {
        this.catalog_number.setText(number+".");
    }

    public void setNumber(String number) {
        this.catalog_number.setText(number);
    }

    public void setFileName(String fileName) {
        this.catalog_file_name.setText(fileName);
    }

    public void setFileSize(String fileSize) {
        this.catalog_file_size.setText(fileSize);
    }

    public void setFileSize(long fileSize) {
        this.catalog_file_size.setText(Tool.formatFileSize(fileSize));
    }

    private void loadFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("catalogLabel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
