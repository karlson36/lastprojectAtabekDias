package kz.springboot.hometask7_ee.db;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {
    private static ArrayList<ShopItem> items = new ArrayList<>();

    static {
        items.add(new ShopItem(1L,"Iphone 12","ponty",10,100000,5,"https://www.ixbt.com/img/n1/news/2020/9/4/12_large_0_large_large_large_large.png"));
        items.add(new ShopItem(1L,"Iphone 12 Pro Max","ponty",10,100000,4,"https://www.ixbt.com/img/n1/news/2020/5/6/6BS4d4w3x41V_large_large_large.jpg"));
        items.add(new ShopItem(1L,"Iphone 12 mini","ponty",10,100000,4,"https://liter.kz/wp-content/uploads/2020/09/ajfon-mini-750x423.jpg"));
        items.add(new ShopItem(1L,"Iphone 11","ponty",10,100000,2,"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRdfWaHezSpU--9uvvXTIscAjtb5-JMMXzfFQ&usqp=CAU"));
    }

    private static Long id = 7L;
    public static ArrayList<ShopItem> getShopItems(){
        return items;
    }

    public static void addItem(ShopItem shopItem) {
        shopItem.setId(id++);
        items.add(shopItem);
    }

    public static ShopItem getItem(Long id) {
        for (ShopItem item:items) {
            if (item.getId().equals(id))
                return item;
        }
        return null;
    }

    public static ShopItem deleteItem(Long id) {
        for (ShopItem item:items) {
            if (item.getId().equals(id)) {
                items.remove(item);
                break;
            }
        }
        return null;
    }
}
