package pers.fxtack.puffer.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import pers.fxtack.puffer.service.DataServer;
import pers.fxtack.puffer.service.KeyValueData;
import pers.fxtack.puffer.util.Revocable;
import pers.fxtack.puffer.util.annotation.PropertiesUsed;
import pers.fxtack.puffer.util.exception.RepeatKeyException;
import pers.fxtack.puffer.util.io.KeyValueIOFactory;
import pers.fxtack.puffer.util.tool.Binder;
import pers.fxtack.puffer.view.CatalogLabel;
import pers.fxtack.puffer.view.dialog.AlertDialog;
import pers.fxtack.puffer.view.dialog.FileChooserDialog;
import pers.fxtack.puffer.view.tab.DataEditTab;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import pers.fxtack.puffer.util.tool.Tool;
import pers.fxtack.puffer.main.Main;
import pers.fxtack.puffer.util.ExceptionHandler;
import pers.fxtack.puffer.util.SingletonProperties;

/**
 * 该类是主界面的 Controller, 主界面内的一切容器与组件的事件都在该类内进行注册。
 * 该类内有一个 {@link KeyValueIOFactory} 对象, 用于管理 (包括打开文件数据) 界面内的业务数据与资源。
 * 该类内部还有两个 HashMap 局部变量: getTabByCatalogLabel 和  getCatalogLabelByTab。这两个变量都是为了管理主界面中
 * TabPane 界面的 Tab 与左置界面 VBox 中的 CatalogLabel 的映射关系, 使之一一对应。
 * <br><br>
 * 该类是在主界面的 FXML 文件中实现绑定与联系的。可以查看 view 包下的 main.fxml, 其根容器标签 {@code <BorderPane>}
 * 中的 fx:controller 属性值为 "org.dataLoadFx.controller.Controller", 这实现了 Controller 的绑定。
 * 对于该类中使用 {@code @FXML} 注解的组件变量, 正对应了 main.fxml 中的组件。 要求是变量名称必须与 main.fxml
 * 中定义的组件 id 一一对应, 且完全相同 (为了区分一般变量与 fxml 组件变量, 作者使用了不同的命名规范, fxml 组件变量多用有下划线)。
 * 主界面的加载在 {@link Main Main} 中实现。
 * <br><br>
 * 该类中涉及到的控件使用与设计细节可参照项目说明文件。
 * @author Fxtack
 * @version 1.0
 */
@PropertiesUsed
public class Controller implements Initializable {

    // KeyValueIOFactory 用于管理界面中打开文件的 IO
    private KeyValueIOFactory keyValueIOFactory;
    // 获得单例的异常处理器，所有异常将会通过该类的对象进行处理
    private ExceptionHandler handler = ExceptionHandler.getInstance();

    // 单例的配置
    private SingletonProperties properties = SingletonProperties.getInstance();

