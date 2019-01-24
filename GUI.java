import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Ghazal Sadeghian
 * @author Maryam Mohammadi Ardehali
 */
public class GUI {

    String newName;
    JFrame frame;
    JList list;
    JPanel listPanel;
    JPanel previewPanel;
    JPanel optionPanel;
    JPanel propertiesPanel;
    JScrollPane scroll;
    JTextArea display;
    JTabbedPane tab;
    JButton download;
    JButton duplicate;
    JButton rename;
    JButton delete;
    JMenuBar menuBar;
    JMenu file;
    JMenu upload;
    JMenu tools;
    JMenu help;
    ClientNode client;
    JTextArea propertiesText;
    ArrayList<File> files = new ArrayList<File>();
    ArrayList<String> fileNames = new ArrayList<String>();

    public GUI(ClientNode client) {

        this.client = client;
        frame = new JFrame();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setSize(350, 300);
        //frame.setLocation(100, 200);
        frame.setLocation(x, y);
        frame.setTitle("File System");
        ImageIcon logo = new ImageIcon("D:\\ApProjects\\Final\\icon2.png");
        frame.setIconImage(logo.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        menuBar = new JMenuBar();
        file = new JMenu("File");
        upload = new JMenu("Upload");
        JMenuItem choose = new JMenuItem("choose");
        upload.add(choose);
        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendServer("upload");
                JFrame uploadFrame = new JFrame();
                uploadFrame.setSize(new Dimension(500, 500));
                uploadFrame.setLayout(new BorderLayout());
                JTextArea uploadText = new JTextArea();
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(1, 2));
                uploadText.setFont(new Font("Arial", 14, 14));
                uploadText.setEditable(true);
                JLabel uploadLabel = new JLabel();
                uploadLabel.setText("Enter directory : ");
                uploadFrame.add(uploadLabel, BorderLayout.NORTH);
                uploadFrame.add(uploadText, BorderLayout.CENTER);
                JButton path = new JButton();
                path.setText("path");
                JButton name = new JButton();
                name.setText("name");
                buttonPanel.add(path);
                buttonPanel.add(name);
                uploadFrame.add(buttonPanel, BorderLayout.SOUTH);
                uploadFrame.setTitle("Upload");
                uploadFrame.setVisible(true);
                name.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String p = uploadText.getText();
                        client.sendServer(p);
                        uploadText.setText("");
                    }
                });
                path.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String s = uploadText.getText();
                        uploadText.setText("");
                        File f = new File(s);
                        Path Path = Paths.get(f.getAbsolutePath());
                        try {
                            byte[] bytes = Files.readAllBytes(Path);

                            int i = bytes.length;
                            System.out.println(i);
                            for (int m = 0; m < i; m++) {
                                System.out.println(bytes[m]);
                            }
                            client.sendServer("" + i);
                            client.sendServerBytes(bytes);
                        } catch (IOException g) {
                            g.printStackTrace();
                        }
                    }
                });
            }
        });
        tools = new JMenu("Tools");
        JMenuItem search = new JMenuItem("Search");
        tools.add(search);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame searchFrame = new JFrame();
                searchFrame.setTitle("Search");
                searchFrame.setSize(new Dimension(500, 100));
                JPanel searchBar = new JPanel();
                searchBar.setLayout(new BorderLayout());
                JTextArea searchText = new JTextArea();
                searchBar.add(searchText, BorderLayout.WEST);
                searchText.setFont(new Font("Arial", 14, 14));
                searchText.setEditable(true);
                JButton searchButton = new JButton();
                searchButton.setText("SEARCH");
                searchBar.add(searchButton, BorderLayout.EAST);
                searchFrame.add(searchBar);
                searchFrame.setVisible(true);
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String l = searchText.getText();
                        if (fileNames.contains(l)) {
                            searchText.setText("FOUND :)");
                        } else {
                            searchText.setText("NOT FOUND !");
                        }
                    }
                });

            }
        });
        help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame helpFrame = new JFrame();
                helpFrame.setTitle("About");
                helpFrame.setSize(new Dimension(400, 200));
                helpFrame.setLayout(new BorderLayout());
                JTextArea helpText = new JTextArea();
                helpText.setFont(new Font("Arial", 14, 14));
                helpText.setText(" AP Final project:Ghazal and Maryam ");
                helpText.setEditable(false);
                helpFrame.add(helpText, BorderLayout.CENTER);
                helpFrame.setVisible(true);

            }
        });
        JMenuItem update = new JMenuItem("update");
        file.add(update);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendServer("update list");
                String s = client.fromServer();
                while (!(s.equals("end"))) {
                    fileNames.add(s);
                    s = client.fromServer();
                }
                list.setListData(fileNames.toArray());


            }
        });
        menuBar.add(file);
        menuBar.add(upload);
        menuBar.add(tools);
        menuBar.add(help);
        frame.setJMenuBar(menuBar);
        createPanels();
        frame.setVisible(true);
    }

    public void createPanels() {

//        for(File file: files){
//            String name = file.getName();
//            fileNames.add(name);
//        }
//        listPanel = new JPanel();
//        list = new JList(fileNames.toArray());
//        list.setVisibleRowCount(36);
//        scroll = new JScrollPane(list);
//        listPanel.add(scroll);
//        frame.add(listPanel,BorderLayout.WEST);
        listPanel = new JPanel();
        list = new JList();
        list.setVisibleRowCount(40);
        scroll = new JScrollPane(list);
        listPanel.add(scroll);
        frame.add(listPanel, BorderLayout.WEST);
        previewPanel = new JPanel();
        propertiesPanel = new JPanel();
        propertiesText = new JTextArea(400, 400);
        propertiesText.setFont(new Font("Arial", 14, 14));
        propertiesPanel.setLayout(new BorderLayout());
        JButton propertiesButton = new JButton();
        propertiesPanel.add(propertiesButton, BorderLayout.SOUTH);
        propertiesButton.setText("get properties");
        propertiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                propertiesText = new JTextArea(400, 400);
                propertiesText.setEditable(false);
                propertiesPanel.add(propertiesText, BorderLayout.NORTH);
                String fileName = (String) list.getSelectedValue();
                client.sendServer("properties" + " " + fileName);
                propertiesText.setText(client.fromServer());
            }
        });

        optionPanel = new JPanel();
        display = new JTextArea(100, 100);
        display.setEditable(false);
        display.setFont(new Font("Arial", 14, 14));
        propertiesPanel.add(display);
        tab = new JTabbedPane();
        tab.addTab("Preview", previewPanel);
        tab.addTab("Properties", propertiesPanel);
        frame.add(tab, BorderLayout.CENTER);
        optionPanel.setLayout(new GridLayout(1, 4));
        download = new JButton();
        download.setText("Download");
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = (String) list.getSelectedValue();
                client.sendServer("Download" + " " + fileName);
                System.out.println("receiving ...");
                String s = client.fromServer();
                System.out.println(s);
                System.out.println("received");
                int size = Integer.parseInt(s);
                System.out.println("size = " + size);
                System.out.println(size);
                byte[] bytes = new byte[size];
                for (int k = 0; k < size; k++) {
                    bytes[k] = client.bytesfromServer();
                    System.out.println(bytes[k]);
                }
                try {
                    FileOutputStream fos = new FileOutputStream("D:\\ApProjects\\Final\\client\\" + fileName);
                    fos.write(bytes);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
//                String t = client.fromServer();
//                while(!(t.equals("end")))
//                t = t + client.fromServer();
            }
        });
