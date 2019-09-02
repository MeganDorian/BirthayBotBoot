package phoenix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.exceptions.TemplateInputException;
import phoenix.entities.Birthday;
import phoenix.exceptions.BirthdayException;
import phoenix.services.BirthdayService;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class BirthdayController {

    @Autowired
    private BirthdayService birthdayService;

    private static Logger log=Logger.getLogger(Exception.class.getName());

    @RequestMapping("/bdays")
    public ModelAndView showBirthdays() {
        return new ModelAndView("birthday/bdays", "allBdays", birthdayService.selectAll());
    }
    @GetMapping("/add")
    public ModelAndView addNewBirthday(@Valid @ModelAttribute("newBirthday") Birthday newBirthday, Model model) {
        model.addAttribute("newBirthday", newBirthday);
        return new ModelAndView("birthday/addBirthday");
    }

    @PostMapping("/add")
    public ModelAndView saveNewBirthday(@Valid @ModelAttribute("newBirthday") Birthday newBirthday, Model model) {
        try {
            birthdayService.insertBirthday(newBirthday);
        } catch (BirthdayException birthdayException) {
            model.addAttribute("inputError", "Date of birth must be less than current one");
            model.addAttribute(newBirthday);
            return new ModelAndView("birthday/addBirthday");
        }
        return new ModelAndView("redirect:/bdays");
    }

    //save changes
    @PostMapping("/editBirthday/{id}")
    public ModelAndView saveEditedBirthday(@Valid @ModelAttribute("saveChanges") Birthday birthday,
                                           Model model, @PathVariable int id) {
        try {
            birthdayService.editBirthday(birthday);
        } catch (BirthdayException birthdayException) {
            model.addAttribute(birthday);
            model.addAttribute("error", "Date of birth must be less than current one");
            return new ModelAndView("birthday/editBirthday");
        }
        return new ModelAndView("redirect:/bdays");
    }

    //set old values
    @GetMapping("/editBirthday/{id}")
    public ModelAndView editBirthday(@PathVariable("id") int id, Model model) {
        Birthday birthday = birthdayService.selectById(id);

        if(birthday==null)
            throw new TemplateInputException("ENTITY NOT FOUND WITH SUCH ID");

        model.addAttribute(birthday);
        return new ModelAndView("birthday/editBirthday");
    }

    //delete from table
    @GetMapping("/bdays/{id}")
    public ModelAndView deleteRowFromBirthday(@PathVariable ("id") int id) {
        birthdayService.deleteBirthday(id);
        return new ModelAndView("redirect:/bdays");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String numberFormatException(MethodArgumentTypeMismatchException exception, Model model) {
        model.addAttribute("page", "/bdays");
        log.log(Level.INFO, "NUMBER FORMAT EXCEPTION IN URL",exception);
        return "errorPages/400";
    }


    @ExceptionHandler(TemplateInputException.class)
    public String birthdayNotFound(Model model) {
//        exception.printStackTrace();
        model.addAttribute("page", "/bdays");
        return "errorPages/entityNotFound";
    }
}

