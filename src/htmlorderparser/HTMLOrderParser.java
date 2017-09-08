/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlorderparser;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author gadau
 */
public class HTMLOrderParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Launch the GUI
        FileChooser fileChooser = new FileChooser();

        //parse the DOM tree using JSoup
        
        //load table to display on GUI
        
        //put collected data into a CSV file
    }
    
}
