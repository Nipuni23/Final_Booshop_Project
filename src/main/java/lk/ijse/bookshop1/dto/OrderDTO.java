package lk.ijse.bookshop1.dto;

import java.time.LocalDateTime;

public class OrderDTO {
    private Integer order_id;
    private LocalDateTime order_date;
    private double total_amount;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Integer order_id, LocalDateTime order_date, double total_amount, String status) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
    }

    public OrderDTO(LocalDateTime order_date, double total_amount, String status) {
        this.order_date = order_date;
        this.total_amount = total_amount;
        this.status = status;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" + "order_id=" + order_id + ", order_date=" + order_date + ", total_amount=" + total_amount + ", status=" + status + '}';
    }
}