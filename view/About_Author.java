package view;

/**
 * @author HP
 * @FileName: About_Author
 * @create 2018-05-12 20:44
 * @desc 作者信息
 **/

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About_Author extends JFrame {

    private final JPanel contentPanel  = new JPanel();
    // 得到显示器屏幕的宽高
    public        int    width         = Toolkit.getDefaultToolkit().getScreenSize().width;
    public        int    height        = Toolkit.getDefaultToolkit().getScreenSize().height;
    // 定义窗体的宽高
    public        int    windowsWedth  = 400;
    public        int    windowsHeight = 400;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            About_Author dialog = new About_Author();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public About_Author() {
        setTitle("作者简介");
        this.setBounds((width - windowsWedth) / 2,
                (height - windowsHeight) / 2, windowsWedth, windowsHeight);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setResizable(false);
        {
            JLabel label = new JLabel("QQ：*********");
            label.setFont(new Font("楷体", Font.PLAIN, 16));
            label.setBounds(120, 100, 200, 15);
            contentPanel.add(label);
        }
        {
            JLabel lblNewLabel = new JLabel("Phone:***********");
            lblNewLabel.setFont(new Font("楷体", Font.PLAIN, 16));
            lblNewLabel.setBounds(120, 150, 200, 15);
            contentPanel.add(lblNewLabel);
        }

        JLabel label = new JLabel("Github ID：Titanlbr520");
        label.setFont(new Font("楷体", Font.PLAIN, 16));

        label.setBounds(120, 200, 200, 15);
        contentPanel.add(label);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });

            }

        }
    }
}
