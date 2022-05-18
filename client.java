package XML.Send_XML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

@SuppressWarnings("unchecked")
public class client extends JFrame implements ActionListener, MouseListener {
  final int port = 1234;
  Connection conn;
  PreparedStatement gStatement;
  String sql = "";
  ResultSet rs;
  ResultSetMetaData rsm;
  Vector vData = new Vector<>();
  Vector vTitle = new Vector<>();
  JScrollPane tableResult;
  DefaultTableModel model;
  JTable tb = new JTable();
  int selectedRow = 0;
  String fileName;
  JPanel p;
  JButton createFile, sendXML, chooseFile, loadFiletoTB;
  File fileXMLSent;
  final File[] fileToSend = new File[1];
  InputStreamReader in;
  OutputStreamWriter out;
  BufferedWriter bufferedWriter;
  BufferedReader bufferedReader;

  public client() {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      conn = DriverManager.getConnection(
          "jdbc:sqlserver://DESKTOP-PNPO69N\\SQLEXPRESS;databaseName=managementProject;user=sa;password=12345");

      p = new JPanel();
      createFile = new JButton("Choose row");
      createFile.addActionListener(this);
      createFile.setFocusable(false);

      sendXML = new JButton("Send File");
      sendXML.addActionListener(this);
      sendXML.setFocusable(false);

      chooseFile = new JButton("Choose file");
      chooseFile.addActionListener(this);
      chooseFile.setFocusable(false);

      loadFiletoTB = new JButton("Get & load XML");
      loadFiletoTB.addActionListener(this);
      loadFiletoTB.setFocusable(false);

      p.add(createFile);
      p.add(sendXML);
      p.add(chooseFile);
      p.add(loadFiletoTB);

      loadData();
      model = new DefaultTableModel(vData, vTitle);
      tb = new JTable(model);
      tb.addMouseListener(this);
      tableResult = new JScrollPane(tb);

      this.setTitle("Employee");
      this.setSize(500, 500);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocation(400, 100);
      this.setVisible(true);
      // add component
      this.getContentPane().add(tableResult, "North");
      this.add(p, "South");

      

    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getMessage());
    }
  }

  // get extension
  public String getFileExtension(String fileName) {

    int i = fileName.lastIndexOf('.');

    if (i > 0) {
      return fileName.substring(i + 1);
    } else {
      return "Nope";
    }

  }

  // load data
  public void loadData() {
    try {
      vData.clear();
      vTitle.clear();
      String sql = "select * from employees";
      gStatement = conn.prepareStatement(sql);
      ResultSet rst = gStatement.executeQuery();
      ResultSetMetaData rsm = rst.getMetaData();
      int num_column = rsm.getColumnCount();
      for (int i = 1; i <= num_column; i++) {
        vTitle.add(rsm.getColumnLabel(i));
      }
      while (rst.next()) {
        Vector row = new Vector();
        for (int i = 1; i <= num_column; i++) {
          row.add(rst.getString(i));
        }
        vData.add(row);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if (e.getActionCommand().equals("Choose row")) {
      Vector st = (Vector) vData.elementAt(selectedRow);
      new CreateForm("Create file xml", this, (String) st.elementAt(0),
          (String) st.elementAt(1),
          (String) st.elementAt(2),
          (String) st.elementAt(3),
          (String) st.elementAt(4),
          (String) st.elementAt(5),
          (String) st.elementAt(6));
    }
    if (e.getActionCommand().equals("Send File")) {
      sendFiletoServer();
    }
    if (e.getActionCommand().equals("Choose file")) {
      chooseFile();
    }
    if (e.getActionCommand().equals("Get & load XML")) {
      loadXMltoTable();
    }
  }

  public void loadXMltoTable() {
    try {
      announceR();
      // System.out.println(fileName);
      new TableOfXML(fileName);
    } catch (Exception e) {
      //TODO: handle exception
      e.printStackTrace();
    }
  }

  public void announceR() {
    try {
      Socket s = new Socket("localhost", port);

      DataInputStream dataInputStream = new DataInputStream(s.getInputStream());

      int fileNameLength = dataInputStream.readInt();

      if(fileNameLength > 0)
      {
        byte[] fileNameBytes = new byte[fileNameLength];

        dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);

        fileName = new String(fileNameBytes);

        int fileContentLength = dataInputStream.readInt();


        if(fileContentLength > 0)
        {
          byte[] fileContentBytes = new byte[fileContentLength];
          dataInputStream.readFully(fileContentBytes, 0, fileContentLength);
          if(getFileExtension(fileName).equalsIgnoreCase("txt"))
          {
            System.out.println("TXT");
          }
          else {System.out.println("Another one");}
        }
      }

    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  public void sendFiletoServer() {
    try {
      Socket socket = new Socket("localhost", port);
      
      FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
      DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
      String fileName = fileToSend[0].getAbsolutePath();
      byte[] fileNameBytes = fileName.getBytes();
      byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];

      fileInputStream.read(fileContentBytes);

      dataOutputStream.writeInt(fileNameBytes.length);
      dataOutputStream.write(fileNameBytes);
      
      dataOutputStream.writeInt(fileContentBytes.length);
      dataOutputStream.write(fileContentBytes);
      

    } catch (Exception ev) {
      // TODO: handle exception
      ev.printStackTrace();
    }

  }

  public void chooseFile() {
    JFileChooser fileChooser = new JFileChooser();

    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      fileToSend[0] = fileChooser.getSelectedFile();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
    selectedRow = tb.getSelectedRow();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    new client();
  }
}
