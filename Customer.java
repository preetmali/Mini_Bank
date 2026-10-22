public class Customer {

    private String name;
    private Address address;

    public Customer(String name, Address address) {
        this.name = Validator.requireText(name, "Customer name");
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}