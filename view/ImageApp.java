/**
 * @author Titanlbr520
 */
package view;

import login.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class ImageApp {
    public static void main(String args[]) {
        Font defaultFont = new Font("System", Font.PLAIN, 12);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
        UIManager.put("RadioButton.font", defaultFont);
        UIManager.put("ToolTip.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("List.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("MenuBar.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("PopupMenu.font", defaultFont);
        UIManager.put("Tree.font", defaultFont);
        UIManager.put("ToolBar.font", defaultFont);
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        MainFrame mainFrame = new MainFrame();
        mainFrame.setTitle("                                                                                                                                                   Photoship CC2018 ---- Produced by:Titanlbr520");
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        new LoginFrame();
    }
}
