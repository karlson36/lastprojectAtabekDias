package kz.springboot.hometask7_ee.entities;

public class BasketItem {
    Items item;
    int amount;
    Long id;

    public BasketItem(){

    }

    public BasketItem(Items item, int amount, Long id) {
        this.item = item;
        this.amount = amount;
        this.id = id;
    }

    public BasketItem(Items item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
