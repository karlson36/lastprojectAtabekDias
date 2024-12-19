package kz.springboot.hometask7_ee.services;

import kz.springboot.hometask7_ee.entities.*;

import java.util.List;

public interface ItemService {
    Items addItem(Items item);
    List<Items> getAllItems();
    List<Items> getAllItemsInTopPage();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);
    List<Items> getSearchItems(String name,double priceFrom, double priceTo);
    List<Items> getSearchItemsByOrder(String name,double priceFrom, double priceTo, String order);

    List<Items> getSearchItemsByBrand(String name,double priceFrom, double priceTo, Brands brand);

    List<Items> getSearchItemsByOrderAndBrand(String name,double priceFrom, double priceTo, String order, Brands brand);

    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);
    void deleteBrand(Brands brand);

    List<Brands> getAllBrands();
    Brands addBrand(Brands brand);
    Brands saveBrand(Brands brand);
    Brands getBrand(Long id);
    void deleteCountry(Countries country);

    List<Categories> getAllCategories();
    Categories addCategory(Categories category);
    Categories saveCategory(Categories category);
    Categories getCategory(Long id);
    void deleteCategory(Categories category);

    List<Pictures> getAllPictures();

    List<Pictures> getPicturesByItemId(Long id);
    Pictures addPicture(Pictures picture);
    Pictures savePicture(Pictures picture);
    Pictures getPicture(Long id);
    void deletePicture(Pictures picture);

    List<SoldItems> getAllSoldItems();
    SoldItems addSoldItem(SoldItems soldItem);
}
