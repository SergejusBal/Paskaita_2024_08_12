package parduotuve.Paskaita_2024_08_12.Models;

public class Order {
    private int id;
    private String products;
    private double totalPrices;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String paymentStatus;
    public Order() {
    }

    public int getId() {
        return id;
    }

    public String getProducts() {
        return products;
    }

    public double getTotalPrices() {
        return totalPrices;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProducts(String product) {
        this.products = product;
    }

    public void setTotalPrices(double totalPrices) {
        this.totalPrices = totalPrices;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
