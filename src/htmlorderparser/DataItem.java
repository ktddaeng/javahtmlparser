/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlorderparser;

/**
 *
 * @author gadau
 */
public class DataItem {
    private String desc;
    private String itemno;
    private String price;   //unit price
    private String packing; //items per pack
    private String quantity;

    public DataItem() {
        desc = "";
        itemno = "";
        price = "";
        quantity = "";
        packing = "";
    }

    public DataItem(String desc, String itemno, String price, String quantity, String packing) {
        this.desc = desc;
        this.itemno = itemno;
        this.price = price;
        this.packing = packing;
        this.quantity = quantity;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemno() {
        return itemno;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }
    
    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
