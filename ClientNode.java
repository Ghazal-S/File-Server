import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Ghazal Sadeghian
 * @author Maryam Mohammadi Ardehali
 */
public class ClientNode {

    String str;
    int i;
    byte b;
    Socket clientSocket;
    Scanner userInputScanner;
    DataInputStream inFromServer;
    DataOutputStream outToServer;
    ArrayList<File> segments = new ArrayList<File>();

    public ClientNode(String s) {
        try {

            clientSocket = new Socket(s, 5000);
            System.out.println("connected");
            userInputScanner = new Scanner(System.in);
            inFromServer = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    public void sendServer(String str) {
        try {
            outToServer.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendServerBytes(byte[] b) {
        try {
            outToServer.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fromServer() {
        try {
            str = inFromServer.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public byte bytesfromServer() {
        try {
            b = inFromServer.readByte();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


}


