package pers.fxtack.puffer.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pers.fxtack.puffer.controller.Controller;
import pers.fxtack.puffer.util.ExceptionHandler;
import pers.fxtack.puffer.util.SingletonProperties;
import pers.fxtack.puffer.util.annotation.PropertiesUsed;


/**
 * 这是程序的入口。该类上标注了注解 @UserProperties, 这意味着在该类内使用了
 * {@link SingletonProperties} 来进行依赖注入。
 * <br>
 * <br>
 * 该类继承了 {@link javafx.application.Application}, 并重写了其中的 start() 方法, 并且在主方法内执行了其中的
 * launch() 方法。这是一个标准的 JavaFx 程序的入口结构。其中 start() 方法传入的 primaryStage 参数对象为将要显示
 * 窗体的顶级容器对象, 类型为 {@link javafx.stage.Stage}。
 *
 *
 * @author Fxtack
 * @version 1.0
 *
 */
@PropertiesUsed
public class Main extends Application {

    private SingletonProperties properties = SingletonProperties.getInstance();

    private static ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    /**
     * 方法开始通过单例配置对象注入了相关配置。
     * <br>
     * <br>
     * 接下来完成了 FXML 文件的加载与读取，从而获得带有完整控件的一个容器。其中通过了当前类路径获取位于 /view 下的 sample.fxml
     * 文件, 并创建出 FXMLoader 对象。
     * <br>
     * <br>
     * 对 FXMLoader 对象调用 load() 方法时，将会返回 sample.fxml 文件中定义界面的根容器对象 root。
     * 因为 sample.fxml 中定义的界面根容器为 {@link javafx.scene.layout.BorderPane BorderPane} 类型, 因此程序实际返回的是一个 BorderPane 对象
     * <br><br>
     * 接着通过 FXMLoader 对象获取了该界面的 Controller 对象, 该对象是一个自定义的类 {@link Controller} 的对象。
     * <br><br><pre>在尚未了结整个项目情况前不建议考虑以下设计原因
     *
     * 这里获取该对象并在接下来调用了 Controller 中的 setUpStage() 方法。该处的特殊设计是为了设置顶级容器 primaryStage 的事件, 因为只有当程序
     * 将 root 对象用于创建 Scene 对象，并再将 Scene 对象通过 setScene(Scene) 方法传入 primaryStage 对象时, root 才与 primaryStage 产生
     * 联系, 从而通过 Controller 中的方法对访问到顶级容器对象, 并为之设置事件。若这么做, 直接在 Controller 初始化过程中获取顶级容器对象值将为 null
     * (实际上在 Controller 对象中获取顶级对象容器也要先得到根容器对象(根 BorderPane 对象), 再获取该对象的上级容器 Scene 对象, 再通过 Scene
     * 对象获取 Stage 对象。在 Controller 初始化过程中访问到 Scene 对象时已为 null, 因此无法获取更上一级的 Stage 容器对象)。
     * <br><br></pre>
     * 对顶级容器的最小宽度、最小高度以及标题进行设置。创建二级容器并设入顶级容器, 通过调用顶级容器的 show() 方法展示出窗体。
     *
     * @param primaryStage 窗体的最高级容器
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        final int width               = Integer.parseInt(properties.getProperty("width"));
        final int height              = Integer.parseInt(properties.getProperty("height"));
        final int minWidth            = Integer.parseInt(properties.getProperty("min_width"));
        final int minHeight           = Integer.parseInt(properties.getProperty("min_height"));
        final String title            = properties.getProperty("title");
        final boolean isResizeable    = Boolean.valueOf(properties.getProperty("is_resizeable"));

        // 创建 FXMLLoader 加载解析主界面
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/main.fxml"));
        // 通过 FXMLLoader 获取根容器
        Parent root = fxmlLoader.load();
        // 通过 FXMLLoader 获取主界面的控制器
        Controller controller = fxmlLoader.getController();

        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
        primaryStage.setResizable(isResizeable);
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, width, height));
        controller.setUpStage();
        primaryStage.show();
    }

    /**
     * 主方法调用的是父类的启动方法。依然是程序的入口。
     * @param args args
     */

    public static void main(String[] args) {
        try{
            launch(args);
        }catch (Exception e){
            exceptionHandler.handle(e);
        }
    }
}
