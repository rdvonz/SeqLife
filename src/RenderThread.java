import java.util.TimerTask;


public class RenderThread extends TimerTask {
    private int[] color = Picaso.getRGB16(0, 255, 0);
    private int[] color2 = Picaso.getRGB16(0, 0, 255);
    private int[] black = Picaso.getRGB16(0, 0, 0);

    private int[] columnData = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    private int columnIndex = 10;
    private boolean newData = false;

    int x = 39;
    int y = 29;

    public void run() {
        if (Visualizer.isRunning()) {

            int temp = Sequencer.getPosition() % 8;

            if (temp != columnIndex) {
                columnIndex = temp;
                for (int i = 0; i < 8; i++) {
                    //Boolean temp2 = sequencerFrame.seq.grid[columnIndex][i];
                    Boolean temp2 = Sequencer.getBool(columnIndex, i);
                    if (temp2 == null) {
                        columnData[i] = 0;
                        continue;
                    }
                    if (temp2 == true) {
                        columnData[i] = 1;
                    }
                    if (temp2 == false) {
                        columnData[i] = 0;
                    }
                }
                newData = true;
            }
            if (newData) {
                Picaso.drawRectangle(1, 1, 318, 238, black);
                for (int i = 0; i < 8; i++) {
                    int fill = columnData[i] & 1;
                    if (fill == 0) {
                        Picaso.drawRectangle(1 + (x * columnIndex), 1 + (y * i), 1 + (x * (columnIndex + 1)), 1 + (y * (i + 1)), black);
                    } else {
                        if (fill == 1) {
                            Picaso.drawRectangle(1 + (x * columnIndex), 1 + (y * i), 1 + (x * (columnIndex + 1)), 1 + (y * (i + 1)), color);
                        } else {
                            System.out.println("out of bounds fill");
                        }
                    }
                }
                Picaso.swapFrame();
                newData = false;
            }
        }

    }

    public void setNotes(int[] columnData, int columnIndex) {
        this.columnData = columnData;
        this.columnIndex = columnIndex;
        newData = true;
    }
}
