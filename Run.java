import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Ghazal Sadeghian
 * @author Maryam Mohammadi Ardehali
 */
//"127.0.0.1"
public class Run {
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception ignored){}
        System.out.println("Enter the IP address of a machine running the capitalize server:");
        Scanner scanner = new Scanner(System.in);
        String serverAddress = scanner.nextLine();
        ClientNode client1 = new ClientNode(serverAddress);
        GUI gui = new GUI(client1);

    }
}