    private final String
            ACTION_DELETE_WARNING_LABEL = properties.getProperty("action_delete_warning_label"),
            ACTION_DELETE_LABEL     = properties.getProperty("action_delete_label"),
            CANNOT_ACTION_LABEL     = properties.getProperty("cannot_action_label"),
            CATALOGLABEL_MOUSE_EXITED_STYLE     = properties.getProperty("cataloglabel_mouse_exited_style"),
            CATALOGLABEL_MOUSE_ENTERED_STYLE    = properties.getProperty("cataloglabel_mouse_entered_style"),
            CONTEXTMENU_CLOSE       = properties.getProperty("contextmenu_close"),
            CONTEXTMENU_SAVE        = properties.getProperty("contextmenu_save"),
            DIALOG_ABOUT_HEADER     = properties.getProperty("dialog_about_header"),
            DIALOG_ABOUT_TITLE      = properties.getProperty("dialog_about_title"),
            DIALOG_CLOSE_ALL_TITLE  = properties.getProperty("dialog_close_all_title"),
            DIALOG_CLOSE_HEADER     = properties.getProperty("dialog_close_header"),
            DIALOG_CLOSE_MAIN_TITLE = properties.getProperty("dialog_close_main_title"),
            DIALOG_CLOSE_TAB_TITLE  = properties.getProperty("dialog_close_tab_title"),
            DIALOG_SAVE_ILLEGAL_TITLE   = properties.getProperty("dialog_save_illegal_title"),
            DIALOG_SAVE_ILLEGAL_HEADER  = properties.getProperty("dialog_save_illegal_header"),
            FILE_NEW_LABEL          = properties.getProperty("file_new_label"),
            FILE_SAVE_NEW_PATH      = properties.getProperty("file_save_new_path"),
            FILE_OPEN_CHOOSER_TITLE = properties.getProperty("file_open_chooser_title"),
            FILE_OPEN_DEFAULT_PATH  = properties.getProperty("file_open_default_path"),
            FILE_OPEN_LABEL         = properties.getProperty("file_open_label"),
            FILE_SAVE_CHOOSER_TITLE = properties.getProperty("file_save_chooser_title"),
            FILE_SAVE_LABEL         = properties.getProperty("file_save_label"),
            HELLO_LABEL             = properties.getProperty("hello_label"),
            HELLO_AUTHOR_NAME       = properties.getProperty("hello_author_name"),
            HELLO_VERSION           = properties.getProperty("hello_version"),
            LABEL                   = properties.getProperty("label"),
            MENU_FILE               = properties.getProperty("menu_file"),
            MENU_ITEM_OPEN          = properties.getProperty("menu_item_open"),
            MENU_ITEM_NEW_FILE      = properties.getProperty("menu_item_new"),
            MENU_ITEM_SAVE          = properties.getProperty("menu_item_save"),
            MENU_ITEM_SAVE_AS       = properties.getProperty("menu_item_save_as"),
            MENU_ITEM_CLOSE_ALL     = properties.getProperty("menu_item_close_all"),
            MENU_ITEM_UNDO          = properties.getProperty("menu_item_undo"),
            MENU_ITEM_REDO          = properties.getProperty("menu_item_redo"),
            MENU_ITEM_ADD           = properties.getProperty("menu_item_add"),
            MENU_ITEM_DELETE        = properties.getProperty("menu_item_delete"),
            MENU_ITEM_ABOUT         = properties.getProperty("menu_item_about"),
            MENU_EDIT               = properties.getProperty("menu_edit"),
            MENU_HELP               = properties.getProperty("menu_help"),
            NEW_FILE_DEFAULT_NAME   = properties.getProperty("new_file_default_name"),
            NEW_FILE_DEFAULT_SIZE   = properties.getProperty("new_file_default_size"),
            SAVE_NO_NEED_LABEL      = properties.getProperty("save_no_need_label"),
            SAVE_ILLEGAL_LABEL      = properties.getProperty("save_illegal_label"),
            TAB_CLOSE_LABEL         = properties.getProperty("tab_close_label");

    // 根面板
    @FXML
    private BorderPane border_pane;

    // 顶置 MenuBar 菜单条
    @FXML
    private MenuBar menu_bar;

    // 菜单选项
    @FXML
    private Menu menu_file,menu_edit,menu_help;

    // 下拉菜单选项 MenuItem
    @FXML
    private MenuItem menu_item_open,
            menu_item_save,
            menu_item_save_as,
            menu_item_close_all,
            menu_item_undo,
            menu_item_redo,
            menu_item_add,
            menu_item_delete,
            menu_item_about,
            menu_item_new;

    // 中置面板的 SplitPane
    @FXML
    private SplitPane split_pane;

    // 二分面板的左右部分的 AnchorPane
    @FXML
    private AnchorPane anchor_pane_left,anchor_pane_right;

    // 二分面板右侧的 tab 面板
    @FXML
    private TabPane tab_pane;

    // 欢迎 Tab
    @FXML
    private Tab tab_hello;

    // 提示栏标签
    @FXML
    private Label label,hello_label, hello_author_name, hello_version;

    // splitPane 左侧子面板
    @FXML
    private VBox left_vbox;

