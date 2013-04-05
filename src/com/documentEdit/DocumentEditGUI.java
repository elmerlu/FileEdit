package com.documentEdit;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.io.*;
import java.util.regex.*;
import javax.swing.text.*;

/**
 * 簡易文字編輯器
 * @author I2A24 呂宜庭(49906124)
 * @version 1.0
 */
public class DocumentEditGUI extends JFrame {
    private JTabbedPane td = new JTabbedPane();
    /**
     * 有拉桿的容器，避免內容過多顯示不了。
     */
    private JScrollPane scrollPane = new JScrollPane();
    /**
     * 計算目前存在的頁面數量。
     */
    private int docCount = 0;
    /**
     * 計算新頁面(使用new產生的頁面)的數量。
     */
    private int NewDocCount = 1;
    /**
     * <Save all>按鈕所需要的路徑，將新頁面儲存於此路徑中，故欲使用<Save all>需先設定此路徑。
     */
    private String default_auto_save_path = null;
    private JMenuItem replace_searchMenuItem = new JMenuItem("Replace/Search");
    private SystemEditReplace_Search replace_search = new SystemEditReplace_Search();
    private JMenuItem cutMenuItem = new JMenuItem("Cut");
    private JMenuItem pasteMenuItem = new JMenuItem("Paste");
    private JMenuItem copyMenutem = new JMenuItem("Copy");
    final String help = "開新檔案:\n    開啟一個新的檔案。\n\n開啟檔案:\n    開啟舊檔。\n\n儲存:"
            + "\n    儲存檔案。\n\n另存新檔:\n    將檔案另外儲存。\n\n全部儲存:\n    將全部檔案儲存，需先設定預設儲存路徑。"
            + "\n\n關閉檔案:\n    不存檔直接關閉檔案。\n\n關閉全部檔案:\n    不存檔直接關閉全部檔案\n\n尋找取代:"
            + "\n    尋找關鍵字或將關鍵字取代為新的字串。\n\n設定自動儲存路徑:\n    此路徑將用於全部存檔時，新黨案將存放於此路徑。";
    final String about = "作者:TTU I2A24 呂宜庭(49906124)\n版本:1.0\n最後更新日期:2011/12/27";

    /**
     * 程式視窗開啟。
     * @param args 無參數
     */
    public static void main(String[] args) {
        DocumentEditGUI doc = new DocumentEditGUI();
        doc.setSize(800, 600);
        doc.setVisible(true);  //將程式視窗顯示出來
    }

    /**
     * 程式的GUI畫面配置，並登入按鈕、選項監聽。
     */
    private void GUI() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem file_newMenuItem = new JMenuItem("New");
        file_newMenuItem.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
        file_newMenuItem.addActionListener(new SystemFileNew());
        fileMenu.add(file_newMenuItem);

        JMenuItem file_openMenuItem = new JMenuItem("Open File...");
        file_openMenuItem.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/TreeOpen.gif")));
        file_openMenuItem.addActionListener(new SystemFileOpen());
        fileMenu.add(file_openMenuItem);

        JMenu file_saveMenu = new JMenu("Save");
        fileMenu.add(file_saveMenu);

        JMenuItem file_save_saveMenuItem = new JMenuItem("Save");
        file_save_saveMenuItem.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
        file_save_saveMenuItem.addActionListener(new SystemFileSave());
        file_save_saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        file_saveMenu.add(file_save_saveMenuItem);

        JMenuItem file_save_save_asMenuItem = new JMenuItem("Save as");
        file_save_save_asMenuItem.addActionListener(new SystemFileSaveAS());
        file_saveMenu.add(file_save_save_asMenuItem);

        JMenuItem file_save_save_allMenuItem = new JMenuItem("Save all");
        file_save_save_allMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        file_save_save_allMenuItem.addActionListener(new SystemFileSaveAll());
        file_saveMenu.add(file_save_save_allMenuItem);

        JMenu file_closeMenu = new JMenu("Close");
        fileMenu.add(file_closeMenu);

        JMenuItem file_close_closeMenuItem = new JMenuItem("Close File");
        file_close_closeMenuItem.addActionListener(new SystemFileClose());
        file_closeMenu.add(file_close_closeMenuItem);

        JMenuItem file_close_close_all_fileMenuItem = new JMenuItem("Close all File");
        file_close_close_all_fileMenuItem.addActionListener(new SystemFileCloseAll());
        file_closeMenu.add(file_close_close_all_fileMenuItem);

