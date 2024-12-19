package kz.springboot.hometask7_ee.repositories;

import kz.springboot.hometask7_ee.entities.Categories;
import kz.springboot.hometask7_ee.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
