
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class server implements ActionListener {
  static ServerSocket server;
  static Socket socket;
  final int port = 1234;
  static Connection conn;
  static PreparedStatement sendXMLtoDB;
  static String sql = "";
  // static String pathFile = "";
  static String fileName;
  static File[] filetoSend = new File[1];
  JPanel panel;
  JLabel label;
  static ResultSet rs;
  static PreparedStatement getIn;

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    JFrame frame = new JFrame();

    frame.setSize(400, 400);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JButton convertToDB = new JButton("Convert to DB");
    convertToDB.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        sendXMLtoDB(fileName);
      }
    });

    JButton sendToClient = new JButton("Send");
    sendToClient.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        sendToClient();
      }
      
    });

    JButton chooseFile = new JButton("Choose");
    chooseFile.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        chooseFile();
      }
      
    });

    JButton InitXMl = new JButton("Init XML");
    InitXMl.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        initXML();
      }
      
    });

    JLabel label = new JLabel("Server Side");
    JPanel panel = new JPanel();
    label.setFont(new Font(null, Font.PLAIN, 25));
    label.setAlignmentX(Component.CENTER_ALIGNMENT);

    //
    panel.add(convertToDB);
    panel.add(sendToClient);
    panel.add(chooseFile);
    panel.add(InitXMl);

    frame.add(label);
    frame.add(panel, "South");
    frame.setVisible(true);



    server = new ServerSocket(1234);

    while (true) {
      try {
        socket = server.accept();

          DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

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

              JPanel fileRow = new JPanel();
              fileRow.setLayout(new BoxLayout(fileRow, BoxLayout.Y_AXIS));

              JLabel fileNamelb = new JLabel(fileName);
              fileNamelb.setBorder(new EmptyBorder(10, 0, 10, 0));

              if(getFileExtension(fileName).equalsIgnoreCase("txt"))
              {
                // fileRow.setName(String.valueOf(fieldId));

                // fileRow.add(fileNamelb);
                // panel.add(fileRow);
                // frame.validate();
                System.out.println("received txt file");
              }
              else
              {
                // fileRow.setName(String.valueOf(fieldId));
                // fileRow.add(fileNamelb);
                // panel.add(fileRow);

                // frame.validate();

                System.out.println(fileName);
                
              }
            }
          }
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
  }

  // Init XML
  public static void initXML() {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
      conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-PNPO69N\\SQLEXPRESS;databaseName=managementProject;user=sa;password=12345");

      sql = "select * from XMLandTable";
      getIn = conn.prepareStatement(sql);

      rs = getIn.executeQuery();
      // rsm = rs.getMetaData();
      // int col = rsm.getColumnCount();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();

      Element root = document.createElement("company");
      document.appendChild(root);

      /// element of root
      
      while(rs.next())
      {
        Element employee = document.createElement("employee");
        // name
        String name = rs.getString(1);
        Element nameTag = document.createElement("name");
        nameTag.setTextContent(name);
        employee.appendChild(nameTag);
        

        // address
        String address = rs.getString(2);
        Element addTag = document.createElement("address");
        addTag.setTextContent(address);
        employee.appendChild(addTag);

        // // salary
        String salary = rs.getString(3);
        Element salaTag = document.createElement("salary");
        salaTag.setTextContent(salary);
        employee.appendChild(salaTag);

        // // job
        String subject = rs.getString(4);
        Element subTag = document.createElement("subject");
        subTag.setTextContent(subject);
        employee.appendChild(subTag);

        // // gender
        String gender = rs.getString(5);
        Element genTag = document.createElement("gender");
        genTag.setTextContent(gender);
        employee.appendChild(genTag);

        // // birthday
        String birth = rs.getString(6);
        Element birthTag = document.createElement("birthday");
        birthTag.setTextContent(birth);
        employee.appendChild(birthTag);

        root.appendChild(employee);
        
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filetoSend[0].toString()));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.transform(domSource, streamResult);
        
      }
      
      System.out.println("Created XML FILE FROM DATAVASE SUCCESSFULLY");
    } catch (Exception e) {
      //TODO: handle exception
    }
  }

   // get extension
  public static String getFileExtension(String fileName) {

    int i = fileName.lastIndexOf('.');

    if(i > 0) {
      return fileName.substring(i + 1);
    }
    else {
      return "Nope";
    }

  }

  // send file to client
  public static void sendToClient() {
    try {
      FileInputStream fileInputStream = new FileInputStream(filetoSend[0].getAbsolutePath());
      
      DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
      String fileName = filetoSend[0].toString();
      byte[] fileNameBytes = fileName.getBytes();
      byte[] fileContentBytes = new byte[(int)filetoSend[0].length()];

      fileInputStream.read(fileContentBytes);

      dataOutputStream.writeInt(fileNameBytes.length);
      dataOutputStream.write(fileNameBytes);

      dataOutputStream.writeInt(fileContentBytes.length);
      dataOutputStream.write(fileContentBytes);
    } catch (Exception e) {
      //TODO: handle exception
      e.printStackTrace();
    }
  }

  // choosing file func
  public static void chooseFile() {
    JFileChooser chooser = new JFileChooser();

    if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
    {
      filetoSend[0] = chooser.getSelectedFile();
      System.out.println(filetoSend[0].getAbsolutePath());
    }
  }

  // send XML to DB
  public static void sendXMLtoDB(String fileN) {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
      conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-PNPO69N\\SQLEXPRESS;databaseName=managementProject;user=sa;password=12345");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      Document doc = documentBuilder.parse(new File(fileN));
      // Document doc = documentBuilder.parse(new File("D:\\JAVA\\XML\\Send_XML\\createdXML.xml"));
      doc.getDocumentElement().normalize();
      // rooot
      System.out.println(doc.getDocumentElement().getNodeName());

      NodeList listOfperson = doc.getElementsByTagName("employee");
      for (int i = 0; i < listOfperson.getLength(); i++) {
        Node person = listOfperson.item(i);

        if (person.getNodeType() == Node.ELEMENT_NODE) {
          Element personEle = (Element) person;

          NodeList nameList = personEle.getElementsByTagName("name");
          Element nameEle = (Element) nameList.item(i);
          NodeList textName = nameEle.getChildNodes();
          String name = ((Node) textName.item(0)).getNodeValue().trim();

          NodeList addList = personEle.getElementsByTagName("address");
          Element addEle = (Element) addList.item(i);
          NodeList textadd = addEle.getChildNodes();
          String add = ((Node) textadd.item(0)).getNodeValue().trim();

          NodeList salaList = personEle.getElementsByTagName("salary");
          Element salaEle = (Element) salaList.item(i);
          NodeList textSala = salaEle.getChildNodes();
          String salary = ((Node) textSala.item(0)).getNodeValue().trim();

          NodeList jobList = personEle.getElementsByTagName("subject");
          Element jobEle = (Element) jobList.item(i);
          NodeList textJob = jobEle.getChildNodes();
          String job = ((Node) textJob.item(0)).getNodeValue().trim();

          NodeList genList = personEle.getElementsByTagName("gender");
          Element genEle = (Element) genList.item(i);
          NodeList textGem = genEle.getChildNodes();
          String gender = ((Node) textGem.item(0)).getNodeValue().trim();

          NodeList birList = personEle.getElementsByTagName("birthday");
          Element birEle = (Element) birList.item(i);
          NodeList textBir = birEle.getChildNodes();
          String birthday = ((Node) textBir.item(0)).getNodeValue().trim();
          
          // sendXML
          sql = "insert into XMLandTable values(?,?,?,?,?,?)";
          sendXMLtoDB = conn.prepareStatement(sql);
          sendXMLtoDB.setString(1, name);
          sendXMLtoDB.setString(2, add);
          sendXMLtoDB.setString(3, salary);
          sendXMLtoDB.setString(4, job);
          sendXMLtoDB.setString(5, gender);
          sendXMLtoDB.setString(6, birthday);
          sendXMLtoDB.executeUpdate();
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getMessage());
    }

  }

}
