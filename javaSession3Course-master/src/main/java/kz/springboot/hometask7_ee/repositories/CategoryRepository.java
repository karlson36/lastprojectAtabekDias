package kz.springboot.hometask7_ee.repositories;

import jdk.jfr.Category;
import kz.springboot.hometask7_ee.entities.Categories;
import kz.springboot.hometask7_ee.entities.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Categories, Long> {
}
