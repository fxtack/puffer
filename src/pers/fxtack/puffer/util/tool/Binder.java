package pers.fxtack.puffer.util.tool;

import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pers.fxtack.puffer.view.CatalogLabel;
import pers.fxtack.puffer.view.tab.DataEditTab;

/**
 *  Binder 类是一个工具类。
 *  提供的静态方法是为了将组件加入容器，绑定控件与控件或控件与容器之间，调整其间的大小关系。
 *
 * @author Fxtack
 * @version Fxtack
 */

public class Binder {

    /**
     * 用于绑定 Tab 与 TabPane
     * @param tab 绑定在 TabPane 中的 Tab
     * @param tabPane Tab 容器
     * @param isSelect 是否在 Tab 加入 TabPane 之后进行选中
     * */
    public static void bindTabToPane(DataEditTab tab, TabPane tabPane, boolean isSelect) {
        tabPane.getTabs().add(tab);
        if(isSelect) {
            tabPane.getSelectionModel().select(tab);
        }
        if(tab.getTabPane() == null) {
            throw new NullPointerException("Tab 无父容器 TabPane 可绑定");
        }else {
            ((AnchorPane)tab.getContent()).prefWidthProperty().bind(tab.getTabPane().widthProperty());
            ((AnchorPane)tab.getContent()).prefHeightProperty().bind(tab.getTabPane().heightProperty().subtract(40.0));
        }
    }

    /**
     * bindTabToPane 方法重载
     * @param tab
     * @param tabPane
     */
    public static void bindTabToPane(DataEditTab tab, TabPane tabPane) {
        bindTabToPane(tab, tabPane, true);
    }

    /**
     * 该方法用于将 CatalogLabel 加入到 VBox 中。此处的 VBox 是主界面的左置容器，用于展示打开的文件目录，一条文件目录则是一个 CatalogLabel 对象
     * @param catalogLabel （自定义组件类）加入到 VBox 中的目录标签
     * @param vBox 主界面的左置容器 VBox
     */
    public static void bindLabelToVBox(CatalogLabel catalogLabel, VBox vBox) {
        vBox.getChildren().add(catalogLabel);
        catalogLabel.prefWidthProperty().bind(vBox.widthProperty());
    }

}
