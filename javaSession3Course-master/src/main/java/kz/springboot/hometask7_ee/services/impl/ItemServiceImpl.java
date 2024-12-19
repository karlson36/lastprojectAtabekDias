package kz.springboot.hometask7_ee.services.impl;

import kz.springboot.hometask7_ee.entities.*;
import kz.springboot.hometask7_ee.repositories.*;
import kz.springboot.hometask7_ee.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired SolditemRepository solditemRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Items> getAllItemsInTopPage() {
        List<Items> items = itemRepository.findAllByInTopPageIsTrue();
        items.addAll(itemRepository.findAllByInTopPageIsFalse());
        return items;
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Items> getSearchItems(String name,double priceFrom, double priceTo) {
        return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqual( name , priceFrom, priceTo);
    }

    @Override
    public List<Items> getSearchItemsByOrder(String name, double priceFrom, double priceTo, String order) {
        if(order.equals("ascending")) {
            return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(name , priceFrom, priceTo);
        }else {
            return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(name , priceFrom, priceTo);
        }
    }

    @Override
    public List<Items> getSearchItemsByBrand(String name, double priceFrom, double priceTo, Brands brand) {

        return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEquals(name , priceFrom, priceTo,brand);
    }

    @Override
    public List<Items> getSearchItemsByOrderAndBrand(String name, double priceFrom, double priceTo, String order, Brands brand) {
        if(order.equals("ascending")) {
            return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEqualsOrderByPriceAsc(name , priceFrom, priceTo,brand);
        }else {
            return itemRepository.findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEqualsOrderByPriceDesc(name , priceFrom, priceTo,brand);
        }
    }


    @Override
    public List<Countries> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Countries addCountry(Countries country) {
        return countryRepository.save(country);
    }

    @Override
    public Countries saveCountry(Countries country) {
        return countryRepository.save(country);
    }


    @Override
    public Countries getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Brands brand) {
        brandRepository.delete(brand);
    }

    @Override
    public List<Brands> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brands addBrand(Brands brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brands saveBrand(Brands brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brands getBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Countries country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public void deleteCategory(Categories category) {
        categoryRepository.delete(category);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return pictureRepository.findAll();
    }

    @Override
    public List<Pictures> getPicturesByItemId(Long id) {
        return pictureRepository.findAllByItem_Id(id);
    }

    @Override
    public Pictures addPicture(Pictures picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Pictures savePicture(Pictures picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Pictures getPicture(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public void deletePicture(Pictures picture) {
        pictureRepository.delete(picture);
    }

    @Override
    public List<SoldItems> getAllSoldItems() {
        return solditemRepository.findAll();
    }

    @Override
    public SoldItems addSoldItem(SoldItems soldItem) {
        return solditemRepository.save(soldItem);
    }

}
