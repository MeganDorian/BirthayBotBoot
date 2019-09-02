package phoenix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import phoenix.entities.User;
import phoenix.exceptions.UserException;
import phoenix.services.UserService;
import phoenix.services.UsersInformationProvider;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsersInformationProvider provider;

    private static Logger log=Logger.getLogger(Exception.class.getName());

    @RequestMapping("/users")
    public ModelAndView showUsers() {
        return new ModelAndView("user/users", "users", userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id) throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.deleteUser(id, (org.springframework.security.core.userdetails.User) authentication.getPrincipal()))
            throw new UserException("ADMIN TRIED TO DELETE HIMSELF");
        return new ModelAndView("redirect:/users");
    }

    @GetMapping("/editUser/{id}")
    public ModelAndView editUser(@PathVariable("id") int id, Model model) {
        User user = userService.getById(id);
        if (user == null)
            throw new NullPointerException("ENTITY NOT FOUND WITH SUCH ID");

        model.addAttribute(user);
        String role = provider.userRole(user.getRole());
        model.addAttribute(role, "selected");

        return new ModelAndView("user/editUser");
    }

    @PostMapping("/editUser/{id}")
    public ModelAndView saveChanges(@Valid @ModelAttribute("save") User user,
                                    Model model, @PathVariable int id) {
        try {
            userService.updateUser(user);
        } catch (UserException userException) {
            model.addAttribute("error", "This login is already taken");
            model.addAttribute(user);
            model.addAttribute(provider.userRole(user.getRole()), "selected");
            return new ModelAndView("user/editUser");
        }
        return new ModelAndView("redirect:/users");
    }

    @GetMapping("/addUser")
    public ModelAndView addNewUser(@Valid @ModelAttribute("newUser") User newUser, Model model) {
        model.addAttribute("newUser", newUser);
        return new ModelAndView("user/addUser");
    }

    @PostMapping("/addUser")
    public ModelAndView saveNewUser(@Valid @ModelAttribute("newUser") User newUser,
                                    @ModelAttribute("password") String password, Model model) {
        try {
            userService.insertUser(newUser);
        } catch (UserException userException) {
            model.addAttribute(newUser);
            model.addAttribute("error", "This login is already taken");
            model.addAttribute("password", password);
            return new ModelAndView("user/addUser");
        }
        return new ModelAndView("redirect:/users");
    }

    @GetMapping("/status/{id}")
    public ModelAndView changeUserStatus(@PathVariable("id") int id) throws UserException {
        User user = userService.getById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!userService.updateStatus(user, !user.getActive(),
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal())) {
            throw new UserException("ADMIN TRIED TO BLOCK HIMSELF");
        }
        return new ModelAndView("redirect:/users");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String numberFormatExceptionCaught(MethodArgumentTypeMismatchException exception, Model model) {
        model.addAttribute("page", "/users");
        log.log(Level.INFO, "NUMBER FORMAT EXCEPTION IN URL", exception);
        return "errorPages/400";
    }

    @ExceptionHandler(UserException.class)
    public ModelAndView tryToBlockOrDeleteCurrentAdmin(Model model) {
        model.addAttribute("error", "You can't block or delete yourself");
        return new ModelAndView("user/users", "users", userService.getAllUsers());
    }

    @ExceptionHandler(NullPointerException.class)
    public String userNotFound(NullPointerException exception, Model model) {
        exception.printStackTrace();
        model.addAttribute("page", "/users");
        return "errorPages/entityNotFound";
    }
}