    // 主界面内映射 CatalogLabel 与 DataEditTab
    private HashMap<CatalogLabel, Tab> getTabByCatalogLabel;
    // mappingManager 的翻转键值对映射
    private HashMap<Tab, CatalogLabel> getCatalogLabelByTab;


    private AlertDialog d = new AlertDialog.Builder(Alert.AlertType.WARNING)
            .setTitle(DIALOG_SAVE_ILLEGAL_TITLE)
            .setHeaderText(DIALOG_SAVE_ILLEGAL_HEADER)
            .build();

    /**
     * 初始化，实现自 Initializable
     *
     * @param location 来自实现的接口的抽象方法
     * @param resources 来自实现的接口的抽象方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hello_label.setText(HELLO_LABEL);
        hello_author_name.setText(HELLO_AUTHOR_NAME);
        hello_version.setText(HELLO_VERSION);

        getTabByCatalogLabel = new HashMap<>();
        getCatalogLabelByTab = new HashMap<>();
        try {
            keyValueIOFactory = KeyValueIOFactory.getInstance();
            setUpMenu();
        } catch (Exception e) {
            handler.handle(e);
        }
    }

    /**
     * 该方法仅在本 Controller 中使用，用于将 DataEditTab 加入到 主界面的 TabPane 中。
     * 并且实现左置文件目录标签的映射关系 (每打开一个 Tab 左置页面应当显示该 Tab 所打开的文件名字与大小, 并且点击时要跳转到相应的 Tab, 等功能)。
     * 该方法还对要加入的 DataEditTab 与 CatalogLabel 对象进行了一些事件注册。原码中进行详细说明。
     *
     * @param catalogLabel 进行关联的 CatalogLabel 对象
     * @param dataEditTab 进关联的 DataEditTab 对象
     */
    private void addTabToPane(CatalogLabel catalogLabel, Tab dataEditTab) {
        // 关闭事件
        EventHandler event_close = event -> {
            if(((DataEditTab)dataEditTab).isSaved()) {
                closeFileAction(catalogLabel,dataEditTab);
            }else{
                new AlertDialog.Builder(Alert.AlertType.CONFIRMATION)
                        .setTitle(DIALOG_CLOSE_TAB_TITLE)
                        .setHeaderText(DIALOG_CLOSE_HEADER)
                        .setOkHandler(() -> {
                            closeFileAction(catalogLabel,dataEditTab);
                        })
                        .setCancelHandler(()->{
                            event.consume();
                        })
                        .build()
                        .showDialog();
            }
        };

        // 设置文件目录标签的序号
        catalogLabel.setNumber(getTabByCatalogLabel.size()+1);

        // 文件目录标签点击后, 将选择到其对应的 Tab
        catalogLabel.setOnMouseClicked(event1 -> {
            tab_pane.getSelectionModel().select(dataEditTab);
        });

        // 文件目录标签在鼠标经过的视觉效果
        catalogLabel.setOnMouseExited(event1 -> {
            catalogLabel.setStyle(CATALOGLABEL_MOUSE_EXITED_STYLE);
        });
        catalogLabel.setOnMouseEntered(event1 -> {
            catalogLabel.setStyle(CATALOGLABEL_MOUSE_ENTERED_STYLE);
        });

        // 文件目录标签的右键菜单
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem_close = new MenuItem(CONTEXTMENU_CLOSE);
        MenuItem menuItem_save = new MenuItem(CONTEXTMENU_SAVE);
        contextMenu.getItems().addAll(menuItem_close, menuItem_save);

        // 右击菜单中的选项事件
        menuItem_save.setOnAction(event -> {
            // 如果选中的 Tab 是 DataEditTab 则进行文件保存, 否则不进行文件保存
            if(dataEditTab instanceof DataEditTab) {
                //如果选中的 Tab 对应的文件是 null, 则说明是一个新建的 Tab, 需要选定文件目录进行保存
                if(((DataEditTab) dataEditTab).getFilePath() != null) {
                    // 如果不是新建的 Tab 直接进行保存
                    try {
                        ((DataEditTab) dataEditTab).saveKeyValue();
                    } catch (RepeatKeyException e) {
                        label.setText(SAVE_ILLEGAL_LABEL);
                        d.showDialog();
                    }
                    label.setText(FILE_SAVE_LABEL);
                }else {
                    // 文件选择器选择保存的文件路径
                    new FileChooserDialog.Builder()
                            .setTitle(FILE_SAVE_CHOOSER_TITLE)
                            .setFilePath(new File(FILE_SAVE_NEW_PATH))
                            .setFileHandler(file -> {

                                DataServer dataServer = keyValueIOFactory.openFile(file);
                                if(dataServer instanceof KeyValueData){
                                    saveNewFileAction(dataEditTab,file,dataServer);
                                }
                            })
                            .build()
                            .showSaveDialog(border_pane.getScene().getWindow());
                    label.setText(FILE_SAVE_LABEL);
                }
            }else {
                this.label.setText(SAVE_NO_NEED_LABEL);
            }
        });
        menuItem_close.setOnAction(event_close);

        // 为文件目录标签设置右击菜单
        catalogLabel.setContextMenu(contextMenu);

        // 在 Tab 关闭时的事件。弹出对话框询问用户关闭
        dataEditTab.setOnCloseRequest(event_close);

        // mappingManager 和 mappingManagerReverse 是用于管理 CatalogLabel 和 Tab 的数据结构。
        // 被加入到界面中并产生关联的 CatalogLabel 和 Tab 将会同时被加入到 mappingManager 和 mappingManagerReverse 中
        getTabByCatalogLabel.put(catalogLabel, dataEditTab);
        getCatalogLabelByTab.put(dataEditTab, catalogLabel);

        // 工具类 Binder 用于封装了控件之间的操作, 该处就是将 catalogLabel 加入到了 left_vbox 中, 下一句同理。
        Binder.bindLabelToVBox(catalogLabel, left_vbox);
        Binder.bindTabToPane((DataEditTab)dataEditTab, tab_pane);
    }