        JMenuItem file_exitMenuItem = new JMenuItem("Exit");
        file_exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        file_exitMenuItem.addActionListener(new SystemExit());
        fileMenu.add(file_exitMenuItem);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        replace_searchMenuItem.addActionListener(replace_search);
        replace_searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        editMenu.add(replace_searchMenuItem);

        SystemEditCut_Copy_Paste cut_copy_paste = new SystemEditCut_Copy_Paste();
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cutMenuItem.addActionListener(cut_copy_paste);
        editMenu.add(cutMenuItem);
        copyMenutem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));

        copyMenutem.addActionListener(cut_copy_paste);
        editMenu.add(copyMenutem);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

        pasteMenuItem.addActionListener(cut_copy_paste);
        editMenu.add(pasteMenuItem);

        JMenuItem pathMenuItem = new JMenuItem("Default AutoSave-Path");
        pathMenuItem.addActionListener(new SystemEditSetPath());
        editMenu.add(pathMenuItem);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        
        JMenuItem HelpMenuItem = new JMenuItem("Help");
        HelpMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
                    JOptionPane.showMessageDialog(null, help, "Help!", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        helpMenu.add(HelpMenuItem);
        
        JMenuItem AboutNewMenuItem = new JMenuItem("About");
        AboutNewMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, about, "About...", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        helpMenu.add(AboutNewMenuItem);

        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);

        scrollPane.setViewportView(td);
        JButton newFileButton = new JButton("New");
        newFileButton.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
        newFileButton.setToolTipText("New File.");
        newFileButton.addActionListener(new SystemFileNew());
        toolBar.add(newFileButton);

        JButton openFileButton = new JButton("Open");
        openFileButton.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
        openFileButton.setToolTipText("Open File.");
        openFileButton.addActionListener(new SystemFileOpen());
        toolBar.add(openFileButton);

        JButton save_asFileButton = new JButton("Save as");
        save_asFileButton.setToolTipText("Save as File.");
        save_asFileButton.addActionListener(new SystemFileSaveAS());
        toolBar.add(save_asFileButton);

        JButton saveFileButton = new JButton("Save");
        saveFileButton.setIcon(new ImageIcon(DocumentEditGUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
        saveFileButton.setToolTipText("Savet his File.");
        saveFileButton.addActionListener(new SystemFileSave());
        toolBar.add(saveFileButton);

        JButton save_allFileButton = new JButton("Save all");
        save_allFileButton.setToolTipText("Save all File.");
        save_allFileButton.addActionListener(new SystemFileSaveAll());
        toolBar.add(save_allFileButton);

        JButton closeButton = new JButton("Close");
        closeButton.setToolTipText("Close this file.");
        closeButton.addActionListener(new SystemFileClose());
        toolBar.add(closeButton);

        JButton close_allButton = new JButton("Close all");
        close_allButton.setToolTipText("Close al File.");
        close_allButton.addActionListener(new SystemFileCloseAll());
        toolBar.add(close_allButton);
    }

    /**
     * 建構子
     */
    public DocumentEditGUI() {
        super("DocumentEdit");
        GUI();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        addWindowListener(new SystemExit());  //登記視窗監聽
        setFocusable(true);  //設定焦點
    }

    /**
     * 開啟新檔案的動作。
     */
    class SystemFileNew implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent a) {
            String str = "NewDoc_" + (NewDocCount++) + ".txt";
            TextDocument text = new TextDocument(str);
            text.addKeyListener(new SystemTrackSave()); //登記文字編譯區動作監聽，用於提示是否存檔
            td.add(text);  //將新黨案放入td，並設定其標題。
            td.setTitleAt(docCount++, str);  
        }
    }

    /**
     * 開啟舊檔案的動作。
     */
    class SystemFileOpen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser f = new JFileChooser();
            f.setFileFilter(new MyFileFilter());  //設定檔案選擇器
            int choose = f.showOpenDialog(getContentPane());  //顯示檔案選取
            if (choose == JFileChooser.OPEN_DIALOG) {  //有開啟檔案的話，開始讀檔
                BufferedReader br = null;
                try {
                    File file = f.getSelectedFile();
                    br = new BufferedReader(new FileReader(file));
                    TextDocument ta = new TextDocument(file.getName(), file);
                    ta.addKeyListener(new SystemTrackSave());  
                    ta.read(br, null);                        
                    td.add(ta);
                    td.setTitleAt(docCount++, file.getName());
                } catch (Exception exc) {
                    exc.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (Exception ecx) {
                        ecx.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 儲存檔案的動作，若選取的檔案為新檔案則另存新檔，否則直接存檔。
     */
    class SystemFileSave implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {  //判斷是否有檔案
                TextDocument ta =
                        (TextDocument) td.getComponentAt(td.getSelectedIndex());
                if (ta.file == null) {  //判斷是否為新黨案
                    new SystemFileSaveAS().actionPerformed(e);  //做另存新黨
                    System.out.println("Save->Save as pass!");
                } else {
                    new SystemFileSaveAll().save(td.getSelectedIndex());//直接儲存於原檔案
                    System.out.println("Save->Save pass!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "沒有檔案可以儲存!");
            }
        }
    }

    /**
     * 另存新檔的動作。
     */
    class SystemFileSaveAS implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {
                JFileChooser f = new JFileChooser();
                f.setFileFilter(new MyFileFilter());
                int choose = f.showSaveDialog(getContentPane());
                if (choose == JFileChooser.APPROVE_OPTION) {
                    BufferedWriter brw = null;
                    try {
                        File file = f.getSelectedFile();
                        brw = new BufferedWriter(new FileWriter(file));
                        int i = td.getSelectedIndex();
                        TextDocument ta = (TextDocument) td.getComponentAt(i);
                        ta.write(brw);
                        ta.fileName = file.getName();//將檔案名稱更新為存檔的名稱
                        td.setTitleAt(td.getSelectedIndex(), ta.fileName);
                        ta.file = file;
                        ta.save = true;  //設定已儲存
                        System.out.println("Save as pass!");
                        td.setTitleAt(i, ta.fileName);  //更新標題名稱
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    } finally {
                        try {
                            brw.close();
                        } catch (Exception ecx) {
                            ecx.printStackTrace();
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "沒有檔案可以儲存!");
            }
        }
    }

    /**
     * 儲存所有檔案的動作，新檔案會儲存於default_auto_save_path路徑下，其餘存回原位置。
     */
    class SystemFileSaveAll implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isHaveNewDocument = false;
            for (int i = 0; i < td.getTabCount(); i++) {  //依序將檔案撈出
                TextDocument ta = (TextDocument) td.getComponentAt(i);
                if (ta.file == null) {  //判斷是否有新黨案，有的會將運用到預設儲存路徑
                    isHaveNewDocument = true;
                    break;
                }
            }
            //有新黨案，但是卻沒設定預設路徑，將跳出警告
            if (isHaveNewDocument && default_auto_save_path == null) {
                JOptionPane.showMessageDialog(null,  
                        "請先設定自動儲存預設位置!\nEdit->Default AutoSave-Path", "Save error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (td.getTabCount() > 0) {
                for (int i = 0; i < td.getTabCount(); i++) {//依序儲存
                    save(i);
                }
                System.out.println("Save pass!");
                JOptionPane.showMessageDialog(null, "可儲存的檔案已儲存檔案。\n新檔案儲存於預設位置" + default_auto_save_path + "目錄下.");
            } else {
                JOptionPane.showMessageDialog(null, "沒有檔案可以儲存!");
            }
        }

        public void save(int i) {
            BufferedWriter brw = null;
            TextDocument ta = (TextDocument) td.getComponentAt(i);
            try {
                File file = null;
                if (ta.file == null) {//判斷是否為新黨案，是的話使用預設路徑儲存
                    file = new File(default_auto_save_path + "\\" + ta.fileName);
                } else {
                    file = ta.file;  //儲存於原檔案
                }
                brw = new BufferedWriter(new FileWriter(file));
                ta.write(brw);
                ta.save = true;
                td.setTitleAt(i, ta.fileName); //更新標題名稱
            } catch (FileNotFoundException ffe) {//若預設路徑不存在，則跳出警告
                JOptionPane.showMessageDialog(null, ta.fileName
                        + "儲存失敗!\n請檢查Default AutoSave-Path是否存在,或是權限不足.", "Save error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                try {
                    brw.close();
                } catch (Exception exc) {
                }
            }
        }
    }

    /**
     * 關閉當前檔案的動作。
     */
    class SystemFileClose implements ActionListener {
        @Override//將當案關閉，並將檔案數量減1
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {
                System.out.println("close");
                td.remove(td.getSelectedIndex());
                docCount--;
            }//如果沒有檔案了，關閉尋找/取代介面
            if (docCount == 0) {
                getContentPane().remove(replace_search.jp);
                replace_search.show = false;
                validate();//更新個容器
            }
        }
    }

    /**
     * 關閉全部檔案的動作。
     */
    class SystemFileCloseAll implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {
                System.out.println("close all");
                td.removeAll();
                docCount = 0;
                getContentPane().remove(replace_search.jp);//關閉尋找/取代介面
                replace_search.show = false;
                validate();
            }
        }
    }

    /**
     * 設定default_auto_save_path路徑的動作。
     */
    class SystemEditSetPath implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            default_auto_save_path = JOptionPane.showInputDialog(null, "自動儲存路徑:", "Default AutoSave-Path", JOptionPane.QUESTION_MESSAGE);
        }
    }

    /**
     * 程式視窗關閉動作。
     */
    class SystemExit extends WindowAdapter implements ActionListener {
        @Override
        public void windowClosing(WindowEvent e) {
            systemexit();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            systemexit();
        }
        private void systemexit() {
            System.out.println("DocumentEdit Close.");
            System.exit(0);//0表示正常關閉
        }
    }

    /**
     * 檢查檔案是否已經儲存的動作。
     * EX:
     * 檔案Title名稱:abc.txt
     * 若對此檔案內容進行修改
     * 則檔案Title名稱變更為:   *abc.txt
     * 儲存後會恢復為:abc.txt
     */
    class SystemTrackSave implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            int i = td.getSelectedIndex();
            TextDocument text = (TextDocument) td.getComponentAt(i);
            if (text.save) {
                td.setTitleAt(i, "   *" + td.getTitleAt(i));
                text.save = false;
            }
        }
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
    }

    /**
     * 收尋與取代的動作及GUI介面。
     * 
     */
    class SystemEditReplace_Search implements ActionListener {
        JPanel jp = new JPanel();
        JLabel jl1 = new JLabel("尋找:");
        JLabel jl2 = new JLabel("取代成:");
        JTextField tf1 = new JTextField(10);
        JTextField tf2 = new JTextField(10);
        JButton jb1 = new JButton("收尋");
        JButton jb2 = new JButton("取代");
        boolean show = false;
        //GUI介面
        public SystemEditReplace_Search() {
            jp.add(jl1);
            jp.add(tf1);
            jp.add(jl2);
            jp.add(tf2);
            jp.add(jb1);
            jb1.addActionListener(this);
            jp.add(jb2);
            jb2.addActionListener(this);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {
                TextDocument ta = (TextDocument) td.getComponentAt(td.getSelectedIndex());
                Pattern pn = Pattern.compile(tf1.getText());
                Matcher mt = pn.matcher(ta.getText());
                if (e.getSource() == jb2) {//取代
                    ta.setText(mt.replaceAll(tf2.getText()));
                } else if (e.getSource() == jb1) {//尋找
                    Highlighter hl = ta.getHighlighter();
                    hl.removeAllHighlights();
                    while (mt.find()) {
                        try {
                            hl.addHighlight(mt.start(), mt.end(), new DefaultHighlighter.DefaultHighlightPainter(null));
                        } catch (Exception ex) {
                        }
                    }//開啟及關閉介面
                } else if (e.getSource() == replace_searchMenuItem) {
                    System.out.println("Replace/Search is show:" + !show);
                    if (show) {
                        getContentPane().remove(jp);
                        show = false;
                    } else {
                        getContentPane().add(jp, BorderLayout.SOUTH);
                        show = true;
                    }
                    validate(); //刷新容器
                }
            } else if (e.getSource() == replace_searchMenuItem) {
                JOptionPane.showMessageDialog(null, "尚無檔案，無法使用!", "Repace/Search error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 剪下、複製、貼上等動作。
     */
    class SystemEditCut_Copy_Paste implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (td.getTabCount() > 0) {
                TextDocument ta = (TextDocument) td.getComponentAt(td.getSelectedIndex());
                if (e.getSource() == cutMenuItem) {
                    ta.cut();
                } else if (e.getSource() == pasteMenuItem) {
                    ta.paste();
                } else if (e.getSource() == copyMenutem) {
                    ta.copy();
                }
            }
        }
    }

    /**
     * 檔案選擇器。
     */
    class MyFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String fn = f.getName();
            if (fn.toLowerCase().endsWith(".java")
                    || fn.toLowerCase().endsWith(".txt")) {
                return true;
            }
            return false;
        }
        @Override
        public String getDescription() {
            return "txt";
        }
    }
}