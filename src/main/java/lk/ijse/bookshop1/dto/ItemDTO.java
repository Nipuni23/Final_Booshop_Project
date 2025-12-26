package lk.ijse.bookshop1.dto;

public class ItemDTO {
    private Integer item_id;
    private String item_name;
    private String category;
    private double unit_price;
    private int quantity_in_stock;

    public ItemDTO() {
    }

    public ItemDTO(Integer item_id, String item_name, String category, double unit_price, int quantity_in_stock) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.category = category;
        this.unit_price = unit_price;
        this.quantity_in_stock = quantity_in_stock;
    }

    public ItemDTO(String item_name, String category, double unit_price, int quantity_in_stock) {
        this.item_name = item_name;
        this.category = category;
        this.unit_price = unit_price;
        this.quantity_in_stock = quantity_in_stock;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity_in_stock() {
        return quantity_in_stock;
    }

    public void setQuantity_in_stock(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    @Override
    public String toString() {
        return "ItemDTO{" + "item_id=" + item_id + ", item_name=" + item_name + ", category=" + category + ", unit_price=" + unit_price + ", quantity_in_stock=" + quantity_in_stock + '}';
    }
}