//        duplicate = new JButton();
//        duplicate.setText("Duplicate");
//        duplicate.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFrame jFrame = new JFrame();
//                JTextArea newNameText = new JTextArea();
//                JLabel label = new JLabel("Enter a new path :");
//                newNameText.setEditable(true);
//                JButton OKButton = new JButton();
//                OKButton.setText("OK");
//                jFrame.setSize(new Dimension(200, 200));
//                jFrame.setLayout(new BorderLayout());
//                jFrame.add(label, BorderLayout.NORTH);
//                jFrame.add(newNameText, BorderLayout.CENTER);
//                jFrame.add(OKButton, BorderLayout.SOUTH);
//                jFrame.setVisible(true);
//                System.out.println("duplicate");
//                OKButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        String fileName = (String) list.getSelectedValue();
//                        newName = newNameText.getText();
//                        client.sendServer("Duplicate" + " " + fileName + " " + newName);
//                        System.out.println("ok-duplicate");
//                    }
//
//                });
//
//            }
//
//        });
        rename = new JButton();
        rename.setText("Rename");
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = new JFrame();
                JTextArea newNameText = new JTextArea();
                JLabel label = new JLabel("Enter a new name :");
                newNameText.setEditable(true);
                JButton OKButton = new JButton();
                OKButton.setText("OK");
                jFrame.setSize(new Dimension(200, 200));
                jFrame.setLayout(new BorderLayout());
                jFrame.add(label, BorderLayout.NORTH);
                jFrame.add(newNameText, BorderLayout.CENTER);
                jFrame.add(OKButton, BorderLayout.SOUTH);
                jFrame.setVisible(true);
                System.out.println("rename");
                OKButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String fileName = (String) list.getSelectedValue();
                        newName = newNameText.getText();
                        client.sendServer("Rename" + " " + fileName + " " + newName);
                        System.out.println("ok-rename");

                    }

                });

            }
        });
        delete = new JButton();
        delete.setText("Delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = (String) list.getSelectedValue();
                client.sendServer("Delete" + " " + fileName);
                System.out.println("delete");

            }
        });
        optionPanel.add(download);
        //optionPanel.add(duplicate);
        optionPanel.add(rename);
        optionPanel.add(delete);
        frame.add(optionPanel, BorderLayout.SOUTH);


    }

}
