import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Ghazal Sadeghian
 * @author Maryam Mohammadi Ardehali
 */
public class ServerNode {

    public static void main(String[] args) throws Exception {
        ArrayList<DataInputStream> inFromClient = new ArrayList<>();
        ArrayList<DataOutputStream> outToClient = new ArrayList<>();
        ArrayList<String> clients = new ArrayList<>();
        HashMap<String, String> manageSegments = new HashMap<>();
        int clientNumber = 0;
        try (ServerSocket server = new ServerSocket(5000)) {
            while (true) {
                System.out.println("Waiting for a client to connect...");
                Capitalizer c = new Capitalizer(server.accept(), clientNumber++);
                clients.add(c.getter());
                inFromClient.add(c.getIn());
                outToClient.add(c.getOut());
                c.start();

            }
        }
    }
}

class Capitalizer extends Thread {


    private int clientNumber;
    private ArrayList<File> files = new ArrayList<>();
    // private List<File> files = Collections.synchronizedList(new ArrayList<File>());
    private Random random = new Random();
    private DataInputStream in;
    private DataOutputStream out;
    private Socket connectionSocket;

    public Capitalizer(Socket socket, int clientNumber) {
        connectionSocket = socket;
        this.clientNumber = clientNumber;
        System.out.println("New client #" + clientNumber + " connected at " + socket);
        System.out.println(socket.getPort());
        System.out.println(socket.getInetAddress().getHostAddress());

    }

    public String getter() {
        return connectionSocket.getInetAddress().getHostAddress();
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void run() {
        try {

            File testFile1 = new File("D:\\ApProjects\\Final\\icon2.png");
            File testFile2 = new File("D:\\ApProjects\\Final\\apTest.txt");
            File testFile3 = new File("D:\\ApProjects\\Final\\word2.docx");
            File testFile4 = new File("D:\\ApProjects\\Final\\rank55.JPG");
            File testFile5 = new File("D:\\ApProjects\\Final\\salam.docx");
            File testFile6 = new File("D:\\ApProjects\\Final\\HW7(9533054).pdf");
            File testFile7 = new File("D:\\ApProjects\\Final\\excelTest.xlsx");

            files.add(testFile1);
            files.add(testFile2);
            files.add(testFile3);
            files.add(testFile4);
            files.add(testFile5);
            files.add(testFile6);
            files.add(testFile7);

            in = new DataInputStream(new BufferedInputStream(connectionSocket.getInputStream()));
            out = new DataOutputStream(connectionSocket.getOutputStream());
            //out.writeUTF("Hello, you are client #" + clientNumber);
            System.out.println("client accepted");


            while (true) {
                String line = in.readUTF();
                String[] splitted = line.split(" ");
                if (line.startsWith("Delete")) synchronized (files) {
                    Iterator i = files.iterator(); // Must be in synchronized block
                    while (i.hasNext()) {
                        File f = (File) i.next();
                        if (f.getName().equals(splitted[1])) {
                            f.delete();
                            files.remove(f);

                        }
                    }
                    for (File file : files) {
                        System.out.println(file.getName());
                    }
                }

                if (line.startsWith("Rename")) {
                    for (File file : files) {
                        if ((file.getName()).equals(splitted[1])) {
                            File file2 = new File(splitted[2]);
                            file.renameTo(file2);
                            files.remove(file);
                            files.add(file2);

                        }
                    }
                }

//                if (line.startsWith("Duplicate")) {
//                    for (File file : files) {
//                        if (file.getName().equals(splitted[1])) {
//                            Path path = Paths.get(file.getAbsolutePath());
//                            // ??? Files.copy(path,splitted[2]);
//                        }
//                    }
//                }

                if (line.startsWith("Download")) {
                    for (File file : files) {
                        if (file.getName().equals(splitted[1])) {
                            Path Path = Paths.get(file.getAbsolutePath());
                            byte[] bytes = Files.readAllBytes(Path);

                            int i = bytes.length;
                            System.out.println(i);
                            for (int m = 0; m < i; m++) {
                                System.out.println(bytes[m]);
                            }
                            out.writeUTF("" + i);
                            out.write(bytes);
                        }
                    }
                }
                if (line.startsWith("update")) {
                    for (File file : files) {
                        out.writeUTF(file.getName());
                    }
                    out.writeUTF("end");
                }
                if (line.startsWith("properties")) {
                    for (File file : files) {
                        if (file.getName().equals(splitted[1])) {
                            out.writeUTF("Filename:" + file.getName() + "              Size:" + file.getUsableSpace());

                        }
                    }
                }
                if (line.startsWith("upload")) {

                    String s = in.readUTF();
                    System.out.println(s);
                    System.out.println("received");
                    int size = Integer.parseInt(s);
                    System.out.println("size = " + size);
                    System.out.println(size);
                    byte[] bytes = new byte[size];
                    for (int k = 0; k < size; k++) {
                        bytes[k] = in.readByte();
                        System.out.println(bytes[k]);
                    }
                    try {
                        String name = in.readUTF();
                        FileOutputStream fos = new FileOutputStream("D:\\ApProjects\\Final\\server\\" + name);
                        fos.write(bytes);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }


            }


        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } finally {
            try {
                connectionSocket.close();
            } catch (IOException e) {
            }
            System.out.println("Connection with client #" + clientNumber + " closed");
        }
    }


//    public  void splitFile(File f) throws IOException {
//        int partCounter = 1;// Names parts from 0001, 0002, 0003, ...
//        int sizeOfFiles = 1024 * 1024;// 1 MB
//        byte[] buffer = new byte[sizeOfFiles];
//        String fileName = f.getName(); //try-with-resources to ensure closing stream
//        try (FileInputStream fis = new FileInputStream(f);
//             BufferedInputStream bis = new BufferedInputStream(fis)) {
//            int bytesAmount = 0;
//            while ((bytesAmount = bis.read(buffer)) != -1) { //write each chunk of data into separate file with different number in name
//                String filePartName = String.format("%s.%04d", fileName, partCounter++);
//                File newFile = new File(f.getParent(), filePartName);
//                Path filePath = Paths.get(f.getParent(), filePartName);
//
//                try (FileOutputStream out = new FileOutputStream(newFile)) {
//                    out.write(buffer, 0, bytesAmount);
//                }
//                int i = clients.size();
//                int j = random.nextInt(i);
//                int k = random.nextInt(i);
//                while(j == k) {
//                    j = random.nextInt(i);
//                    k = random.nextInt(i);
//                }
//                byte[] fileBytes = Files.readAllBytes(filePath);
//                outToClient.get(j).write(fileBytes);
//                outToClient.get(k).write(fileBytes);
//                String firstAcceptor = clients.get(j);
//                String secondAcceptor = clients.get(k);
//                manageSegments.put(firstAcceptor,filePartName);
//                manageSegments.put(secondAcceptor,filePartName);
//
//
//            }
//        }
//    }


}

