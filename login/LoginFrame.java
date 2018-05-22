package login;

/**
 * @author HP
 * @FileName: LoginFrame
 * @create 2018-05-14 11:03
 * @desc
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    /****************************定义各控件**************************/
    private Icon           welcomeIcon = new ImageIcon("welcome.png");
    private JLabel         lbWelcome   = new JLabel(welcomeIcon);
    private JLabel         lbAccount   = new JLabel("请您输入账号");
    private JTextField     tfAccount   = new JTextField(10);
    private JLabel         lbPassword  = new JLabel("请您输入密码");
    private JPasswordField pfPassword  = new JPasswordField(10);
    private JButton        btLogin     = new JButton("登录");
    private JButton        btRegister  = new JButton("注册");
    private JButton        btExit      = new JButton("退出");

    public LoginFrame() {
        /**********************界面初始化*****************************/
        super("登录");
        this.setLayout(new FlowLayout());
        this.add(lbWelcome);
        this.add(lbAccount);
        this.add(tfAccount);
        this.add(lbPassword);
        this.add(pfPassword);
        this.add(btLogin);
        this.add(btRegister);
        this.add(btExit);
        this.setSize(340, 280);
        GUIUtil.toCenter(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        /*****************************增加监听************************/
        btLogin.addActionListener(this);
        btRegister.addActionListener(this);
        btExit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btLogin) {
            String account = tfAccount.getText();
            String password = new String(pfPassword.getPassword());
            FileOpe.getInfoByAccount(account);
            if (Conf.account == null || !Conf.password.equals(password)) {
                JOptionPane.showMessageDialog(this, "登录失败！");
                return;
            }
            JOptionPane.showMessageDialog(this, "登录成功！");
            this.dispose();
            new OperationFrame();
        } else if (e.getSource() == btRegister) {
            this.dispose();
            new RegisterFrame();
        } else {
            JOptionPane.showMessageDialog(this, "下次再会！");
            System.exit(0);
        }
    }
}
