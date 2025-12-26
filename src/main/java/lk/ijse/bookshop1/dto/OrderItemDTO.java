package lk.ijse.bookshop1.dto;

public class OrderItemDTO {
    private Integer order_id;
    private Integer item_id;
    private String item_name;  // For display purposes
    private int qty;
    private double unit_price;
    private double total;  // qty * unit_price

    public OrderItemDTO() {
    }

    public OrderItemDTO(Integer order_id, Integer item_id, int qty, double unit_price) {
        this.order_id = order_id;
        this.item_id = item_id;
        this.qty = qty;
        this.unit_price = unit_price;
        this.total = qty * unit_price;
    }

    public OrderItemDTO(Integer item_id, String item_name, int qty, double unit_price) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.qty = qty;
        this.unit_price = unit_price;
        this.total = qty * unit_price;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
        this.total = qty * unit_price;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
        this.total = qty * unit_price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" + "order_id=" + order_id + ", item_id=" + item_id + ", item_name=" + item_name + ", qty=" + qty + ", unit_price=" + unit_price + ", total=" + total + '}';
    }
}