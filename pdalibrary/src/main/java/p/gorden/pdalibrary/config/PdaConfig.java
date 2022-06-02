package p.gorden.pdalibrary.config;

public class PdaConfig {
    public static String SQLUSERNAME = "";
    public static String SQLPWS = "";
    public static String ERPUSERNAME = "";
    public static String ERPPWS = "";
    public static String WEBSERVERURL = "";
    public static String APKURL = "";

    public static String IP = "";
    public static String DATABASE = "";

    public static boolean ISKANBAN = false;
    public static boolean USEWEB = false;


    public static String getDatabaseUrl() {

        if (IP.isEmpty()) {
            throw new IllegalArgumentException("IP should be initialized!");
        }

        if (DATABASE.isEmpty()) {
            throw new IllegalArgumentException("DATABASE should be initialized!");
        }
        return "jdbc:jtds:sqlserver://" +
                IP + ";DatabaseName=" +
                DATABASE + "";
    }
}
