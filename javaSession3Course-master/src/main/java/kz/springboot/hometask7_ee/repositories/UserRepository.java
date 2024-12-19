package kz.springboot.hometask7_ee.repositories;

import kz.springboot.hometask7_ee.entities.Roles;
import kz.springboot.hometask7_ee.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
