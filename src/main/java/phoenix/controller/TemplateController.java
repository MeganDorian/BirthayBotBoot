package phoenix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import phoenix.entities.TemplateMessage;
import phoenix.services.TemplateService;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    private static Logger log=Logger.getLogger(Exception.class.getName());

    @GetMapping("/templates")
    public ModelAndView showTemplates() {
        return new ModelAndView("template/templates", "allTemplates", templateService.templateSelectAll());
    }

    @PostMapping("/addTemplate")
    public ModelAndView saveNewTemplate(@RequestParam("message") String message, Model model) {
        if (message.equals("")) {
            model.addAttribute("error", "Template message is null");
            return new ModelAndView("template/addTemplate");
        }
        try {
            templateService.insertNewTemplateInDB(new TemplateMessage(message));
        }catch (RuntimeException e) {
            if(e.getCause() instanceof PersistenceException){
                log.log(Level.INFO, "EXCEPTION THROWN WHILE MERGE OPERATION", e);
            }
            else
                log.log(Level.INFO, "UNHANDLED EXCEPTION FOUND", e);
        }
        return new ModelAndView("redirect:/templates");
    }

    @GetMapping("/addTemplate")
    public String addNewTemplate() {
        return "/template/addTemplate";
    }

    @GetMapping("templates/{id}")
    public ModelAndView deleteTemplate(@PathVariable int id) {
        templateService.deleteTemplate(id);
        return new ModelAndView("redirect:/templates");
    }

    @GetMapping("/editTemplate/{id}")
    public String editTemplate(@PathVariable int id, Model model) {
        TemplateMessage templateMessage = templateService.templateSearchId(id);
        model.addAttribute("template", templateMessage.getTemplate());
        return "template/editTemplate";
    }

    @PostMapping("/editTemplate/{id}")
    public ModelAndView saveEditedTemplate(@Valid @ModelAttribute("saveChanges") TemplateMessage template,
                                           Model model, @PathVariable String id) {
        if (template.getTemplate().equals("")) {
            model.addAttribute("error", "Template message is null");
            return new ModelAndView("template/editTemplate");
        } else {
            try {
                templateService.editTemplate(template);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof PersistenceException) {
                    log.log(Level.INFO, "EXCEPTION THROWN WHILE MERGE OPERATION", e);
                }
                else
                    log.log(Level.INFO, "UNHANDLED EXCEPTION FOUND", e);
                model.addAttribute("template", template.getTemplate());
                return new ModelAndView("template/editTemplate");
            }
            return new ModelAndView("redirect:/templates");
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String numberFormatException(MethodArgumentTypeMismatchException exception, Model model) {
        model.addAttribute("page", "/templates");
        log.log(Level.INFO, "NUMBER FORMAT EXCEPTION IN URL", exception);
        return "errorPages/400";
    }


    @ExceptionHandler(NullPointerException.class)
    public String templateNotFound(NullPointerException exception, Model model) {
        log.log(Level.INFO, "TEMPLATE NOT FOUND WITH SUCH ID", exception);
        model.addAttribute("page", "/templates");
        return "errorPages/entityNotFound";
    }
}
