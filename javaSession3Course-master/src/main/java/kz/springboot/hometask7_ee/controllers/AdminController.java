package kz.springboot.hometask7_ee.controllers;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Value("${file.item_picture.viewPath}")
    private String viewPath;

    @Value("${file.item_picture.uploadPath}")
    private String uploadPath;


    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;


    @PostMapping(value = "/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "brand_id", defaultValue = "0") Long id,
                          @RequestParam(name = "itemName", defaultValue = "null") String name,
                          @RequestParam(name = "itemDesc", defaultValue = "null") String description,
                          @RequestParam(name = "itemPrice", defaultValue = "") int price,
                          @RequestParam(name = "itemRate", defaultValue = "") int rate,
                          @RequestParam(name = "itemSmallPicUrl", defaultValue = "no text") String smallPictureUrl,
                          @RequestParam(name = "itemLargePicUrl", defaultValue = "no text") String largePictureUrl,
                          @RequestParam(name = "inTopPage", defaultValue = "") boolean inTopPage){

        Brands brand = itemService.getBrand(id);
        if(brand != null){

            Date date = new java.sql.Date(System.currentTimeMillis());
            itemService.addItem(new Items(null,name,description,price,rate,smallPictureUrl, largePictureUrl,date,inTopPage,brand,null));

        }

        return "redirect:/admin";
    }

    @PostMapping(value = "/changeItem")

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String changeItem(@RequestParam(name = "brand_id1", defaultValue = "0") Long brand_id,
                             @RequestParam(name = "id", defaultValue = "") Long id,
                             @RequestParam(name = "name123", defaultValue = "null") String name,
                             @RequestParam(name = "description123", defaultValue = "null") String description,
                             @RequestParam(name = "price123", defaultValue = "") double price,
                             @RequestParam(name = "rate123", defaultValue = "") int rate,
                             @RequestParam(name = "smallPic123", defaultValue = "no text") String smallPictureUrl,
                             @RequestParam(name = "largePic123", defaultValue = "no text") String largePictureUrl,
                             @RequestParam(name = "inTop123", defaultValue = "") boolean inTopPage){

        Brands brand = itemService.getBrand(brand_id);
        if(brand != null) {
            List<Categories> categories = itemService.getItem(id).getCategories();
            itemService.saveItem(new Items(id, name, description, price, rate, smallPictureUrl, largePictureUrl, itemService.getItem(id).getAddedDate(), inTopPage,brand,categories));
        }


        return "redirect:/admin";
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

    @GetMapping(value = "/delete/{id}")

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItem(@PathVariable(name = "id") Long id,Model model){

        model.addAttribute("currentUser", getUserData());
        itemService.deleteItem(itemService.getItem(id));

        return "redirect:/admin";
    }


    @PostMapping(value = "/addBrand")

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addBrand(@RequestParam(name = "country_id", defaultValue = "0") Long id,
                          @RequestParam(name = "brandName", defaultValue = "null") String name){

        Countries countries = itemService.getCountry(id);
        if(countries != null){

            itemService.addBrand(new Brands(null,name,countries));

        }

        return "redirect:/admin";
    }

    @PostMapping(value = "/changeBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String changeBrand(@RequestParam(name = "country_id1", defaultValue = "0") Long country_id,
                             @RequestParam(name = "id_brand", defaultValue = "") Long id,
                             @RequestParam(name = "brandName1", defaultValue = "null") String name
                             ){

        Countries countries = itemService.getCountry(country_id);
        if(countries != null){

            itemService.saveBrand(new Brands(id,name,countries));

        }


        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteBrand/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteBrand(@PathVariable(name = "id") Long id,Model model){

        model.addAttribute("currentUser", getUserData());
        itemService.deleteBrand(itemService.getBrand(id));

        return "redirect:/admin";
    }

    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCountry(@RequestParam(name = "countryName", defaultValue = "null") String name,
                             @RequestParam(name = "countryCode", defaultValue = "null") String code){

        itemService.addCountry(new Countries(null,name,code));
        return "redirect:/admin";
    }

    @PostMapping(value = "/changeCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String changeCountry(
                              @RequestParam(name = "id_country", defaultValue = "") Long id,
                                @RequestParam(name = "countryName1", defaultValue = "null") String name,
                                @RequestParam(name = "countryCode1", defaultValue = "null") String code
    ){

        itemService.saveCountry(new Countries(id,name,code));


        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteCountry/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCountry(@PathVariable(name = "id") Long id, Model model){

        model.addAttribute("currentUser", getUserData());
        itemService.deleteCountry(itemService.getCountry(id));

        return "redirect:/admin";
    }


    @GetMapping(value = "/admin_item_details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String details(Model model, @PathVariable(name = "id") Long id){
        List<Brands> brands = itemService.getAllBrands();
        List<Categories> categories = new ArrayList<>();
        List<Pictures> pictures = itemService.getPicturesByItemId(id);
        model.addAttribute("currentUser", getUserData());
        for (Categories cat:itemService.getAllCategories()) {
            if(!itemService.getItem(id).getCategories().contains(cat)){
                categories.add(cat);
            }
        }

        model.addAttribute("pictures",pictures);
        model.addAttribute("categories",categories);
        model.addAttribute("brands", brands);
        model.addAttribute("item", itemService.getItem(id));

        return "admin_item_detail";
    }

    @PostMapping(value = "/assignCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String assignCategory(@RequestParam("item_id") Long itemId,
                                 @RequestParam("category_id") Long categoryId ){
        Categories cat = itemService.getCategory(categoryId);
        if (cat != null){
            Items item = itemService.getItem(itemId);
            if(item != null){
                List<Categories> categories = item.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                categories.add(cat);
                itemService.saveItem(item);
                return "redirect:/admin/admin_item_details/" + item.getId();
            }

        }
            return "redirect:/admin";
    }
    @PostMapping(value = "/takeAwayCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String takeAwayCategory(@RequestParam("item_id1") Long itemId,
                                 @RequestParam("category_id1") Long categoryId){
        Categories cat = itemService.getCategory(categoryId);
        if (cat != null){
            Items item = itemService.getItem(itemId);
            if(item != null){
                item.getCategories().remove(cat);
                itemService.saveItem(item);
                return "redirect:/admin/admin_item_details/" + item.getId();
            }

        }
        return "redirect:/admin";
    }


    @PostMapping(value = "/addCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam(name = "categoryName", defaultValue = "null") String name,
                             @RequestParam(name = "categoryURL", defaultValue = "null") String code){

        itemService.addCategory(new Categories(null,name,code));
        return "redirect:/admin";
    }

    @PostMapping(value = "/changeCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String changeCategory(
            @RequestParam(name = "id_category", defaultValue = "") Long id,
            @RequestParam(name = "categoryName1", defaultValue = "null") String name,
            @RequestParam(name = "categoryURL1", defaultValue = "null") String code
    ){

        itemService.saveCategory(new Categories(id,name,code));


        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteCategory/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCategory(@PathVariable(name = "id") Long id, Model model){

        model.addAttribute("currentUser", getUserData());
        itemService.deleteCategory(itemService.getCategory(id));

        return "redirect:/admin";
    }




    @PostMapping(value = "/uploadPicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String uploadPicture(@RequestParam(name = "item_picture") MultipartFile file,
                                @RequestParam(name = "item_id") Long id){

        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") )

            try{

                Items item = itemService.getItem(id);

                Pictures picture = itemService.addPicture(new Pictures());
                String picName =  DigestUtils.sha1Hex("item_" + picture.getId() + "_!Picture");

                picture.setAddedDate(new java.sql.Date(System.currentTimeMillis()));
                picture.setUrl(picName);
                picture.setItem(item);

                byte []bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path,bytes);

                itemService.savePicture(picture);

                return "redirect:/admin/admin_item_details/" + id + "?success";
            }catch (Exception e){
                e.printStackTrace();
            }


        return "redirect:/admin/admin_item_details/" + id + "?error";
    }

    @GetMapping(value = "/viewPicture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException
    {
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(viewPath + url + ".jpg");
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);
    }


    @GetMapping(value = "/deletePicture/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deletePicture(@PathVariable(name = "id") Long id, Model model){

        Long itemId = itemService.getPicture(id).getItem().getId();
        itemService.deletePicture(itemService.getPicture(id));

        return "redirect:/admin/admin_item_details/" + itemId;
    }






}
