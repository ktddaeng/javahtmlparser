/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlorderparser;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JTextArea;

/**
 *
 * @author gadau
 */
public class HTMLParser {
    private File filepath;
    private int option;
    private Document document;
    private JTextArea textArea;
    
    public HTMLParser(File filepath, int option, JTextArea textArea) {
        this.filepath = filepath;
        this.option = option;
        this.textArea = textArea;
        try {
            document = Jsoup.parse(filepath, "UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
   
    //parse it here depending on the choice of the vendor
    public ArrayList<DataItem> parseDoc(){    
        ArrayList<DataItem> listOfData = new ArrayList<DataItem>();
        
        List<String> listOfSKU = new ArrayList<String>();
        List<String> listOfNames = new ArrayList<String>();
        List<String> listOfQty = new ArrayList<String>();
        List<String> listOfPrices = new ArrayList<String>();
        List<String> listOfPacking = new ArrayList<String>();
        
        /*
         * shoppingcart_item_name
         * shoppingcart_item_sku
         * cartQty (input inside div)
         * cartPrice (parse $15.60 / CA)
         *
         * cart-item-no (span inside div)
         * cart-item-description (a inside div)
         * cart-item-price NumberCell (span inside div)
         * cart-item-quantity-input (input inside div)
         */
        Elements list1, list2, list3, list4, list5;
        
        switch (option) {
            case 0: //Wholesale
                listOfNames = document.getElementsByClass("shoppingcart_item_name").eachText();
                listOfSKU = document.getElementsByClass("shoppingcart_item_sku").eachText();
                listOfPacking = document.getElementsByClass("cartPrice").eachText();
                list2 = document.getElementsByClass("cartQty");
                for(int j = 0; j < list2.size(); j++){
                    list3 = list2.get(j).getElementsByTag("input");
                    listOfQty.add(list3.get(2).val());
                }
                    //make sure to account for special elements
                listOfPrices = document.getElementsByClass("cartTotal").eachText();
;               break;
            case 1:
            default:
                list1 = document.getElementsByClass("cart-item-description");
                list2 = document.getElementsByClass("cart-item-no");
                list3 = document.getElementsByClass("cart-item-quantity-input");
                list4 = document.getElementsByClass("cart-item-price");
                list5 = document.getElementsByClass("cart-item-subtotal");
                for (int j = 1; j < list1.size(); j++){
                    Elements list0 = list1.get(j).getElementsByTag("a");
                    listOfNames.add(list1.get(j).text());
                    list0 = list2.get(j).getElementsByTag("span");
                    listOfSKU.add(list2.get(j).text());
                    list0 = list3.get(j).getElementsByTag("input");
                    listOfQty.add(list0.get(0).val());
                    list0 = list4.get(j).getElementsByTag("span");
                    listOfPacking.add(list4.get(j).text());
                    list0 = list5.get(j).getElementsByTag("span");
                    listOfPrices.add(list5.get(j).text());
                }
                break; //JC
        }
        
        //check if null, meaning the document is wrongly attributed
        if (listOfNames.isEmpty() || listOfSKU.isEmpty() || listOfQty.isEmpty()
                ||listOfPacking.isEmpty() || listOfPrices.isEmpty()) {
            textArea.setText(textArea.getText() + "\n" + "ERROR: Source file not valid!");
            return null;
        }
        
        for (int i = 0; i < listOfNames.size(); i++){
            DataItem di = new DataItem();
            switch (option) {
                case 0:
                    di.setItemno(listOfSKU.get(i));
                    di.setDesc(listOfNames.get(i));
                    di.setPacking(listOfPacking.get(i));
                    di.setQuantity(listOfQty.get(i));
                    di.setPrice(listOfPrices.get(i).substring(1));
                    break;
                case 1:
                    di.setItemno(listOfSKU.get(i));
                    di.setDesc(listOfNames.get(i));
                    di.setPrice(listOfPrices.get(i).substring(1));
                    di.setQuantity(listOfQty.get(i));
                    di.setPacking(listOfPacking.get(i));
                    break;
            }
            textArea.setText(textArea.getText() + "\n" + di.getItemno() + ", " + di.getDesc() + ", " + di.getQuantity() + ", " + di.getPacking() + ", " + di.getPrice());
            listOfData.add(di);
        }
        
        return listOfData;
    }

}
