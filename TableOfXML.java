package XML.Send_XML;

import java.io.File;

import javax.swing.*;
public class TableOfXML extends JFrame{

  JTable table;
  JScrollPane scrollPane;

  Model model;

  public TableOfXML(String file) {
    model = new Model(file);
    table = new JTable();
    table.setModel(model);
    scrollPane = new JScrollPane(table);
    JPanel panel = new JPanel();
    panel.add(scrollPane);
    this.add(panel, "Center");
    this.pack();
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

}