    // 设置顶级容器 Stage
    public void setUpStage() {
        if(border_pane.getScene() == null) {
            return;
        }else {
            // 在关闭整个应用是询问用户是否确定要关闭界面
            border_pane.getScene().getWindow().setOnCloseRequest(event -> {
                new AlertDialog.Builder(Alert.AlertType.CONFIRMATION)
                        .setTitle(DIALOG_CLOSE_MAIN_TITLE)
                        .setHeaderText(DIALOG_CLOSE_HEADER)
                        .setOkHandler(()->{
                            ((Stage)border_pane.getScene().getWindow()).close();
                        })
                        .setCancelHandler(()->{
                            event.consume();
                        })
                        .build();
            });
        }
    }

    // 设置 TabPane
    public void setTabPane() {

    }

    // 设置菜单
    private void setUpMenu() throws Exception{

        menu_file.setText(MENU_FILE);
            menu_item_open.setText(MENU_ITEM_OPEN);
            menu_item_new.setText(MENU_ITEM_NEW_FILE);
            menu_item_save.setText(MENU_ITEM_SAVE);
            menu_item_save_as.setText(MENU_ITEM_SAVE_AS);
            menu_item_close_all.setText(MENU_ITEM_CLOSE_ALL);
        menu_edit.setText(MENU_EDIT);
            menu_item_undo.setText(MENU_ITEM_UNDO);
            menu_item_redo.setText(MENU_ITEM_REDO);
            menu_item_add.setText(MENU_ITEM_ADD);
            menu_item_delete.setText(MENU_ITEM_DELETE);
        menu_help.setText(MENU_HELP);
            menu_item_about.setText(MENU_ITEM_ABOUT);

        label.setText(LABEL);

        menu_item_about.setOnAction(event ->{
            new AlertDialog.Builder(Alert.AlertType.INFORMATION)
                    .setTitle(DIALOG_ABOUT_TITLE)
                    .setHeaderText(DIALOG_ABOUT_HEADER)
                    .build()
                    .showDialog();
        });

        menu_item_new.setOnAction(event -> {
            DataEditTab det = new DataEditTab();
            CatalogLabel catalogLabel = new CatalogLabel(NEW_FILE_DEFAULT_NAME, NEW_FILE_DEFAULT_SIZE);
            det.setText(NEW_FILE_DEFAULT_NAME);
            addTabToPane(catalogLabel, det);
            this.label.setText(FILE_NEW_LABEL);
        });

        menu_item_open.setOnAction(event -> {
            new FileChooserDialog.Builder()
                    .setTitle(FILE_OPEN_CHOOSER_TITLE)
                    .addExtensionFilters(new FileChooser.ExtensionFilter("dat","*.dat"))
                    .setFilePath(new File(FILE_OPEN_DEFAULT_PATH))
                    .setFileHandler(file -> {
                        DataServer keyValueData = keyValueIOFactory.openFile(file);
                        CatalogLabel catalogLabel = new CatalogLabel(file.getName(), Tool.formatFileSize(file.length()));

                        if(keyValueData instanceof KeyValueData) {
                             DataEditTab det = new DataEditTab((KeyValueData) keyValueData, file.toString());
                            det.setText(file.getName());
                            addTabToPane(catalogLabel, det);
                        }
                    })
                    .build()
                    .showOpenDialog(border_pane.getScene().getWindow());

            this.label.setText(FILE_OPEN_LABEL);
        });

        menu_item_save.setOnAction(event -> {
            Tab willSaveTab = tab_pane.getSelectionModel().getSelectedItem();
            if(willSaveTab instanceof DataEditTab) {
                if(((DataEditTab) willSaveTab).getFilePath() != null) {
                    try {
                        ((DataEditTab) willSaveTab).saveKeyValue();
                    } catch (RepeatKeyException e) {
                        label.setText(SAVE_ILLEGAL_LABEL);
                        d.showDialog();
                    }
                    label.setText(FILE_SAVE_LABEL);
                }else {
                    new FileChooserDialog.Builder()
                            .setTitle(FILE_SAVE_CHOOSER_TITLE)
                            .setFilePath(new File(FILE_SAVE_NEW_PATH))
                            .setFileHandler(file -> {
                                DataServer dataServer = keyValueIOFactory.openFile(file);
                                if(dataServer instanceof KeyValueData){
                                    saveNewFileAction(willSaveTab,file,dataServer);
                                }
                            })
                            .build()
                            .showSaveDialog(border_pane.getScene().getWindow());
                    label.setText(FILE_SAVE_LABEL);
                }
            }else {
                this.label.setText(SAVE_NO_NEED_LABEL);
            }
        });

        menu_item_save_as.setOnAction(event -> {
            Tab willSaveTab = tab_pane.getSelectionModel().getSelectedItem();
            if (willSaveTab instanceof DataEditTab) {
                new FileChooserDialog.Builder()
                        .setTitle(FILE_SAVE_CHOOSER_TITLE)
                        .setFilePath(new File(FILE_SAVE_NEW_PATH))
                        .setFileHandler(file -> {
                                DataServer dataServer = keyValueIOFactory.openFile(file);
                                if(((DataEditTab) willSaveTab).getFilePath() != null) {
                                    ((KeyValueData)dataServer).coverData(((DataEditTab) willSaveTab).getKeyValueData().getAllKV());
                                    try {
                                        dataServer.submit();
                                    } catch (IOException e) {
                                        handler.handle(e);
                                    }
                                    keyValueIOFactory.closeFile(file);
                                }else {
                                    saveNewFileAction(willSaveTab,file,dataServer);
                                }
                        })
                        .build()
                        .showSaveDialog(border_pane.getScene().getWindow());
                label.setText(FILE_SAVE_LABEL);
            } else {
                label.setText(CANNOT_ACTION_LABEL);
            }
        });

        menu_item_close_all.setOnAction(event -> {
            new AlertDialog.Builder(Alert.AlertType.CONFIRMATION)
                    .setTitle(DIALOG_CLOSE_ALL_TITLE)
                    .setHeaderText(DIALOG_CLOSE_HEADER)
                    .setOkHandler(()->{
                        tab_pane.getTabs().clear();
                        left_vbox.getChildren().clear();
                        keyValueIOFactory.clear();
                        getTabByCatalogLabel.clear();
                        getCatalogLabelByTab.clear();
                    })
                    .setCancelHandler(()->{
                        event.consume();
                    })
                    .build().showDialog();
        });

        menu_item_add.setOnAction(event -> {
            Tab tab = tab_pane.getSelectionModel().getSelectedItem();
            if(tab instanceof DataEditTab) {
                ((DataEditTab)tab).addNewKeyValue();
                menu_item_undo.setDisable(false);
                menu_item_redo.setDisable(true);
            }else{
                label.setText(CANNOT_ACTION_LABEL);
            }
        });

        menu_item_delete.setOnAction(event -> {
            Tab selectTab = tab_pane.getSelectionModel().getSelectedItem();
            if(selectTab instanceof DataEditTab) {
                boolean var = ((DataEditTab) selectTab).deleteKeyValue();
                if(var) {
                    label.setText(ACTION_DELETE_LABEL);
                    menu_item_undo.setDisable(false);
                    menu_item_redo.setDisable(true);
                }else {
                    label.setText(ACTION_DELETE_WARNING_LABEL);
                }
            }else {
                label.setText(CANNOT_ACTION_LABEL);
            }
        });

        menu_item_undo.setDisable(true);
        menu_item_redo.setDisable(true);
        menu_item_undo.setOnAction(event -> {
            Tab selectTab = tab_pane.getSelectionModel().getSelectedItem();
            if(selectTab instanceof DataEditTab && selectTab instanceof Revocable) {
                ((DataEditTab) selectTab).undo();
                if(((DataEditTab) selectTab).canRedo()) {
                    menu_item_redo.setDisable(false);
                }else {
                    menu_item_redo.setDisable(true);
                }
                if(((DataEditTab) selectTab).canUndo()) {
                    menu_item_undo.setDisable(false);
                }else {
                    menu_item_undo.setDisable(true);
                }
            }else {
                label.setText(CANNOT_ACTION_LABEL);
            }
        });

        menu_item_redo.setOnAction(event -> {
            Tab selectTab = tab_pane.getSelectionModel().getSelectedItem();
            if(selectTab instanceof DataEditTab && selectTab instanceof Revocable) {
                ((DataEditTab) selectTab).redo();
                if(((DataEditTab) selectTab).canRedo()) {
                    menu_item_redo.setDisable(false);
                }else {
                    menu_item_redo.setDisable(true);
                }
                if(((DataEditTab) selectTab).canUndo()) {
                    menu_item_undo.setDisable(false);
                }else {
                    menu_item_undo.setDisable(true);
                }
            }else {
                label.setText(CANNOT_ACTION_LABEL);
            }
        });
    }

