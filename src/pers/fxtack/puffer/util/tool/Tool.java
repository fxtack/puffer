package pers.fxtack.puffer.util.tool;

/**
 * 该类是一个工具类，用于放置其他工具
 *
 * @author Fxtack
 * @version 1.0
 */
public class Tool {
    /**
     * 该方法将输入文件的大小 (bit为单位), 将其进行单位换算，并格式化输出文件大小的字符串表示。
     * @param size 文件的大小
     * @return 文件大小的格式化字符串表示
     */
    public static String formatFileSize(long size) {
        String s[] = {" B", " KB", " MB", " GB"};
        long value = 0;
        byte i;
        for(i = 1 ; i <= 4 ; i++) {
            if(size / (1024 * i) == 0 && i != 1) {
                value = size / (1024 * i - 1);
                break;
            }else if(size / (1024 * i) == 0 && i == 1){
                value =  size;
                break;
            }
        }
        return value + s[i-1];
    }

    public static String filePathToName(String filePath) {
        return filePath.substring(filePath.lastIndexOf('\\')+1);
    }
}
