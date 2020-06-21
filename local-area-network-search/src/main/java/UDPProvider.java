import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * UDP提供者，用于提供服务
 */
public class UDPProvider {

    public static void main(String[] args) throws IOException {

        System.out.println("UDPProvider Started.");
        DatagramSocket ds = new DatagramSocket(20000);
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
        System.out.println("UDPProvider receive from ip: "+ ip + " port: " + port + " data: " + data);

        //构建回送数据
        String responseData = "Receive data with len : " + dataLen;

        byte[] bytes = responseData.getBytes();
        DatagramPacket res = new DatagramPacket(bytes, bytes.length, receivePack.getAddress(), receivePack.getPort());
        ds.send(res);
        System.out.println("UDPProvider Finished.");
        ds.close();



    }
}