    // 复用代码方法
    private void saveNewFileAction(Tab willSaveTab, File file, DataServer dataServer) {
        ((DataEditTab) willSaveTab).setFilePath(file.toString());
        ((DataEditTab) willSaveTab).setKeyValueData((KeyValueData) dataServer);
        try {
            ((DataEditTab) willSaveTab).saveKeyValue();
        } catch (RepeatKeyException e) {
            label.setText(SAVE_ILLEGAL_LABEL);
            d.showDialog();
        }
        getCatalogLabelByTab.get(willSaveTab).setFileName(((DataEditTab) willSaveTab).getFileName());
        getCatalogLabelByTab.get(willSaveTab).setFileSize(file.length());
    }

    // 复用代码方法
    private void closeFileAction(CatalogLabel catalogLabel, Tab dataEditTab) {
        tab_pane.getTabs().remove(dataEditTab);
        left_vbox.getChildren().remove(catalogLabel);
        if(((DataEditTab)dataEditTab).getFilePath() != null) {
            keyValueIOFactory.closeFile(((DataEditTab)dataEditTab).getFilePath());
        }
        getTabByCatalogLabel.remove(catalogLabel);
        getCatalogLabelByTab.remove(dataEditTab);
        this.label.setText(TAB_CLOSE_LABEL);
    }
}