/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlorderparser;

import java.io.File;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *
 * @author gadau
 */
public class FileChooser implements ActionListener{
    private JFrame frame;
    private JButton b;
    private JButton b2;
    private JButton b3;
    private JTextArea textArea;
    private JComboBox<String> vendorSelect;
    private File path;
    private String[] vendors = new String[] {"Bargin Wholesale", "JC Sales"};
    private ArrayList<DataItem> listOfData;
    
    final static boolean RIGHT_TO_LEFT = false;
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    
    public FileChooser(){        
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){    
                setUpGUI();
            }
        });
    }    
    
    private void setUpGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);

        frame = new JFrame("Order Inventory Parser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        
        if (RIGHT_TO_LEFT){
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill){
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        
        vendorSelect = new JComboBox<>(vendors);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(vendorSelect, c);
                
        b = new JButton("Select File");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        b.addActionListener(this);
        pane.add(b, c);
        
        b2 = new JButton("Load Data");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        b2.addActionListener(this);
        pane.add(b2, c);
        
        textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.setText("Please Choose a File First!");        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 65;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
        pane.add(scroll, c);
        
        b3 = new JButton("Export Data");
        c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 0;       //reset to default
	c.weighty = 1.0;   //request any extra vertical space
	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
	c.insets = new Insets(10,0,0,0);  //top padding
	c.gridx = 1;       //aligned with button 2
	c.gridwidth = 2;   //2 columns wide
	c.gridy = 2;       //third row
        b3.addActionListener(this);
        pane.add(b3, c);
        
        frame.pack();
        frame.setSize(700, 200);
        frame.setVisible(true);       
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b){    //select file
            path = selectFileWindow(false);
            if (path != null){
                textArea.setText(textArea.getText() + "\n" + path.toString());
            } else {
                textArea.setText(textArea.getText() + "\n" + "ERROR: File Selection Failed.");
            }
        } else if (e.getSource() == b2){    //extract file
            //get extract
            if (path == null){
                textArea.setText(textArea.getText() + "\n" + "ERROR: No file has been selected yet!");
                return;
            }
            
            textArea.setText(textArea.getText() + "\n" + "Commencing extract...");
            int option = 0;
            for (int i = 0; i < vendors.length; i++){
                if (vendors[i].equals(vendorSelect.getSelectedItem())){
                    option = i;
                }
            }
            HTMLParser hp = new HTMLParser(path, vendorSelect.getSelectedIndex(), textArea);
            listOfData = hp.parseDoc();
            if (listOfData == null || listOfData.isEmpty()){
                textArea.setText(textArea.getText() + "\n" + "ERROR: Extraction Failed.");
            } else {
                textArea.setText(textArea.getText() + "\n" + "Extraction Successful!");
            }
        } else if (e.getSource() == b3){    //export file
            String s = textArea.getText();
            if (listOfData == null || listOfData.isEmpty()){
                textArea.setText(s + "\n" + "ERROR: List is empty!");
                return;
            }
            exportList();
        }
    }
    
    private File selectFileWindow(boolean getDirectory){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        if (getDirectory) {
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        //int returnValue = jfc.showOpenDialog(null);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile();
        } else {
            return null;
        }
    }
    
    private void exportList(){
        File path2 = selectFileWindow(true);
        if (path2 == null){
            textArea.setText(textArea.getText() + "\n" + "ERROR: Invalid File Path!");
        }
        textArea.setText(textArea.getText() + "\n" + "Exporting to " + path2.getPath());
        System.out.println(path2.getPath());
        String filename = "Order " + vendorSelect.getSelectedItem() + ".csv";

        try {
            File saveFile = new File(path2, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            int rowCount = listOfData.size();
            if (rowCount > 0) {
                bw.write("SKU, Description, Quantity, Packing Price, Price");
                bw.newLine();
                for (int i = 0; i < rowCount; i++) {
                    DataItem di = listOfData.get(i);
                    bw.write("'" + di.getItemno() + ", " + di.getDesc() + ", " +
                            di.getQuantity() + ", " + di.getPacking() + ", " +
                            di.getPrice());
                    bw.newLine();
                }
                bw.flush();
            }
            fw.close();
            textArea.setText(textArea.getText() + "\n" + "Export Successful!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        textArea.setText(textArea.getText() + "\n" + "ERROR: Export Failed!");
    }
}
