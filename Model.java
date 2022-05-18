
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.*;


import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class Model extends AbstractTableModel {
	Vector data;
	Vector columns;

	public Model(String file) {
		try {
			JFrame frame = new JFrame();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			NodeList nl = doc.getElementsByTagName("name");
			NodeList n2 = doc.getElementsByTagName("address");
			NodeList n3 = doc.getElementsByTagName("salary");
			NodeList n4 = doc.getElementsByTagName("subject");
			NodeList n5 = doc.getElementsByTagName("gender");
			NodeList n6 = doc.getElementsByTagName("birthday");
			NodeList listOfPersons = doc.getElementsByTagName("employee");
			String data1 = "", data2 = "", data3 = "", data4 = "", data5 = "", data6 = "";
			data = new Vector();
			columns = new Vector();
			for (int i = 0; i < listOfPersons.getLength(); i++) {
				data1 = nl.item(i).getFirstChild().getNodeValue();
				data2 = n2.item(i).getFirstChild().getNodeValue();
				data3 = n3.item(i).getFirstChild().getNodeValue();
				data4 = n4.item(i).getTextContent();
				data5 = n5.item(i).getFirstChild().getNodeValue();
				data6 = n6.item(i).getFirstChild().getNodeValue();
				String line = data1 + " " + data2 + " " + data3 + " " + data4 + " " + data5 + " " + data6 + " ";
				StringTokenizer st2 = new StringTokenizer(line, " ");
				while (st2.hasMoreTokens())
					data.addElement(st2.nextToken());
			}
			columns.add("");
			columns.add("");
			columns.add("");
			columns.add("");
			columns.add("");
			columns.add("");

			
			


		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public int getRowCount() {
		return data.size() / getColumnCount();
	}

	public int getColumnCount() {
		return columns.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return (String) data.elementAt((rowIndex * getColumnCount())
				+ columnIndex);
	}

	public static void main(String[] args) {
		new Model("D:\\JAVA\\XML\\Send_XML\\InitXML.xml");
	}

}