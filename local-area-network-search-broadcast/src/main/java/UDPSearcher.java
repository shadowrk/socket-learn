import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * UDP 搜索者，用于搜索服务支持方
 */
public class UDPSearcher {

    private static final int LISTEN_PORT = 30000;
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("UDPSearcher Started.");
        Listener listen = listen();
        sendBroadCast();

        System.in.read();

        List<Device> devices = listen.getDevicesAndClose();
        for (Device device : devices) {
            System.out.println("Device: "+device.toString());
        }

        //完成
        System.out.println("UDPSearcher Finished");




    }

    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(
                LISTEN_PORT,
                countDownLatch
        );
        listener.start();
        countDownLatch.await();
        return listener;

    }

    private static void sendBroadCast() throws IOException {
        System.out.println("UDPSearcher sendBroadCast Started.");
        //搜索方无需指定端口，让系统分配
        DatagramSocket ds = new DatagramSocket();
        //发送数据
        String requestData = MessageCreator.buildWithPort(LISTEN_PORT);

        byte[] sendBytes = requestData.getBytes();
        DatagramPacket res = new DatagramPacket(sendBytes, sendBytes.length);
        res.setAddress(InetAddress.getByName("255.255.255.255"));
        res.setPort(20000);
        ds.send(res);
        ds.close();

        System.out.println("UDPSearcher sendBroadCast Finished.");
    }
    private static class Device{
        final int port;
        final String ip;
        final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    private static class Listener extends Thread{
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private List<Device> devices = new ArrayList<Device>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            // 通知启动
            countDownLatch.countDown();
            try{
                ds = new DatagramSocket(listenPort);
                while (!done){
                    //构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
                    ds.receive(receivePacket);

                    String ip = receivePacket.getAddress().getHostAddress();
                    int port = receivePacket.getPort();
                    int dataLen = receivePacket.getLength();
                    String data = new String(receivePacket.getData(), 0, dataLen);
                    System.out.println("UDPSearcher receive from ip: "+ ip + " port: " + port + " data: " + data);

                    String sn = MessageCreator.parseSN(data);
                    if(sn != null){
                        Device device = new Device(port, ip, sn);
                        devices.add(device);
                    }

                }

            }catch (Exception e){

            }finally {
                close();

            }
            System.out.println("UDPSearcher listener finished.");
        }

        private void close(){
            if(ds != null){
                ds.close();
                ds = null;
            }
        }

        List<Device> getDevicesAndClose(){
            done = true;
            close();
            return devices;
        }
    }
}
