import java.util.Timer;


public class Visualizer {
    private static Timer timer;
    private static boolean timerRunning = false;
    private static RenderThread thread;

    private static void setupPicaso(String port) {
        Picaso.init(port);
        Picaso.setResolution(0);
        Picaso.setBaud(0);
        Picaso.drawRectangle(0, 0, 319, 239, 1, Picaso.getRGB16(255, 255, 255));
        Picaso.swapFrame();
        Picaso.clearScreen();
        Picaso.drawRectangle(0, 0, 319, 239, 1, Picaso.getRGB16(255, 255, 255));
        Picaso.setFill(0);
    }

    public static void start(String port) {
        setupPicaso(port);
        timer = new Timer();
        thread = new RenderThread();
        timer.scheduleAtFixedRate(thread, 0, 100);
        timerRunning = true;
    }

    public static void stop() {
        if (timerRunning) {
            timer.cancel();
            timerRunning = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Picaso.close();
        }
    }

    public static boolean isRunning() {
        return timerRunning;
    }

    public static void setNotes(int[] columnData, int columnIndex) {
        thread.setNotes(columnData, columnIndex);
    }

    public static String[] getPorts() {
        return SerialIO.getPorts();
    }
}
