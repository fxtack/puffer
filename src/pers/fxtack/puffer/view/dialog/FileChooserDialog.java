package pers.fxtack.puffer.view.dialog;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import pers.fxtack.puffer.util.functionalinterface.FileHandler;

import java.io.File;
import java.util.ArrayList;

/**
 * 重新封装了 FileChooserDialog 类, 实现了建造者设计模式, 并且在建造者 Builder 类中可使用链式表达式。
 *
 * @author Fxtack
 * @version 1.0
 */
public class FileChooserDialog {

    private final FileChooser fileChooser;
    private File file;
    private FileHandler fileHandler;

    public FileChooserDialog() {
        fileChooser = new FileChooser();
        fileChooser.initialFileNameProperty().setValue("NewFile.dat");
    }

    public static class Builder {
        private String title;
        private File filePath;
        private FileHandler fileHandler;
        private ArrayList<FileChooser.ExtensionFilter> extensionFilters = new ArrayList();

        public Builder() {

        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setFilePath(File filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setFileHandler(FileHandler fileHandler) {
            this.fileHandler = fileHandler;
            return this;
        }

        public Builder setExtensionFilters(ArrayList<FileChooser.ExtensionFilter> extensionFilters) {
            this.extensionFilters = extensionFilters;
            return this;
        }

        public Builder addExtensionFilters(FileChooser.ExtensionFilter extension) {
            this.extensionFilters.add(extension);
            return this;
        }

        public FileChooserDialog build() {
            FileChooserDialog dialog = new FileChooserDialog();
            dialog.setTitle(this.title);
            dialog.setInitialDirectory(this.filePath);
            dialog.setFiletHandler(this.fileHandler);
            dialog.setExtensionFilters(this.extensionFilters);
            return dialog;
        }
    }

    public void showSaveDialog(Window window) {
        file = fileChooser.showSaveDialog(window);
        if(fileHandler != null) {
            doHandle();
        }
    }

    public void showOpenDialog(Window window) {
        file = fileChooser.showOpenDialog(window);
        if(fileHandler != null) {
            doHandle();
        }
    }

    public void doHandle() {
        if(file != null) {
            fileHandler.handle(file);
        }
    }

    public void setTitle(String title) {
        this.fileChooser.setTitle(title);
    }

    public void setExtensionFilters(ArrayList<FileChooser.ExtensionFilter> extensions) {
        this.fileChooser.getExtensionFilters().setAll(extensions);
    }

    public void setInitialDirectory(File filePath) {
        this.fileChooser.setInitialDirectory(filePath);
    }

    public void setFiletHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
}