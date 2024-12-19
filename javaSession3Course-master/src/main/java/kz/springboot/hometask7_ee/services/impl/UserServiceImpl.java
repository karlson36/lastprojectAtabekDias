package kz.springboot.hometask7_ee.services.impl;

import kz.springboot.hometask7_ee.entities.Comments;
import kz.springboot.hometask7_ee.entities.Roles;
import kz.springboot.hometask7_ee.entities.Users;
import kz.springboot.hometask7_ee.repositories.CommentRepository;
import kz.springboot.hometask7_ee.repositories.RoleRepository;
import kz.springboot.hometask7_ee.repositories.UserRepository;
import kz.springboot.hometask7_ee.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);
        if(myUser != null){
            User secUser = new User(myUser.getEmail(),myUser.getPassword(),myUser.getRoles());
            return secUser;
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users saveUser(Users user) {

        return userRepository.save(user);
    }

    @Override
    public Users createUser(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser == null){
            Roles role = roleRepository.findByName("ROLE_USER");
            if(role!=null){
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
        return null;
    }


    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Roles getRoleById(Long id) {
        return roleRepository.getOne(id);

    }

    @Override
    public List<Comments> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comments addComment(Comments comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comments saveComment(Comments comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comments getComment(Long id) {
        return commentRepository.getOne(id);
    }


    @Override
    public void deleteComment(Comments comment) {
        commentRepository.delete(comment);
    }
}
