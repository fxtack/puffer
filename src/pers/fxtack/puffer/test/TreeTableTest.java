package pers.fxtack.puffer.test;


import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * TreeTableView 使用案例。该组件可用来优化主界面的左置文件目录。
 *
 * @author Fxtack
 * @version 1.0
 */
public class TreeTableTest extends Application {

    //数据集合
    List files = Arrays.asList(
            new File("123.txt", "1KB"),
            new File("223.txt", "3KB"),
            new File("323.txt", "15KB"),
            new File("423.txt", "12KB"),
            new File("523.txt", "7KB"));

    //根行
    final TreeItem root = new TreeItem<>(new File("Folder", ""));

    @Override
    public void start(Stage stage) {

        //默认展开
        root.setExpanded(true);

        //lambda 表达式将每个 file 加入到树的根节行中
        files.stream().forEach((file) -> {
            root.getChildren().add(new TreeItem<>(file));
        });

        //加入行
        TreeTableColumn<File, String> fileColumn = new TreeTableColumn<>("文件");
        fileColumn.setPrefWidth(140);
        fileColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<File, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getName())
        );

        //加入行
        TreeTableColumn<File, String> fileSizeColumn = new TreeTableColumn<>("文件大小");
        fileSizeColumn.setPrefWidth(110);
        fileSizeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<File, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getSize())
        );


        TreeTableView treeTableView = new TreeTableView<>();
        treeTableView.setRoot(root);

        treeTableView.getColumns().setAll(fileColumn, fileSizeColumn);

        stage.setTitle("Tree Table View Sample");
        Group sceneRoot = new Group();
        Scene scene = new Scene(sceneRoot, 250, 400);
        sceneRoot.getChildren().add(treeTableView);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(TreeTableTest.class, args);
    }

    public class File {
        private SimpleStringProperty name;
        private SimpleStringProperty size;
        private File(String name, String size) {
            this.name = new SimpleStringProperty(name);
            this.size = new SimpleStringProperty(size);
        }
        public String getName() {
            return name.get();
        }
        public String getSize() {
            return size.get();
        }
    }
}
