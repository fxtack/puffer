package pers.fxtack.puffer.view.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import pers.fxtack.puffer.util.functionalinterface.AlertHandler;

import java.util.Optional;

/**
 * 重新封装了 Alert 类, 实现了建造者设计模式, 并且在建造者 Builder 类中可使用链式表达式。
 *
 * @author Fxtack
 * @version 1.0
 */
public class AlertDialog {

    private final Alert dialog;
    private AlertHandler okHandler, cancelHandler;

    private Optional<ButtonType> result;

    private AlertDialog(Alert.AlertType type) {
        this.dialog = new Alert(type);
    }

    public static class Builder {

        private final Alert.AlertType type;
        private String title;
        private String headerText;
        private String contentText;
        private AlertHandler okHandler, cancelHandler;

        public Builder(Alert.AlertType type) {
            this.type = type;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setHeaderText(String headerText) {
            this.headerText = headerText;
            return this;
        }

        public Builder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public Builder setCancelHandler(AlertHandler alertCancel) {
            this.cancelHandler = alertCancel;
            return this;
        }

        public Builder setOkHandler(AlertHandler alertOk) {
            this.okHandler = alertOk;
            return this;
        }
        public AlertDialog build() {
            AlertDialog dialog = new AlertDialog(this.type);
            dialog.setTitle(this.title);
            dialog.setContentText(this.contentText);
            dialog.setHeaderText(this.headerText);
            dialog.setOkHandler(this.okHandler);
            dialog.setCancelHandler(this.cancelHandler);
            return dialog;
        }
    }

    public void showDialog() {
        result = dialog.showAndWait();
        if (okHandler != null && result.get() == ButtonType.OK) {
            okHandler.handle();
        }else if(cancelHandler != null && result.get() == ButtonType.CANCEL) {
            cancelHandler.handle();
        }else {

        }
    }

    public void setTitle(String title) {
        this.dialog.setTitle(title);
    }

    public void setHeaderText(String headerText) {
        this.dialog.setHeaderText(headerText);
    }

    public void setContentText(String contentText) {
        this.dialog.setContentText(contentText);
    }

    public void setOkHandler(AlertHandler okHandler) {
        this.okHandler = okHandler;
    }

    public void setCancelHandler(AlertHandler cancelHandler) {
        this.cancelHandler = cancelHandler;
    }
}

