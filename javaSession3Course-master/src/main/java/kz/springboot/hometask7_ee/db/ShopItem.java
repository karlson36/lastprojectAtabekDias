package kz.springboot.hometask7_ee.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {
    private Long id;
    private String name;
    private String description;
    private int amount;
    private int price;
    private int stars;
    private String pictureUrl;


    public int getFullStars(){
        return (int)(stars);
    }

    public int getEmptyStars(){
        return 5 - getFullStars();
    }
}
