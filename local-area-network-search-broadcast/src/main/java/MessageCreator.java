public class MessageCreator {

    private static final String SN_HEADER = "收到暗号，这是(SN)：";
    private static final String PORT_HEADER = "发送暗号，收到回电(Port)：";

    public static String buildWithPort(int port){
        return PORT_HEADER+port;
    }

    public static int parsePort(String data){
        if(data.startsWith(PORT_HEADER)){
            return Integer.valueOf(data.substring(PORT_HEADER.length()));
        }

        return -1;
    }

    public static String buildWithSN(String sn){
        return SN_HEADER + sn;
    }

    public static String parseSN(String data){
        if(data.startsWith(SN_HEADER)){
            return data.substring(SN_HEADER.length());
        }
        return null;
    }
}
