package XML.Send_XML;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLtoDB implements ActionListener{
  Connection conn;
  PreparedStatement sendXML;
  String sql = "";
  public XMLtoDB() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-PNPO69N\\SQLEXPRESS;databaseName=managementProject;user=sa;password=12345");
      String path = "XML\\Send_XML\\createdXML.xml";
      File file = new File(path);
      JFrame frame = new JFrame();
      
      JLabel lb = new JLabel(file.toString());

      JButton button = new JButton("Send");
      button.addActionListener(this);
      frame.add(lb);
      frame.add(button);
      frame.setLayout(new FlowLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(500, 500);
      frame.setVisible(true);

    } catch (Exception e) {
      //TODO: handle exception
      System.out.println(e.getMessage());
    }
  }

  public void sendXMLtoDB() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      Document doc = documentBuilder.parse(new File("XML\\Send_XML\\createdXML.xml"));
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
          sendXML = conn.prepareStatement(sql);
          sendXML.setString(1, name);
          sendXML.setString(2, add);
          sendXML.setString(3, salary);
          sendXML.setString(4, job);
          sendXML.setString(5, gender);
          sendXML.setString(6, birthday);
          sendXML.executeUpdate();
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getMessage());
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if(e.getActionCommand().equals("Send"))
    {
      // System.out.println("CCC");
      sendXMLtoDB();
    }
  }

  public static void main(String[] args) {
    new XMLtoDB();
  }


}
