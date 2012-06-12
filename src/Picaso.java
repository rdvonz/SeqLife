import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class Picaso {
    private static byte nullTerminator = 0x00;
    private static int m_resIndex = 0;
    private static boolean waitForACK = true;
    private static int frame = 0;

    public static SerialIO serial;

    public static void resetDisplay(String port) {
        serial.close();
        init(port);
    }

    public static void resetDisplay(String port, int bps) {
        serial.close();
        init(port, bps);
    }

    public static byte getResponse() {
        Integer response = 0;
        if (waitForACK) {
            response = serial.read();
            //System.out.println(Integer.toHexString(response));
        }

        return response.byteValue();
    }

    public static void init(String port) {
        init(port, 9600);
    }

    public static void init(String port, int bps) {
        serial = new SerialIO();
        serial.connect(port, bps);
        try {
            Thread.sleep(1000);
            send(0x55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getResponse();
    }

    public static void close() {
        if (serial != null) {
            serial.close();
        }
    }

    public static void setBaud(int data) {
        send(0x51);
        send(0x0F);
        sleep(100);
        serial.setBaud(282353);
    }

    public static void swapFrame() {
        send(0x59);
        send(0x09);
        send(frame);
        if (waitForACK) {
            getResponse();
        }

        send(0x59);
        send(0x0B);
        send(frame ^ 1);
        if (waitForACK) {
            getResponse();
        }

        frame = frame ^ 1;

    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clearScreen() {
        send(0x45);
        getResponse();
    }

    public static void send(byte data) {
        serial.write(data);
    }

    private static void send(char data) {
        send((byte) data);
    }

    private static void send(int data) {
        send((byte) data);
    }

    public static void setResolution(int resIndex) {
        m_resIndex = resIndex;
        send('Y');
        send(0x0C);
        send((byte) resIndex);
        getResponse();
        send('U');
        getResponse();
    }

    private static boolean checkBounds(int xMin, int yMin, int xMax, int yMax) {
        int xResMin = 0, yResMin = 0, xResMax = 0, yResMax = 0;
        switch (m_resIndex) {
            case 0:
                //xResMax = 310;
                //yResMax = 210;
                xResMax = 320;
                yResMax = 240;
                break;
            case 1:
                xResMax = 620;
                yResMax = 420;
                break;
            case 2:
                xResMax = 800;
                yResMax = 560;
                break;
        }
        boolean result = (xMin >= xResMin) && (yMin >= yResMin) && (xMax < xResMax) && (yMax < yResMax);
        if (result == false) {
            System.out.print(xMin + " " + yMin + " " + xMax + " " + yMax + " ");
        }
        return (xMin >= xResMin) && (yMin >= yResMin) && (xMax < xResMax) && (yMax < yResMax);
    }

    private static boolean checkTextBounds(int xMin, int yMin, byte font) {
        int xResMin = 0, yResMin = 0, xResMax = 0, yResMax = 0;
        switch (m_resIndex) {
            case 0:
                switch (font) {
                    case 0:
                        xResMax = 50;
                        yResMax = 25;
                        break;
                    case 1:
                        xResMax = 37;
                        yResMax = 25;
                        break;
                    case 2:
                        xResMax = 37;
                        yResMax = 16;
                        break;
                }
                break;
            case 1:
                switch (font) {
                    case 0:
                        xResMax = 102;
                        yResMax = 51;
                        break;
                    case 1:
                        xResMax = 76;
                        yResMax = 51;
                        break;
                    case 2:
                        xResMax = 76;
                        yResMax = 34;
                        break;
                }
                break;
            case 2:

                break;
        }

        return (xMin >= xResMin) && (yMin >= yResMin);
    }

    public static int getRGB8(int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return (red & (0xFF << 5)) | ((green & (0xFF << 5)) >> 3) | ((blue & (0xFF << 6)) >> 6);
    }

    public static int[] getRGB16(int r, int g, int b) {
        int[] result = new int[2];

        int newRed = r & (0xFF);
        newRed = newRed >> 3;

        int newGreen = g & (0xFF);
        newGreen = newGreen >> 2;

        int newBlue = b & (0xFF);
        newBlue = newBlue >> 3;

        int red = newRed << 3;
        int greenHigh = newGreen >> 3;
        int greenLow = (newGreen >> 3) << 5;
        int blue = newBlue;

        result[1] = (red | greenHigh) & (0xFF);
        result[0] = (greenLow | blue) & (0xFF);

        return result;
    }

    private static byte getMSB(int value) {
        return (byte) (value >> 8);
    }

    private static byte getLSB(int value) {
        return (byte) (value & 0xFF);
    }

    public static void clear() {
        send('E');
        getResponse();
    }

    public static void setFill(int filled) {
        if (filled < 2) {
            send('p');
            send(filled);
            getResponse();
        }
    }

    public static void drawText(int column, int row, int font, String text, int[] color) {
        if (checkTextBounds((byte) column, (byte) row, (byte) font) == false) {
            System.out.println("Bounds check failed");
            return;
        }
        setFill(0);
        send('s');
        send(column);
        send(row);
        send(font);
        send(color[1]);
        send(color[0]);
        for (int i = 0; i < text.length(); i++) {
            send(text.charAt(i));
        }
        send(nullTerminator);

        getResponse();
    }

    public static void addBitmap(int format, int index, int[][] array) {
        send(0x41);
        send(format);
        send(index);

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[0].length; x++) {
                send(array[y][x]);
            }
        }
        getResponse();
    }

    public static void drawBitmap(int format, int index, int x, int y, int[] color) {
        send(0x44);
        send(format);
        send(index);

        send(getMSB(x));
        send(getLSB(x));

        send(getMSB(y));
        send(getLSB(y));

        send(color[1]);
        send(color[0]);

        if (waitForACK) {
            getResponse();
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, int[] color) {
        if (checkBounds(x1, y1, x2, y2) == false) {
            System.out.println("Bounds check failed");
            return;
        }
        send('L');

        send(getMSB(x1));
        send(getLSB(x1));

        send(getMSB(y1));
        send(getLSB(y1));

        send(getMSB(x2));
        send(getLSB(x2));

        send(getMSB(y2));
        send(getLSB(y2));

        send(color[1]);
        send(color[0]);

        getResponse();
    }

    public static void drawRectangle(int x1, int y1, int x2, int y2, int filled, int[] color) {
        if (checkBounds(x1, y1, x2, y2) == false) {
            System.out.println("Bounds check failed");
            return;
        }

        setFill((byte) filled);
        send('r');

        send(getMSB(x1));
        send(getLSB(x1));

        send(getMSB(y1));
        send(getLSB(y1));

        send(getMSB(x2));
        send(getLSB(x2));

        send(getMSB(y2));
        send(getLSB(y2));

        send(color[1]);
        send(color[0]);

        getResponse();
    }

    public static void drawRectangle(int x1, int y1, int x2, int y2, int[] color) {
        if (checkBounds(x1, y1, x2, y2) == false) {
            System.out.println("Bounds check failed");
            return;
        }

        send('r');

        send(getMSB(x1));
        send(getLSB(x1));

        send(getMSB(y1));
        send(getLSB(y1));

        send(getMSB(x2));
        send(getLSB(x2));

        send(getMSB(y2));
        send(getLSB(y2));

        send(color[1]);
        send(color[0]);

        getResponse();
    }

    public static void drawImage(int x1, int y1, int scale, boolean colorMode, BufferedImage image) {
        AffineTransform at = new AffineTransform();
        float scaleF = (float) Math.pow(2.0, scale);
        at.scale(1.0 / scaleF, 1.0 / scaleF);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        image = scaleOp.filter(image, null);
        int width = image.getWidth();
        int height = image.getHeight();
        if (checkBounds(x1, y1, x1 + width, y1 + height) == false) {
            System.out.println("Bounds check failed");
            return;
        }
        send(0x49);

        send(getMSB(x1));
        send(getLSB(x1));

        send(getMSB(y1));
        send(getLSB(y1));

        send(getMSB(width));
        send(getLSB(width));

        send(getMSB(height));
        send(getLSB(height));

        if (colorMode) {
            send(0x10);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb >> 0) & 0xFF;
                    int[] rgb16 = getRGB16(r, g, b);
                    send(rgb16[1]);
                    send(rgb16[0]);
                }
            }
        } else {
            send(0x08);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    send(getRGB8(image.getRGB(x, y)));
                }
            }
        }

        getResponse();
    }

    public static void drawCircle(int x, int y, int radius, int filled, int[] color) {
        //Check to see if we are drawing on the screen
        /*
          if (CheckBounds(x - radius, y - radius, x + radius, y + radius) == false)
          {
              System.out.println("Bounds check failed");
              return;
          }*/

        setFill(filled);

        send('C');

        send(getMSB(x));
        send(getLSB(x));

        send(getMSB(y));
        send(getLSB(y));

        send(getMSB(radius));
        send(getLSB(radius));

        send(color[1]);
        send(color[0]);

        getResponse();
    }

    public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int[] color) {
        if (checkBounds(x1, y1, x1, y1) == false ||
                checkBounds(x2, y2, x2, y2) == false ||
                checkBounds(x3, y2, x3, y3) == false) {
            System.out.println("Bounds check failed");
            return;
        }
        if (!(x2 < x1 && x3 > x2 && y2 > y1 && y3 > y1)) {
            System.out.println("Bounds check failed 2");
            return;
        }

        setFill(0);

        send('G');

        send(getMSB(x1));
        send(getLSB(x1));

        send(getMSB(y1));
        send(getLSB(y1));

        send(getMSB(x2));
        send(getLSB(x2));

        send(getMSB(y2));
        send(getLSB(y2));

        send(getMSB(x3));
        send(getLSB(x3));

        send(getMSB(y3));
        send(getLSB(y3));

        send(color[1]);
        send(color[0]);

        getResponse();
    }

}
