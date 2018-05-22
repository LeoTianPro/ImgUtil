package login;

/**
 * @author HP
 * @FileName: ModifyDialog
 * @create 2018-05-14 11:05
 * @desc
 **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifyDialog extends JDialog implements ActionListener {
    /*******************************定义各控件**************************/
    private JLabel         lbMsg       = new JLabel("您的账号为：");
    private JLabel         lbAccount   = new JLabel(Conf.account);
    private JLabel         lbPassword  = new JLabel("请您输入密码");
    private JPasswordField pfPassword  = new JPasswordField(Conf.password, 10);
    private JLabel         lbPassword2 = new JLabel("输入确认密码");
    private JPasswordField pfPassword2 = new JPasswordField(Conf.password, 10);
    private JLabel         lbName      = new JLabel("请您修改姓名");
    private JTextField     tfName      = new JTextField(Conf.name, 10);
    private JLabel         lbDept      = new JLabel("请您修改部门");
    private JComboBox      cbDept      = new JComboBox();
    private JButton        btModify    = new JButton("修改");
    private JButton        btExit      = new JButton("关闭");

    public ModifyDialog(JFrame frm) {
        /***********************界面初始化***************************/
        super(frm, true);
        this.setLayout(new GridLayout(6, 2));
        this.add(lbMsg);
        this.add(lbAccount);
        this.add(lbPassword);
        this.add(pfPassword);
        this.add(lbPassword2);
        this.add(pfPassword2);
        this.add(lbName);
        this.add(tfName);
        this.add(lbDept);
        this.add(cbDept);
        cbDept.addItem("设计部");
        cbDept.addItem("行政部");
        cbDept.addItem("客户服务部");
        cbDept.addItem("运维部");
        cbDept.setSelectedItem(Conf.dept);
        this.add(btModify);
        this.add(btExit);
        this.setSize(240, 200);
        GUIUtil.toCenter(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*******************增加监听*******************************/
        btModify.addActionListener(this);
        btExit.addActionListener(this);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btModify) {
            String password1 = new String(pfPassword.getPassword());
            String password2 = new String(pfPassword2.getPassword());
            if (!password1.equals(password2)) {
                JOptionPane.showMessageDialog(this, "密码不匹配！");
                return;
            }
            String name = tfName.getText();
            String dept = (String) cbDept.getSelectedItem();
            //将新的值存入静态变量
            Conf.password = password1;
            Conf.name = name;
            Conf.dept = dept;
            FileOpe.updateCustomer(Conf.account, password1, name, dept);
            JOptionPane.showMessageDialog(this, "修改成功！");
        } else {
            this.dispose();
        }
    }
}
