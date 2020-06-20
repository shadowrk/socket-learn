import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 读取流超时时间
        socket.setSoTimeout(3000);
        // 连接本地端口 2000， 超时时间3000
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("已发起服务器连接，并进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + "P: "+socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress()+"P: "+socket.getPort());
        try{
            // 发送数据
            todo(socket);
        }catch (Exception e){
            System.out.println("异常关闭");
        }
        socket.close();
        System.out.println("客户端以退出");
    }

    private static void todo(Socket client) throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        //得到socket的输出流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);
        //得到socket的输入流，并转换为BufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketInputStream = new BufferedReader(new InputStreamReader(inputStream));
        boolean flag = true;
        do{
            // 读取一行
            String str = input.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            String echo = socketInputStream.readLine();
            if("bye".equalsIgnoreCase(echo)){
                flag = false;
            }else{
                System.out.println(echo);
            }
        }while (flag);
        socketInputStream.close();
        socketPrintStream.close();




    }
}
