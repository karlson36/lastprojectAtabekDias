package kz.springboot.hometask7_ee.controllers;


import kz.springboot.hometask7_ee.db.DBManager;
import kz.springboot.hometask7_ee.db.ShopItem;
import kz.springboot.hometask7_ee.entities.*;
import kz.springboot.hometask7_ee.services.ItemService;
import kz.springboot.hometask7_ee.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping(value = "/")
    public String index(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<Items> items = itemService.getAllItemsInTopPage();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);



        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        List<Users> users = userService.getAllUsers();

        model.addAttribute("users",users);
        return "index";
    }


    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        List<SoldItems> soldItems = itemService.getAllSoldItems();
        model.addAttribute("sold_items", soldItems);

        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));


        List<Users> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "admin_panel";
    }

    @GetMapping(value = "/search")
    public String search(Model model, @RequestParam(name = "searchInput", defaultValue = "null") String name,
                         @RequestParam(name = "priceFrom", defaultValue = "0") double priceFrom,
                         @RequestParam(name = "priceTo", defaultValue = "999999") double priceTo,
                         @RequestParam(name = "brand_id", defaultValue = "0") Long brand_id,
                         HttpSession session
                         ){
        model.addAttribute("currentUser", getUserData());
        List<Items> items = itemService.getSearchItems(name,priceFrom,priceTo);
        model.addAttribute("items", items);
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);

        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        model.addAttribute("search_name", name);
        model.addAttribute("priceFrom", priceFrom);
        model.addAttribute("priceTo", priceTo);
        return "search";
    }

    @GetMapping(value = "/order_search")
    public String orderSearch(Model model, @RequestParam(name = "itemName", defaultValue = "") String name,
                              @RequestParam(name = "priceFrom", defaultValue = "0") double priceFrom,
                              @RequestParam(name = "priceTo", defaultValue = "999999") double priceTo,
                              @RequestParam(name = "options", required = false) String order,
                              @RequestParam(name = "brand_id", required = false) Long brand_id,
                              HttpSession session){

        model.addAttribute("currentUser", getUserData());
        List<Items> items;

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        Brands brand = itemService.getBrand(brand_id);
        if(order == null && brand_id == null){
            items = itemService.getSearchItems(name,priceFrom,priceTo);
        }else if(order == null){
            items = itemService.getSearchItemsByBrand(name,priceFrom,priceTo, brand);
        }else if(brand_id == null){
            items = itemService.getSearchItemsByOrder(name,priceFrom,priceTo,order);
        }else{
            items = itemService.getSearchItemsByOrderAndBrand(name,priceFrom,priceTo,order, brand);
        }
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("brand_id", brand_id);
        model.addAttribute("items", items);
        model.addAttribute("search_name", name);
        model.addAttribute("priceFrom", priceFrom);
        model.addAttribute("priceTo", priceTo);
        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        model.addAttribute("order", order);
        return "search";
    }

    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id,
                          HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<Brands> brands = itemService.getAllBrands();

        List<Pictures> pictures = itemService.getPicturesByItemId(id);

        List<Comments> comments = userService.getAllComments();
        Roles admin = userService.getRoleById(2L);
        model.addAttribute("admin", admin);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        Roles moderator = userService.getRoleById(3L);
        model.addAttribute("moderator", moderator);

        model.addAttribute("comments", comments);
        model.addAttribute("pictures", pictures);
        model.addAttribute("brands", brands);
        model.addAttribute("item", itemService.getItem(id));
        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        return "details";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model,
                               HttpSession session){

        model.addAttribute("currentUser", getUserData());
        return "403";
    }


    @GetMapping(value = "/login")
    public String login(Model model,
                        HttpSession session){
        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        model.addAttribute("currentUser", getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model,
                          HttpSession session){
        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("currentUser", getUserData());
        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        return "profile";
    }


    public Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @PostMapping(value = "/editProfile")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(
            @RequestParam(name = "user_email") String email,
            @RequestParam(name = "user_fullname") String name
    ){
        if(email!=null && name!=null){
            getUserData().setEmail(email);
            getUserData().setFullName(name);
            userService.saveUser(getUserData());
            return "redirect:/profile?success";
        }else if(email!=null){
            getUserData().setEmail(email);

            userService.saveUser(getUserData());
            return "redirect:/profile?success";
        }else if(name!=null){
            getUserData().setFullName(name);

            userService.saveUser(getUserData());
            return "redirect:/profile?success";
        }

        return "redirect:/profile?error";
    }

    @PostMapping(value = "/editPassword")
    @PreAuthorize("isAuthenticated()")
    public String editPassword(
            @RequestParam(name = "user_old_password") String oldPassword,
            @RequestParam(name = "user_new_password") String newPassword,
            @RequestParam(name = "user_rnew_password") String rnewPassword
    ){

        if(passwordEncoder.matches(oldPassword,getUserData().getPassword()) && newPassword.equals(rnewPassword) ){
            getUserData().setPassword(passwordEncoder.encode(newPassword));

            userService.saveUser(getUserData());
            return "redirect:/profile?success";
        }
        return "redirect:/profile?error";
    }

    @PostMapping(value = "/editUserProfile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUserProfile(
            @RequestParam(name = "id") int id,
            @RequestParam(name = "user_email") String email,
            @RequestParam(name = "user_fullname") String name
    ){
        Users user = userService.getUserByEmail(email);
        if(email!=null && name!=null){
            user.setEmail(email);
            user.setFullName(name);
            userService.saveUser(user);
            return "redirect:/profile?success";
        }else if(email!=null){
            user.setEmail(email);

            userService.saveUser(user);
            return "redirect:/profile?success";
        }else if(name!=null){
            user.setFullName(name);

            userService.saveUser(user);
            return "redirect:/profile?success";
        }

        return "redirect:/profile?error";
    }

    @PostMapping(value = "/editUserPassword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUserPassword(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "user_old_password") String oldPassword,
            @RequestParam(name = "user_new_password") String newPassword,
            @RequestParam(name = "user_rnew_password") String rnewPassword
    ){

        Users user = userService.getUserById(id);
        if(passwordEncoder.matches(oldPassword,user.getPassword()) && newPassword.equals(rnewPassword) ){
            user.setPassword(passwordEncoder.encode(newPassword));

            userService.saveUser(user);
            return "redirect:/profile?success";
        }
        return "redirect:/profile?error";
    }

    @GetMapping(value = "/register")
    public String register(Model model ,
                           HttpSession session){
        model.addAttribute("currentUser", getUserData());
        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));

        return "registration";

    }

    @PostMapping(value = "/registration")
    public String registration(
            @RequestParam(name = "user_email", defaultValue = "") String email,
            @RequestParam(name = "user_password", defaultValue = "") String password,
            @RequestParam(name = "user_re-password", defaultValue = "") String rePassword,
            @RequestParam(name = "user_fullname", defaultValue = "null") String name
    ){

        if(password.equals(rePassword)){
            Users user = new Users();
            user.setPassword(password);
            user.setFullName(name);
            user.setEmail(email);
            if(userService.createUser(user)!=null){
                return "redirect:/register?success";
            }
        }
        return "redirect:/register?error";
    }


    @PostMapping(value = "/uploadAvatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava")MultipartFile file){

        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") )

        try{

            Users currentUser = getUserData();

            String picName =  DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");

            byte []bytes = file.getBytes();
            Path path = Paths.get(uploadPath + picName + ".jpg");
            Files.write(path,bytes);

            currentUser.setUserAvatar(picName);
            userService.saveUser(currentUser);

            return "redirect:/profile";
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping(value = "/viewImage/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException
    {
        String pictureURL = viewPath + defaultPicture;

        if(url != null && !url.equals("null")){
            pictureURL = viewPath + url + ".jpg";
        }

        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = "/editUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String userDetails(Model model, @PathVariable(name = "id") Long id){
        List<Roles> roles = new ArrayList<>();

        model.addAttribute("currentUser", getUserData());
        for (Roles role:userService.getAllRoles()) {
            if(!userService.getUserById(id).getRoles().contains(role)){
                roles.add(role);
            }
        }
        model.addAttribute("roles",roles);
        model.addAttribute("user", userService.getUserById(id));

        return "edit_user";
    }


    @PostMapping(value = "assign_role_to_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(@RequestParam("user_id") Long userId,
                             @RequestParam("role_id") Long roleId){

        Users user = userService.getUserById(userId);
        if(user !=null){
            Roles role = userService.getRoleById(roleId);
            if(role != null){
                if(user.getRoles() == null){
                    user.setRoles(new ArrayList<>());
                }
                if(!user.getRoles().contains(role)){
                    user.getRoles().add(role);
                }


                userService.saveUser(user);
            }
        }

        return "redirect:/editUser/" + user.getId();
    }

    @PostMapping(value = "remove_role_to_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeRole(@RequestParam("user_id") Long userId,
                             @RequestParam("role_id") Long roleId){

        Users user = userService.getUserById(userId);
        if(user !=null){
            Roles role = userService.getRoleById(roleId);
            if(role != null){
                if(user.getRoles() == null){
                    user.setRoles(new ArrayList<>());
                }
                user.getRoles().remove(role);


                userService.saveUser(user);
            }
        }

        return "redirect:/editUser/" + user.getId();
    }

    @PostMapping(value = "add_to_basket")
    public String addToBasket(@RequestParam("item_id") Long itemId,
                              HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response){
        Items item = itemService.getItem(itemId);

        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");

        boolean added = false;
        for(BasketItem basketItem : basket){
            if(basketItem.getItem().getId().equals(itemId)){
                basketItem.setAmount(basketItem.getAmount() + 1);
                added = true;
            }
        }
        if(!added){
            basket.add(new BasketItem(item, 1,itemId));
        }

        Cookie [] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(c.getName().equals("JSESSIONID")){
                c.setMaxAge(3600);
                response.addCookie(c);
            }
        }

        return "redirect:/details/" + itemId + "?success";
    }

    @GetMapping("basket")
    public  String basket(Model model,
                          HttpSession session,
                          HttpServletRequest request,
                          HttpServletResponse response){
        model.addAttribute("currentUser", getUserData());


        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories",categories);

        if(session.getAttribute("basket") == null){
            session.setAttribute("basket", new ArrayList<BasketItem>());
        }
        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        for(BasketItem item : basket){
            item.setItem(itemService.getItem(item.getId()));
        }

        model.addAttribute("basket", basket);
        model.addAttribute("number_of_basket_items",  countOfItems((List<BasketItem>) session.getAttribute("basket")));
        model.addAttribute("price_of_basket_items",  sumOfBasketItems(basket));

        return "basket";
    }

    public int countOfItems(List<BasketItem> basket){
        int count = 0;
        for(BasketItem b: basket){
            count += b.getAmount();
        }
        return count;
    }

    public double sumOfBasketItems(List<BasketItem> basket){
        double sum = 0;
        for(BasketItem b: basket){
            sum += (b.getAmount() * b.getItem().getPrice());
        }
        return sum;
    }



    @PostMapping("decrease_amount_item")
    public String increaseAmount(Model model,
                                 HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam("item_id") Long itemId){

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        for(BasketItem item : basket){
            if(item.getId().equals(itemId)){
                item.setAmount(item.getAmount() - 1);
                if(item.getAmount() == 0){
                    basket.remove(item);
                    return "redirect:/basket";
                }
            }
        }

        return "redirect:/basket";

    }

    @PostMapping("increase_amount_item")
    public String decreaseAmount(Model model,
                                 HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam("item_id") Long itemId){

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        for(BasketItem item : basket){
            if(item.getId().equals(itemId)){
                item.setAmount(item.getAmount() + 1);
            }
        }

        return "redirect:/basket";

    }


    @PostMapping("clear_basket")
    public String clearBasket(Model model,
                                 HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        basket.clear();

        return "redirect:/basket";

    }

    @PostMapping("check_in")
    public String checkIn(Model model,
                              HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response){

        List<BasketItem> basket = (List<BasketItem>) session.getAttribute("basket");
        for(BasketItem item : basket){
            itemService.addSoldItem(new SoldItems(null,item.getAmount(), new java.sql.Date(System.currentTimeMillis()), itemService.getItem(item.getId())));
        }
        basket.clear();
        return "redirect:/basket";

    }

    @PostMapping(value = "/add_comment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "comment") String comment,
                              @RequestParam(name = "item_id") Long id){
        Items item = itemService.getItem(id);
        Date date = new java.sql.Date(System.currentTimeMillis());
        userService.addComment(new Comments(null,comment,date,item, getUserData()));
        return "redirect:/details/" + id;
    }

    @PostMapping(value = "/edit_comment")
    public String changeComment(@RequestParam(name = "comment") String commentText,
                                 @RequestParam(name = "comment_id") Long id){

        Comments comment = userService.getComment(id);
        userService.saveComment(new Comments(id,commentText,comment.getAddedDate(),comment.getItem(), comment.getAuthor()));

        return "redirect:/details/" + comment.getItem().getId();
    }

    @GetMapping(value = "/delete_comment/{id}")
    public String deleteComment(@PathVariable(name = "id") Long id){

        Comments comment = userService.getComment(id);
        Long itemId = comment.getItem().getId();
        userService.deleteComment(userService.getComment(id));
        return "redirect:/details/" + itemId;
    }

}












