package kz.springboot.hometask7_ee.services;

import kz.springboot.hometask7_ee.entities.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);
    Users saveUser(Users user);
    Users createUser(Users user);


    List<Roles> getAllRoles();
    Users getUserById(Long id);
    List<Users> getAllUsers();
    Roles getRoleById(Long id);

    List<Comments> getAllComments();
    Comments addComment(Comments comment);
    Comments saveComment(Comments comment);
    Comments getComment(Long id);
    void deleteComment(Comments comment);
}
