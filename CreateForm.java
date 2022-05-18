
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.awt.*;

public class CreateForm extends JFrame implements ActionListener{

  client mana;
  JLabel idLabel; // id
  JTextField idField;
  JLabel namelb; // name
  JTextField nametext;
  JLabel addresslb; // address
  JTextField addtext;
  JLabel salalb; // salary
  JTextField salatext;
  JLabel joblb; // job
  JTextField jobtext;
  JLabel genlb; // gender
  JTextField gentext;
  JLabel birthlb; // birthdate
  JTextField birthtext;
  JButton createXML, cancel;

  // XML
  Element root;
  Document doc;


  public CreateForm(String s, client manage, String id, String na, String add, String sala, String sub, String gen, String birth) {
    super(s);
    mana = manage;
    Container cont = this.getContentPane();
    cont.setLayout(new GridLayout(8, 2));

    idLabel = new JLabel("ID");
    idField = new JTextField(id);
    cont.add(idLabel);
    cont.add(idField);

    namelb = new JLabel("Name");
    nametext = new JTextField(na);
    cont.add(namelb);
    cont.add(nametext);

    addresslb = new JLabel("Address");
    addtext = new JTextField(add);
    cont.add(addresslb);
    cont.add(addtext);

    salalb = new JLabel("Salary");
    salatext = new JTextField(sala);
    cont.add(salalb);
    cont.add(salatext);

    joblb = new JLabel("Job");
    jobtext = new JTextField(sub);
    cont.add(joblb);
    cont.add(jobtext);

    genlb = new JLabel("Gender");
    gentext = new JTextField(gen);
    cont.add(genlb);
    cont.add(gentext);

    birthlb = new JLabel("Birhtday");
    birthtext = new JTextField(birth);
    cont.add(birthlb);
    cont.add(birthtext);

    createXML = new JButton("Create XML file");
    createXML.addActionListener(this);
    cont.add(createXML);

    cancel = new JButton("Cancel");
    cancel.addActionListener(this);
    cont.add(cancel);

    this.setSize(350, 350);
    this.setVisible(true);
  }

  public void createXMLfile(String id, String name, String address, String salary, String subject, String gender, String birthday, Staff st) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      doc = documentBuilder.newDocument();
      root = doc.createElement("company");
      addToXML(doc, root, st);
      doc.appendChild(root);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(doc);
      StreamResult streamResult = new StreamResult(new File("XML\\Send_XML\\createdXML.xml"));
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
      transformer.transform(domSource, streamResult);
    } catch (Exception e) {
      //TODO: handle exception
      System.out.println(e.getMessage());
    }
  }

  public void addToXML(Document doc, Element root, Staff em) {

    Element employee = doc.createElement("employee");
    employee.setAttribute("id", em.getId());

    Element nameTag = doc.createElement("name");
    nameTag.setTextContent(em.getName());
    employee.appendChild(nameTag);

    Element addTag = doc.createElement("address");
    addTag.setTextContent(em.getAddress());
    employee.appendChild(addTag);

    Element salaTag = doc.createElement("salary");
    salaTag.setTextContent(em.getSalary());
    employee.appendChild(salaTag);

    Element subTag = doc.createElement("subject");
    subTag.setTextContent(em.getSubject());
    employee.appendChild(subTag);

    Element genTag = doc.createElement("gender");
    genTag.setTextContent(em.getGender());
    employee.appendChild(genTag);

    Element birthTag = doc.createElement("birthday");
    birthTag.setTextContent(em.getBirthday());
    employee.appendChild(birthTag);
    
    root.appendChild(employee);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if(e.getActionCommand().equals("Create XML file")) {
      String id = idField.getText();
      String name = nametext.getText();
      String address = addtext.getText();
      String salary = salatext.getText();
      String subject = jobtext.getText();
      String gender = gentext.getText(); 
      String birthday = birthtext.getText();
      Staff st = new Staff(id, name, address, salary, subject, gender, birthday);
      createXMLfile(id, name, address, salary, subject, gender, birthday, st);
    }
    if(e.getActionCommand().equals("Cancel"))
    {
      this.dispose();
    }
  }

}
