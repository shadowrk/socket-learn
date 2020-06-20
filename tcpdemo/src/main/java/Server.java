import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器端
 */
public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(2000);
        System.out.println("服务器准备就绪");
        System.out.println("客户端信息：" + server.getInetAddress() + "P: "+server.getLocalPort());
        // 等待客户端连接
        for(;;){
            Socket client = server.accept();
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }

    }

    private static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接："+socket.getInetAddress() + "P: "+socket.getPort());
            try{
                // 获取输入流

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //获取输出流
                PrintStream out = new PrintStream(socket.getOutputStream());
                boolean flag = true;
                do{
                    String s = in.readLine();

                    if("bye".equalsIgnoreCase(s)){
                        flag = false;
                        out.println("bye");
                    }else{
                        System.out.println(s);
                        out.println("回送："+s.length());
                    }
                }while (flag);

                in.close();
                out.close();

            }catch (Exception e){
                System.out.println("连接异常断开");
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已退出："+socket.getInetAddress() + "P: "+socket.getPort());
        }
    }

}
