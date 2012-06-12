import jssc.*;

import java.util.concurrent.ArrayBlockingQueue;

public class SerialIO {
    private SerialPort serialPort;
    private int bufferSize = 256;
    private ArrayBlockingQueue<Integer> inputBuffer = new ArrayBlockingQueue<Integer>(bufferSize);
    private boolean portActive = false;

    void connect(String portName, int BPS) {
        boolean portMatch = false;
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length > 0) {
            for (int i = 0; i < portNames.length; i++) {
                System.out.println(portNames[i]);
                if (portNames[i].equals(portName)) {
                    portMatch = true;
                }
            }
            if (portMatch) {
                serialPort = new SerialPort(portName);
            } else {
                serialPort = new SerialPort(portNames[0]);
            }
            try {
                System.out.println("Port opened: " + serialPort.openPort());
                System.out.println("Params setted: " + serialPort.setParams(BPS, 8, 1, 0));
                int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                serialPort.setEventsMask(mask);
                serialPort.addEventListener(new SerialPortEventListener() {
                    public void serialEvent(SerialPortEvent arg0) {
                        if ((arg0.isRXCHAR()) && (arg0.getEventValue() > 0)) {
                            int[] input;
                            try {
                                input = serialPort.readIntArray();
                                for (int i = 0; i < input.length; i++) {
                                    inputBuffer.put(input[i]);
                                }
                            } catch (SerialPortException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                portActive = true;

            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No Ports Found");
        }
    }

    public void cleanBuffers() {
        inputBuffer = new ArrayBlockingQueue<Integer>(bufferSize);
    }

    public void setBaud(int bps) {
        try {
            System.out.println("Params setted: " + serialPort.setParams(bps, 8, 1, 0));
            cleanBuffers();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (portActive) {
                portActive = false;
                serialPort.removeEventListener();
                serialPort.closePort();
            }

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public boolean getPortActive() {
        return portActive;
    }

    public static String[] getPorts() {
        String[] portNames = SerialPortList.getPortNames();
        return portNames;
    }

    public Integer read() {
        try {
            return inputBuffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(int data) {
        try {
            serialPort.writeInt(data);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}