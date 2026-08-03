public class MachineItem {
    private final String name;
    private double price;
    private int stock;
    private double calories;
    private boolean sellableIndiv;

    private final int maxStock = 50;

//    public MachineItem(String name) {
//        this.name = name;
//    }

    public MachineItem(String name, double price, int stock, double calories, boolean sellableIndiv) {
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.sellableIndiv = sellableIndiv;

        if (stock > maxStock)
        {
            System.out.println("You cannot put more than 50 stock.");
            System.out.println("Setting stock to 50...");
            this.stock = 50;
        }
        else
        {
            this.stock = stock;
        }
    }

    public MachineItem(String name, double price, int stock, double calories){
        this(name ,price,stock,calories,true);
    }

    // ================== region getters ===========
    public String getName()
    {
        return name;
    }
    public double getPrice()
    {
        return price;
    }
    public int getStock()
    {
        return stock;
    }
    public double getCalories()
    {
        return calories;
    }
    public boolean isSellableIndiv()
    {
        return sellableIndiv;
    }
    // ================= endregion ===============

    // ================= region setters==============
    public void setPrice(double price)
    {
        this.price = price;
    }
    public void setCalories(double calories)
    {
        this.calories = calories;
    }
    public void setSellableIndiv(boolean sellableIndiv)
    {
        this.sellableIndiv = sellableIndiv;
    }
    // ============== endregion ================

    public void transact()
    {
        stock -= 1;
    }
    public boolean restock(int quantity)
    {
        if (this.stock == maxStock)
        {
            System.out.println("You cannot put more than 50 stock.");
            return false;
        }
        else if (this.stock + quantity > maxStock)
        {
            System.out.println("Item restock quantity exceeds maximum stock: "
                    + this.stock
                    + " + "
                    + quantity
                    + " > "
                    + this.maxStock);
            return false;
        }
        else if (quantity < 0)
        {
            System.out.println("Invalid quantity: " + quantity);
            return false;
        }

        System.out.println("Restocking " + quantity + " to " + getName());
        this.stock += quantity;
        return true;
    }
}
