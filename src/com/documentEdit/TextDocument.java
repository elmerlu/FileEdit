package com.documentEdit;

import java.io.File;
import javax.swing.JTextArea;

/**
 * 文字編譯區。
 * @author I2A24 呂宜庭(49906124)
 * @version 1.0
 */
public class TextDocument extends JTextArea{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 專屬於此編譯區的檔案。
     */
    File file;
    /**
     * 檔案名稱，方便存取及新黨案暫存用。
     */
    String fileName;
    /**
     * 判斷是否已經儲存過了，用於檢查是否已儲存而將標題加上提示*號。
     */
    boolean save=true;
    public TextDocument(String fileName,File file) {
        this(fileName);
        this.file=file;
    }
    public TextDocument(String fileName) {
        this.fileName=fileName;
    }

}