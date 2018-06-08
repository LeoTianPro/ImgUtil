/**
 * @author Titanlbr520
 * @function 像素运算
 */
package util;

public class ProcessUtil {

    /**
     * 取得三色
     **/
    public static int[] decodeColor(int color, int rgb[]) {
        if (rgb == null)
            rgb = new int[3];
        rgb[0] = (color & 0x00ff0000) >> 16;
        rgb[1] = (color & 0x0000ff00) >> 8;
        rgb[2] = (color & 0x000000ff);
        return rgb;
    }

    /**
     * 取得单色
     **/
    public static int decodeColor(int color) {
        int g = (color & 0x000000ff);
        return g;
    }

    /**
     * 合并三色
     **/
    public static int encodeColor(int rgb[]) {
        int color = (255 << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
        return color;
    }

    /**
     * 灰度化计算
     **/
    public static int getBrightness(int color) {
        // TODO Auto-generated method stub
        int r = (color & 0x00ff0000) >> 16; //首先将颜色值与十六进制表示的00ff0000进行“与”运算，运算结果除了表示红色的数字值之外，GGBB部分颜色都为0，在将结果向右移位16位，得到的就是红色值。所以这句代码主要用来从一个颜色中抽取其组成色---红色的值。
        int g = (color & 0x0000ff00) >> 8;
        int b = (color & 0x000000ff);
        int y = Math.round(0.3f * r + 0.59f * g + 0.11f * b);
        y = y < 0 ? 0 : y;
        y = y > 255 ? 255 : y;
        return y;
    }

    /**
     * 取得灰度最大值
     **/
    public static int getMaxGray(int srcRGBs[]) {
        int maxGray = 0;
        for (int i = 0; i < srcRGBs.length; i++) {
            if (srcRGBs[i] > maxGray) {
                maxGray = srcRGBs[i];
            }
        }
        return maxGray;
    }

    /**
     * RGB_TO_YHS
     **/
    public static float[] convertRGBToYHS(int color, float yhs[]) {
        if (yhs == null)
            yhs = new float[3];
        int r = (color & 0x00ff0000) >> 16;
        int g = (color & 0x0000ff00) >> 8;
        int b = (color & 0x000000ff);

        yhs[0] = (float) (0.3 * r + 0.59 * g + 0.11 * b);
        double c1 = 0.7 * r - 0.59 * g - 0.11 * b;
        double c2 = -0.3 * r - 0.59 * g + 0.89 * b;
        yhs[2] = (float) Math.sqrt(c1 * c1 + c2 * c2);
        if (yhs[2] < 0.005)
            yhs[1] = 0;
        else {
            yhs[1] = (float) Math.atan2(c1, c2);
            if (yhs[1] < 0)
                yhs[1] += (float) Math.PI * 2;
        }

        return yhs;
    }

}
