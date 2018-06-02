/**
 * @author Titanlbr520
 */
package view;
/*
 * 临时记录：文件选择器 取消有bug(已修复！)
 * 临时记录：没有添加对线性差值点位置的判断
 * 临时记录：当有dialog取消，列表依然更新....(已修复！)
 * 临时记录：极小点阈值法 对于极小点的确定方法①最小二乘法 拟合函数 图像太复杂不合实际
 *                                        ② 利用斜率数组判断，方法简单，但是图像复杂    （没做）
 * 临时记录：查看梯度图，如果改变窗口大小会自动调用repaint方法
 * 临时记录：Prewitt算子用哪个？
 * 临时记录：a,b的值如何确定
 * 临时记录：关于HT变换的想法，对于噪声的去除，仅仅就直线而言，可以利用延展去实现。噪声是一个单独突出的一圈点。然后一些细节处理。
 */

import algorithm.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MainFrame extends JFrame implements ListSelectionListener {

    JMenuBar  mb;
    JMenu     fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    JMenu     editMenu;
    JMenuItem undoItem;
    JMenuItem redoItem;

    JMenu     geoMenu;
    JMenuItem horMirrorItem;
    JMenuItem verMirrorItem;
    JMenuItem scaleItem;
    JMenuItem rotateItem;

    JMenu     ehanceMenu;
    JMenuItem grayScaleItem;
    JMenu     grayTransformation;
    JMenu     linearTransformation;
    JMenuItem liNotSegmentation;
    JMenuItem liSegmentation;
    JMenu     histogramModification;
    JMenuItem histgramItem;
    JMenu     imageSmoothing;
    JMenuItem medianFiltering;
    JMenuItem gaussianSmoothingItem;
    JMenuItem fieldAverageItem;
    JMenu     imageSharpening;
    JMenuItem laplacianHiBoostFiltering;


    JMenu     imageSegmentation;
    JMenu     thresholdSeg;
    JMenuItem simpleThreshold;
    JMenuItem otsuThreshold;

    JMenu     edgeMenu;
    JMenu     gradientMenu;
    JMenuItem horGradientItem;
    JMenuItem verGradientItem;
    JMenuItem cannyItem;

    JMenu     aboutMenu;
    JMenuItem introItem;

    JToolBar tb;
    JButton  openBtn;
    JButton  undoBtn;
    JButton  redoBtn;
    JButton  saveBtn;
    JButton  exitBtn;

    ImagePanel    imagePanel;
    JScrollPane   scrollPane;
    JScrollPane   scrollPane1;
    BufferedImage image;

    JFileChooser   chooser;
    ImagePreviewer imagePreviewer;
    ImageFileView  fileView;
    JList          operationList;

    ImageFileFilter jpgFilter;
    ImageFileFilter bmpFilter;
    ImageFileFilter gifFilter;
    ImageFileFilter bothFilter;

    LinkedList undoList;
    LinkedList redoList;
    LinkedList allList;
    int     pointer  = 0;
    int     newImage = 0;
    boolean okFlag   = false;
    private final static int MAX_UNDO_COUNT = 10;
    private final static int MAX_REDO_COUNT = 10;
    private final static int MAX_ALL_COUNT  = 50;

    public MainFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(e);
            }
        });

        undoList = new LinkedList();
        redoList = new LinkedList();
        allList = new LinkedList();
        initComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        Container contentPane = getContentPane();

        imagePanel = new ImagePanel(image);
        scrollPane = new JScrollPane(imagePanel);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        chooser = new JFileChooser();
        imagePreviewer = new ImagePreviewer(chooser);
        fileView = new ImageFileView();
        jpgFilter = new ImageFileFilter("jpg", "JPEG Compressed Image Files");
        bmpFilter = new ImageFileFilter("bmp", "BMP Image Files");
        gifFilter = new ImageFileFilter("gif", "GIF Image Files");
        bothFilter = new ImageFileFilter(new String[]{"jpg", "bmp", "gif"}, "JPEG, BMP and GIF Image Files");
        chooser.addChoosableFileFilter(jpgFilter);
        chooser.addChoosableFileFilter(bmpFilter);
        chooser.addChoosableFileFilter(gifFilter);
        chooser.addChoosableFileFilter(bothFilter);
        chooser.setAccessory(imagePreviewer);
        chooser.setFileView(fileView);
        chooser.setAcceptAllFileFilterUsed(false);
        Image img = new ImageIcon("PS.png").getImage();
        setIconImage(img);

        Icon openIcon = new ImageIcon("open.png");
        Icon saveIcon = new ImageIcon("save.png");
        Icon exitIcon = new ImageIcon("close.png");
        Icon undoIcon = new ImageIcon("undo.png");
        Icon redoIcon = new ImageIcon("redo.png");
        Icon horIcon = new ImageIcon("hor.png");
        Icon verIcon = new ImageIcon("ver.png");
        Icon scaleIcon = new ImageIcon("scale.png");
        Icon rotateIcon = new ImageIcon("rotate.png");
        Icon grayIcon = new ImageIcon("gray.png");
        Icon histogramIcon = new ImageIcon("histogram.png");
        Icon smoothIcon = new ImageIcon("smooth.png");
        Icon sharpenIcon = new ImageIcon("sharpen.png");
        Icon thresholdIcon = new ImageIcon("threshold.png");
        Icon gradientIcon = new ImageIcon("gradient.png");
        Icon cannyIcon = new ImageIcon("canny.png");
        Icon authorIcon = new ImageIcon("author.png");


        // ----操作列表---------------------------------------------------------
        operationList = new JList();
        scrollPane1 = new JScrollPane(operationList);
        contentPane.add(scrollPane1, BorderLayout.EAST);

        final DefaultListModel dlm = new DefaultListModel();
        operationList.setModel(dlm);
        operationList.addListSelectionListener(this);

        // ----菜单条------------------------------------------------------------
        mb = new JMenuBar();
        setJMenuBar(mb);
        // ----File菜单----------------------------------------------------------
        fileMenu = new JMenu("文件(F)");
        fileMenu.setMnemonic('F');
        mb.add(fileMenu);


        openItem = new JMenuItem("打开(O)", openIcon);
        openItem.setMnemonic('O');
        openItem.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile(e);
                if (okFlag) {
                    newImage++;
                    dlm.addElement("第" + pointer + "步：新图像" + newImage + "                            ");
                    pointer++;
                    okFlag = false;
                }
            }
        });

        saveItem = new JMenuItem("保存(S)", saveIcon);
        saveItem.setMnemonic('S');
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile(e);
            }
        });

        exitItem = new JMenuItem("退出(X)", exitIcon);
        exitItem.setMnemonic('X');
        exitItem.setAccelerator(KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitSystem(e);
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // ----Edit菜单----------------------------------------------------------
        editMenu = new JMenu("编辑(E)");
        editMenu.setMnemonic('E');
        mb.add(editMenu);

        undoItem = new JMenuItem("撤销(U)", undoIcon);
        undoItem.setMnemonic('U');
        undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', Event.CTRL_MASK));
        undoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undo(e);
            }
        });

        redoItem = new JMenuItem("重做(R)", redoIcon);
        redoItem.setMnemonic('R');
        redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', Event.CTRL_MASK));
        redoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redo(e);
            }
        });

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        // ----Geo菜单----------------------------------------------------------
        geoMenu = new JMenu("几何变换(G)");
        geoMenu.setMnemonic('G');
        mb.add(geoMenu);


        horMirrorItem = new JMenuItem("水平镜像(H)", horIcon);
        horMirrorItem.setMnemonic('H');
        horMirrorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horMirror(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经水平镜像");
                pointer++;
            }
        });

        verMirrorItem = new JMenuItem("垂直镜像(V)", verIcon);
        verMirrorItem.setMnemonic('V');
        verMirrorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verMirror(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经垂直镜像");
                pointer++;
            }
        });

        scaleItem = new JMenuItem("比例缩放(S)", scaleIcon);
        scaleItem.setMnemonic('S');
        scaleItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scale(e);
                if (okFlag) {
                    dlm.addElement("第" + pointer + "步：图像" + newImage + "经比例缩放");
                    pointer++;
                    okFlag = false;
                }
            }
        });

        rotateItem = new JMenuItem("旋转(R)", rotateIcon);
        rotateItem.setMnemonic('R');
        rotateItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotate(e);
                if (okFlag) {
                    dlm.addElement("第" + pointer + "步：图像" + newImage + "经旋转");
                    pointer++;
                    okFlag = false;
                }
            }
        });

        geoMenu.add(horMirrorItem);
        geoMenu.add(verMirrorItem);
        geoMenu.add(scaleItem);
        geoMenu.add(rotateItem);

        // ----Ehance菜单-------------------------------------------------------
        ehanceMenu = new JMenu("图像增强(E)");
        ehanceMenu.setMnemonic('E');
        mb.add(ehanceMenu);

        grayTransformation = new JMenu("灰度变换(T)");
        grayTransformation.setIcon(grayIcon);
        grayTransformation.setMnemonic('T');

        grayScaleItem = new JMenuItem("图片灰度化(G)");
        grayScaleItem.setMnemonic('G');
        grayScaleItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graySacle(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经灰度化");

            }
        });

        linearTransformation = new JMenu("线性变换(L)");
        linearTransformation.setMnemonic('L');

        liNotSegmentation = new JMenuItem("不分段线性(N)");
        liNotSegmentation.setMnemonic('N');
        liNotSegmentation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                liNotSegmentation(e);
                if (okFlag) {
                    dlm.addElement("第" + pointer + "步：图像" + newImage + "经不分段线性变换");
                    pointer++;
                    okFlag = false;
                }
            }
        });

        liSegmentation = new JMenuItem("分段线性(S)");
        liSegmentation.setMnemonic('S');
        liSegmentation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                liSegmentation(e);
                if (okFlag) {
                    dlm.addElement("第" + pointer + "步：图像" + newImage + "经分段线性变换");
                    pointer++;
                    okFlag = false;
                }
            }
        });


        histogramModification = new JMenu("直方图修正(H)");
        histogramModification.setIcon(histogramIcon);
        histogramModification.setMnemonic('H');

        histgramItem = new JMenuItem("灰度直方图(H)");
        histgramItem.setMnemonic('H');
        histgramItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                histgram(e);
            }
        });


        imageSmoothing = new JMenu("图像平滑(S)");
        imageSmoothing.setIcon(smoothIcon);
        imageSmoothing.setMnemonic('S');

        medianFiltering = new JMenuItem("中值滤波(F)");
        medianFiltering.setMnemonic('F');
        medianFiltering.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                medianFiltering(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经中值滤波");
                pointer++;
            }
        });

        gaussianSmoothingItem = new JMenuItem("高斯平滑(G)");
        gaussianSmoothingItem.setMnemonic('G');
        gaussianSmoothingItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gaussianSmoothing(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经高斯平滑");
                pointer++;
            }
        });

        fieldAverageItem = new JMenuItem("邻域平均(F)");
        fieldAverageItem.setMnemonic('F');
        fieldAverageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fieldAverage(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经邻域平均");
                pointer++;
            }
        });

        imageSharpening = new JMenu("图像锐化(S)");
        imageSharpening.setIcon(sharpenIcon);
        imageSharpening.setMnemonic('S');


        laplacianHiBoostFiltering = new JMenuItem("拉斯高增滤波(H)");
        laplacianHiBoostFiltering.setMnemonic('H');
        laplacianHiBoostFiltering.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                laplacianHiBoostFiltering(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经拉普拉斯高增滤波");
                pointer++;
            }
        });


        ehanceMenu.add(grayTransformation);
        grayTransformation.add(grayScaleItem);
        grayTransformation.add(linearTransformation);
        linearTransformation.add(liNotSegmentation);
        linearTransformation.add(liSegmentation);
        ehanceMenu.add(histogramModification);
        histogramModification.add(histgramItem);
        ehanceMenu.add(imageSmoothing);
        imageSmoothing.add(medianFiltering);
        imageSmoothing.add(gaussianSmoothingItem);
        imageSmoothing.add(fieldAverageItem);
        ehanceMenu.add(imageSharpening);
        imageSharpening.add(laplacianHiBoostFiltering);

        // ----图像分割----------------------------------------------------------
        imageSegmentation = new JMenu("图像分割(I)");
        imageSegmentation.setMnemonic('I');
        mb.add(imageSegmentation);

        thresholdSeg = new JMenu("全局阈值分割(T)");
        thresholdSeg.setIcon(thresholdIcon);
        thresholdSeg.setMnemonic('T');

        simpleThreshold = new JMenuItem("简单阈值(S)");
        simpleThreshold.setMnemonic('S');
        simpleThreshold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                threshold(e);
                if (okFlag) {
                    dlm.addElement("第" + pointer + "步：图像" + newImage + "经简单阈值分割");
                    pointer++;
                    okFlag = false;
                }

            }
        });


        otsuThreshold = new JMenuItem("Ostu法(O)");
        otsuThreshold.setMnemonic('O');
        otsuThreshold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                otsu(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经Ostu阈值分割");
                pointer++;
            }
        });


        imageSegmentation.add(thresholdSeg);
        thresholdSeg.add(simpleThreshold);
        thresholdSeg.add(otsuThreshold);

        // ----边缘检测-------------------------------------------------------
        edgeMenu = new JMenu("边缘检测(E)");
        edgeMenu.setMnemonic('E');
        mb.add(edgeMenu);

        gradientMenu = new JMenu("Sobel边缘检测(S)");
        gradientMenu.setIcon(gradientIcon);
        gradientMenu.setMnemonic('G');

        horGradientItem = new JMenuItem("水平边缘检测(H)");
        horGradientItem.setMnemonic('H');
        horGradientItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                horGradient(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经水平边缘检测");
                pointer++;
            }
        });

        verGradientItem = new JMenuItem("垂直边缘检测(V)");
        verGradientItem.setMnemonic('V');
        verGradientItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verGradient(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经垂直边缘检测");
                pointer++;
            }
        });


        cannyItem = new JMenuItem("canny边缘检测(C)", cannyIcon);
        cannyItem.setMnemonic('C');
        cannyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canny(e);
                dlm.addElement("第" + pointer + "步：图像" + newImage + "经canny边缘检测");
                pointer++;

            }
        });

        edgeMenu.add(gradientMenu);
        gradientMenu.add(horGradientItem);
        gradientMenu.add(verGradientItem);
        edgeMenu.add(cannyItem);

        // ----关于----------------------------------------------------------
        aboutMenu = new JMenu("关于(A)");
        aboutMenu.setMnemonic('A');
        mb.add(aboutMenu);

        introItem = new JMenuItem("作者简介(I)", authorIcon);
        introItem.setMnemonic('I');
        introItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                introduce(e);
                dlm.addElement("第" + pointer + "步：" + "查看关于");
                pointer++;
            }
        });

        aboutMenu.add(introItem);
        // ----工具条------------------------------------------------------------
        tb = new JToolBar();
        contentPane.add(tb, BorderLayout.NORTH);
        // ----------------------------------------------------------------------

        openBtn = new JButton(openIcon);
        openBtn.setToolTipText("打开");
        tb.add(openBtn);
        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile(e);
            }
        });

        undoBtn = new JButton(undoIcon);
        undoBtn.setToolTipText("撤销");
        tb.add(undoBtn);
        undoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undo(e);
            }
        });

        redoBtn = new JButton(redoIcon);
        redoBtn.setToolTipText("重做");
        tb.add(redoBtn);
        redoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redo(e);
            }
        });

        saveBtn = new JButton(saveIcon);
        saveBtn.setToolTipText("保存");
        tb.add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile(e);
            }
        });

        exitBtn = new JButton(exitIcon);
        exitBtn.setToolTipText("退出");
        tb.add(exitBtn);
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitSystem(e);
            }
        });
        // ----------------------------------------------------------------------
    }

    /**
     * 退出程序事件
     */
    private void exit(WindowEvent e) {
        int n = JOptionPane.showConfirmDialog(null, "阁下，真的要一走了之了吗?", "友尽框", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(new JFrame(), "好吧，有缘再见！");
            System.exit(0);
        } else if (n == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(new JFrame(), "还以为你真的要走了呢！");
        }
    }

    void openFile(ActionEvent e) {
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        if (chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
            try {
                image = ImageIO.read(chooser.getSelectedFile());
            } catch (Exception ex) {
                return;
            }
            imagePanel.setImage(image);
            imagePanel.repaint();
            undoList.clear();
            redoList.clear();
            saveRedoInfo(image);
            saveAllInfo(image);
            okFlag = true;

        }
    }

    void exitSystem(ActionEvent e) {
        int n = JOptionPane.showConfirmDialog(null, "阁下，真的要一走了之了吗?", "友尽框", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(new JFrame(), "好吧，有缘再见！");
            System.exit(0);
        } else if (n == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(new JFrame(), "还以为你真的要走了呢！");
        }

    }

    void saveFile(ActionEvent e) {
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int index = chooser.showDialog(null, "保存文件");
        if (index == JFileChooser.APPROVE_OPTION) {
            image = (BufferedImage) undoList.get(0);
            File f = chooser.getSelectedFile();
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            String fileName = chooser.getName(f) + "123";
            String writePath = chooser.getCurrentDirectory().getAbsolutePath() + fileName;
            File filePath = new File(writePath);
            try {
                ImageIO.write(image, "jpg", filePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    void saveUndoInfo(BufferedImage image) {
        if (undoList.size() == MAX_UNDO_COUNT) {
            undoList.removeLast();
        }
        undoList.addFirst(image);
    }

    void saveRedoInfo(BufferedImage image) {
        if (redoList.size() == MAX_REDO_COUNT) {
            redoList.removeLast();
        }
        redoList.addFirst(image);
    }

    void saveAllInfo(BufferedImage image) {
        if (allList.size() == MAX_ALL_COUNT) {
            allList.removeFirst();
        }
        allList.addLast(image);
    }

    void undo(ActionEvent e) {
        if (undoList.size() > 0) {
            // saveRedoInfo(image);
            image = (BufferedImage) undoList.get(0);
            imagePanel.setImage(image);
            imagePanel.repaint();
            undoList.remove(0);
        }
    }

    void redo(ActionEvent e) {
        if (redoList.size() > 0) {
            saveUndoInfo(image);
            image = (BufferedImage) redoList.get(0);
            imagePanel.setImage(image);
            imagePanel.repaint();
            // redoList.remove(0);
        }
    }


    void horMirror(ActionEvent e) {
        saveUndoInfo(image);
        image = GeoTransform.horMirror(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void verMirror(ActionEvent e) {
        saveUndoInfo(image);
        image = GeoTransform.verMirror(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void scale(ActionEvent e) {
        ScaleDlg scaleDlg = new ScaleDlg(this, true);
        scaleDlg.setLocationRelativeTo(this);
        scaleDlg.setImageWidth(image.getWidth());
        scaleDlg.setImageHeight(image.getHeight());
        if (scaleDlg.showModal() == JOptionPane.OK_OPTION) {
            saveUndoInfo(image);
            image = GeoTransform.scale(image, scaleDlg.getScale(), scaleDlg.getScale());
            imagePanel.setImage(image);
            imagePanel.repaint();
            saveAllInfo(image);
            okFlag = true;
        }
    }

    void rotate(ActionEvent e) {
        RotateDlg rotateDlg = new RotateDlg(this, true);
        rotateDlg.setLocationRelativeTo(this);
        if (rotateDlg.showModal() == JOptionPane.OK_OPTION) {
            saveUndoInfo(image);
            image = GeoTransform.rotate(image, rotateDlg.getAngle(), rotateDlg.getIsResize());
            imagePanel.setImage(image);
            imagePanel.repaint();
            saveAllInfo(image);
            okFlag = true;
        }
    }

    void graySacle(ActionEvent e) {
        saveUndoInfo(image);
        image = ImageEnhancement.grayScale(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void liNotSegmentation(ActionEvent e) {
        LinearTransformationDlg litranmadlg = new LinearTransformationDlg(this, true);
        litranmadlg.setImage(image);
        litranmadlg.setLocationRelativeTo(this);
        litranmadlg.show();
        if (litranmadlg.getokOption() == 1) {
            saveUndoInfo(image);
            image = ImageEnhancement.linearTransformation(image, Math.round(litranmadlg.getxValue() / 2) - 20,
                    Math.round(litranmadlg.getxValue1() / 2) - 20, 300 - Math.round(litranmadlg.getyValue() / 2) - 15,
                    300 - Math.round(litranmadlg.getyValue1() / 2) - 15);
        }
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
        okFlag = true;
        litranmadlg.dispose();

    }

    void liSegmentation(ActionEvent e) {
        NotLinearTransformationDlg noLitranmadlg = new NotLinearTransformationDlg(this, true);
        noLitranmadlg.setImage(image);
        noLitranmadlg.setLocationRelativeTo(this);
        noLitranmadlg.show();
        if (noLitranmadlg.getokOption() == 1) {
            saveUndoInfo(image);
            image = ImageEnhancement.NotSegmentationLinearTransformation(image,
                    Math.round(noLitranmadlg.getxValue() / 2) - 20, Math.round(noLitranmadlg.getxValue1() / 2) - 20,
                    300 - Math.round(noLitranmadlg.getyValue() / 2) - 15,
                    300 - Math.round(noLitranmadlg.getyValue1() / 2) - 15);
        }
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
        okFlag = true;
        noLitranmadlg.dispose();

    }


    void histgram(ActionEvent e) {
        HistgramDlg histgramDlg = new HistgramDlg(this, true);
        histgramDlg.setImage(image);
        histgramDlg.setLocationRelativeTo(this);
        histgramDlg.show();

    }


    void medianFiltering(ActionEvent e) {
        saveUndoInfo(image);
        ImageEnhancement.findLine(image);
        image = ImageEnhancement.medianFiltering(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void gaussianSmoothing(ActionEvent e) {
        saveUndoInfo(image);
        image = ImageEnhancement.gaussianSmoothing(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void fieldAverage(ActionEvent e) {
        saveUndoInfo(image);
        image = ImageEnhancement.fieldAverage(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }


    void laplacianHiBoostFiltering(ActionEvent e) {
        saveUndoInfo(image);
        image = ImageEnhancement.laplacianHiBoostFiltering(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);

    }

    public void valueChanged(ListSelectionEvent e) {
        Object source = e.getSource();
        if (source == operationList) {
            saveUndoInfo(image);
            int selectNo = operationList.getSelectedIndex();
            image = (BufferedImage) allList.get(selectNo);
            imagePanel.setImage(image);
            imagePanel.repaint();
        }

    }

    public void threshold(ActionEvent e) {
        ThresholdDlg threshouldDlg = new ThresholdDlg(this, true);
        threshouldDlg.setLocationRelativeTo(this);
        threshouldDlg.show();
        if (threshouldDlg.getModelResult() == JOptionPane.OK_OPTION) {
            saveUndoInfo(image);
            image = ImageSegmentation.threshold(image, threshouldDlg.getThreshold());
            imagePanel.setImage(image);
            imagePanel.repaint();
            saveAllInfo(image);
            okFlag = true;
        }
    }


    public void otsu(ActionEvent e) {
        saveUndoInfo(image);
        image = ImageSegmentation.otsu(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }


    void horGradient(ActionEvent e) {
        saveUndoInfo(image);
        image = EdgeDetection.horGradient(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void verGradient(ActionEvent e) {
        saveUndoInfo(image);
        image = EdgeDetection.verGradient(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }


    void canny(ActionEvent e) {
        saveUndoInfo(image);
        image = EdgeDetection.canny(image);
        imagePanel.setImage(image);
        imagePanel.repaint();
        saveAllInfo(image);
    }

    void introduce(ActionEvent e) {
        new About_Author().setVisible(true);
    }
}

