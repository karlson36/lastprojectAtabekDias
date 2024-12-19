package kz.springboot.hometask7_ee.repositories;

import kz.springboot.hometask7_ee.entities.Brands;
import kz.springboot.hometask7_ee.entities.SoldItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SolditemRepository extends JpaRepository<SoldItems, Long> {
}
