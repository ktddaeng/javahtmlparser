/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlorderparser;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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
        List<String> listOfSubtotals = new ArrayList<String>();
        
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
                    listOfQty.add(list3.get(3).val());
                }
                    //make sure to account for special elements
                listOfPrices = document.getElementsByClass("cartTotal").eachText();
                break;
            case 1: //JC
                list1 = document.getElementsByClass("cart-table__product-name");
                list2 = document.getElementsByClass("cart-table__column--product");
                list3 = document.getElementsByClass("cart-item-quantity");
                list4 = document.getElementsByClass("cart-table__price");
                list5 = document.getElementsByClass("cart-table__column--total");
                for (int j = 1; j < list1.size(); j++){
                    listOfNames.add(list1.get(j - 1).text());
                    Elements list0 = list2.get(j).getElementsByTag("span");
                    listOfSKU.add(list0.get(0).text());
                    listOfQty.add(list3.get(j - 1).val());
                    list0 = list4.get(j - 1).getElementsByClass("unit-price");
                    listOfPacking.add(list0.get(1).text());
                    listOfPrices.add(list5.get(j).text());
                }
                break;
            case 2: //4Seasons
                listOfNames = document.getElementsByClass("shoppingcart_item_name").eachText();
                System.out.println(listOfNames.size());
                listOfSKU = document.getElementsByClass("shoppingcart_item_sku").eachText();
                listOfSubtotals = document.getElementsByClass("cartTotal").eachText();
                list1 = document.getElementsByClass("cartQty");
                list2 = document.getElementsByClass("cartPrice");
                System.out.println(listOfSKU.size());
                System.out.println(list1.size());
                
                for (int j = 0; j < list1.size(); j++) {
                    //get quantity input value (4th in list of inputs)
                    Elements list0 = list1.get(j).getElementsByTag("input");
                    listOfQty.add(list0.get(3).val());
                    //pack size (first in every 4 divs)
                    listOfPacking.add(list2.get(j * 4).text());
                    //price (fourth in every 4 divs)
                    listOfPrices.add(list2.get(j * 4 + 3).text());
                }
            default:                
                break;
        }
        
        //check if null, meaning the document is wrongly attributed
        if (listOfNames.isEmpty() || listOfSKU.isEmpty() || listOfQty.isEmpty()
                ||listOfPacking.isEmpty() || listOfPrices.isEmpty()) {
            textArea.setText(textArea.getText() + "\n" + "ERROR: Names: " + listOfNames.isEmpty());
            textArea.setText(textArea.getText() + "\n" + "ERROR: SKU: " + listOfSKU.isEmpty());
            textArea.setText(textArea.getText() + "\n" + "ERROR: Qty: " + listOfQty.isEmpty());
            textArea.setText(textArea.getText() + "\n" + "ERROR: Pack: " + listOfPacking.isEmpty());
            textArea.setText(textArea.getText() + "\n" + "ERROR: Prices: " + listOfPrices.isEmpty());
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
                case 2:
                    di.setItemno(listOfSKU.get(i));
                    String s = listOfNames.get(i);
                    di.setDesc(s.replace(",", " "));
                    di.setPrice(listOfPrices.get(i).substring(1));
                    di.setQuantity(listOfQty.get(i));
                    di.setPacking(listOfPacking.get(i));
                    di.setSubtotal(listOfSubtotals.get(i).substring(1));
            }
            textArea.setText(textArea.getText() + "\n" + (i + 1) + ": " + di.getItemno() + ", "
                    + di.getDesc() + ", " + di.getQuantity() + ", "
                    + di.getPacking() + ", " + di.getPrice()
                    + ", " + di.getSubtotal());
            listOfData.add(di);
        }
        
        return listOfData;
    }

}
