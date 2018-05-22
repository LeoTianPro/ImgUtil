package login;

/**
 * @author HP
 * @FileName: GUIUtil
 * @create 2018-05-14 11:03
 * @desc
 **/

import java.awt.*;

public class GUIUtil {
    public static void toCenter(Component comp) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rec = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        comp.setLocation(((int) (rec.getWidth() - comp.getWidth()) / 2),
                ((int) (rec.getHeight() - comp.getHeight())) / 2);

    }
}
