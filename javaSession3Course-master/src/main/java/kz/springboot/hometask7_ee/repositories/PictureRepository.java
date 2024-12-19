package kz.springboot.hometask7_ee.repositories;

import kz.springboot.hometask7_ee.entities.Items;
import kz.springboot.hometask7_ee.entities.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PictureRepository  extends JpaRepository<Pictures, Long> {
    List<Pictures> findAllByItem_Id(Long id);
}
