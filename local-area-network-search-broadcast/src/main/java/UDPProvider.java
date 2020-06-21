import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

/**
 * UDP提供者，用于提供服务
 */
public class UDPProvider {

    public static void main(String[] args) throws IOException {
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
//        Thread thread = new Thread(provider);

        provider.start();
        //键盘输入任何信息之后，都会退出
        System.in.read();
        provider.exit();

    }

    private static class Provider extends Thread{
        private final String sn;
        private DatagramSocket ds = null;
        private boolean done = false;

        private Provider(String sn) {
            this.sn = sn;
        }

        public void run() {
            System.out.println("UDPProvider Started.");

            try{
                ds = new DatagramSocket(20000);
                while(!done){
                    // 构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
                    //接收
                    ds.receive(receivePack);
                    //打印接收到的信息
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();

                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("UDPProvider receive from ip: "+ ip + " port: " + port + " data: " + data);


                    int responsePort = MessageCreator.parsePort(data);
                    if(responsePort != -1){
                        //构建回送数据
                        String responseData = MessageCreator.buildWithSN(sn);
                        byte[] bytes = responseData.getBytes();
                        DatagramPacket res = new DatagramPacket(bytes, bytes.length, receivePack.getAddress(), responsePort);
                        ds.send(res);
                    }
                }
            }catch (Exception e){

            }finally {
                close();
            }
            System.out.println("UDPProvider Finished.");

        }
        private void close(){
            if(ds != null){
                ds.close();
                ds = null;
            }
        }

        public void exit(){
            done = true;
            close();
        }
    }

}
