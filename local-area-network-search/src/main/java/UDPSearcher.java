import java.io.IOException;
import java.net.*;

/**
 * UDP 搜索者，用于搜索服务支持方
 */
public class UDPSearcher {
    public static void main(String[] args) throws IOException {

        System.out.println("UDPSearcher Started.");
        //搜索方无需指定端口，让系统分配
        DatagramSocket ds = new DatagramSocket();
        //发送数据
        String requestData = "Hello World...";

        byte[] sendBytes = requestData.getBytes();
        DatagramPacket res = new DatagramPacket(sendBytes, sendBytes.length);
        res.setAddress(InetAddress.getLocalHost());
        res.setPort(20000);
        ds.send(res);

        // 构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
        //接收
        ds.receive(receivePack);
        //打印接收到的信息
        //发送者的ip
        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();

        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDPSearcher receive from ip: "+ ip + " port: " + port + " data: " + data);

        System.out.println("UDPSearcher Finished.");
        ds.close();
    }
}
