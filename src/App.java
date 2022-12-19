import server.WebServer;

public class App {
    public static void main(String[] args) throws Exception {
        com.formdev.flatlaf.FlatDarkLaf.install();
        new WebServer();
    }
}