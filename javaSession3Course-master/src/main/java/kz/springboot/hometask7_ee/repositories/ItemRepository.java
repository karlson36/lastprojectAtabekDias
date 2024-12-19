package kz.springboot.hometask7_ee.repositories;

import kz.springboot.hometask7_ee.entities.Brands;
import kz.springboot.hometask7_ee.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
    List<Items> findAllByInTopPageIsTrue();
    List<Items> findAllByInTopPageIsFalse();
    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqual(String name, double priceFrom, double priceTo);
    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(String name, double priceFrom, double priceTo);

    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceDesc(String name, double priceFrom, double priceTo);
    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEquals(String name, double priceFrom, double priceTo, Brands brand);
    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEqualsOrderByPriceAsc(String name, double priceFrom, double priceTo, Brands brand);
    List<Items> findAllByNameContainsAndPriceGreaterThanEqualAndPriceLessThanEqualAndBrandEqualsOrderByPriceDesc(String name, double priceFrom, double priceTo, Brands brand);
}
