package pers.fxtack.puffer.util;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import pers.fxtack.puffer.util.annotation.PropertiesUsed;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * 该类用于处理所有本程序中可能出现的异常。
 * 但该类本身发生的异常不会被本身处理。其异常信息将会直接输出在控制台上。
 * 该类使用了配置文件注入相关配置。
 * <br><br>
 * 该类将会把程序运行过程中出现的错误以提示框的形式展示给使用者，并且会将详细的异常栈信息输出在错误日志中。
 * 错误日志的路径是可改变的。通过在配置文件中修改 exception_handler_log_path 属性既可。并且，
 * 出于对用户或开发者的习惯不同考虑，可以在配置文件中指定是否使用绝对路径。
 * 仅需将配置文件中的 exception_handler_log_path_is_absolute 该为 true，就会开启绝对路径使用。
 * 此时 exception_handler_log_path 必须填写错误日志的绝对路径。默认情况下使用相对路径，
 * exception_handler_log_path_is_absolute 值为 false。
 * <br><br>
 * ExceptionHandler 使用 handle 处理异常时可以选择是否要在控制台上输出错误信息。
 * 可以在配置文件中配置 exception_handler_is_system_print 为 true, 使得异常信息输出在控制台上.false 则不输出。
 * 也可以在使用 handle 方法时设置 isSystemPrint 参数为 true 或 false 来决定是否在控制台上输出异常信息。
 *
 * @author Fxtack
 * @version 1.0
 */
@PropertiesUsed
public class ExceptionHandler {

    private static SingletonProperties properties = SingletonProperties.getInstance();
    private static ExceptionHandler exceptionHandler;

    private String title            = properties.getProperty("exception_handler_title");
    private String headerText       = properties.getProperty("exception_handler_header");
    private String contentText      = properties.getProperty("exception_handler_content");
    private String messageColor     = properties.getProperty("exception_handler_message_color");
    private String detailLabel      = properties.getProperty("exception_handler_detail");
    private String errorLogPath     = properties.getProperty("exception_handler_log_path");
    private String dividingLine     = properties.getProperty("exception_handler_log_dividing_line");
    private boolean isAbsolutePath  = Boolean.parseBoolean(properties.getProperty("exception_handler_log_path_is_absolute"));
    private boolean isSystemPrint   = Boolean.parseBoolean(properties.getProperty("exception_handler_is_system_print"));

    /**
     * 该静态方法是一个用于实现单例模式的静态方法。
     *
     * @return 返回单例的 ExceptionHandler 对象
     */
    public static ExceptionHandler getInstance() {
        if(exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler();
        }
        return exceptionHandler;
    }

    private ExceptionHandler() { }

    /**
     * 该方法用于处理异常。
     *
     * @param e 要处理的异常对象
     */
    public void handle(Exception e) {
        handle(e, isSystemPrint);
    }

    /**
     * 该方法用于处理异常。
     *
     * @param e 要处理的异常对象
     * @param isSystemPrint 若为 true 则异常信息也会在控制台输出，否则不会在控制台输出。
     */
    public void handle(Exception e, boolean isSystemPrint) {

        if(isSystemPrint) {
            e.printStackTrace();
        }

        // 创建对话框对象
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // 设置对话框标题
        alert.setTitle(title);

        // 设置头信息
        alert.setHeaderText(headerText);

        // 设置内容信息
        alert.setContentText(contentText);

        // 异常界面标签
        Label error_message = new Label();
        Label error_message_detail = new Label(detailLabel);

        // 若异常无附带信息则显示 "(无异常提示信息)", 否则显示异常附带信息
        if(e.getMessage() == null) {
            error_message.setText("(无异常提示信息)");
        }else {
            error_message.setText(e.getMessage());
        }

        // 设置标签的显示
        error_message.setStyle("-fx-text-fill: "+messageColor);
        error_message.setPadding(new Insets(0,0,10,3));
        error_message_detail.setPadding(new Insets(0,0,10,3));

        // 读取异常栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        // 获取系统时间
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();

        // 组合了时间的异常信息
        String exceptionText = "["+df.format(date)+"]\n"+sw.toString();

        try {

            // 输出到错误日志
            File errorLog = null;

            // 判断配置中是否使用绝对路径
            if(isAbsolutePath) {
                errorLog = new File(errorLogPath);
            }else {
                errorLog = new File(errorLogPath);
            }

            // 如果目录下没有日志文件，则生成一个新的日志文件
            if(!errorLog.exists()) {
                errorLog.createNewFile();
            }

            // 字节流输出
            FileWriter writer = new FileWriter(errorLog, true);
            writer.append(dividingLine+"\n" + exceptionText);
            writer.close();
        }catch (IOException ex){
            System.out.println("异常处理器日志输出错误");
            ex.printStackTrace();
        }

        // 创建一个文本域用于显示异常的栈信息
        TextArea textArea = new TextArea(exceptionText);

        // 是否可以编辑
        textArea.setEditable(false);

        // 是否自动转行
        textArea.setWrapText(true);

        // GridPane内控件绑定
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        // 将组建加入到 GridPane 面板中
        GridPane expContent = new GridPane();
        expContent.add(error_message_detail, 0, 1);
        expContent.add(error_message, 0,0);
        expContent.add(textArea, 0, 2);

        // 设置错误面板
        alert.getDialogPane().setExpandableContent(expContent);

        // 展开错误面板
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }
}
