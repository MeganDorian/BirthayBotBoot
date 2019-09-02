package phoenix.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import phoenix.entities.TemplateMessage;
import phoenix.services.TemplateService;

import java.util.List;
import java.util.Random;

@Component
public class MessageConcat {
    private Random random;
    private TemplateService templateService;

    public MessageConcat(TemplateService tmp, Random rand) {
        random = rand;
        templateService = tmp;
    }

    public String birthdayAndTemplateConcat(String birthday) {
        List<TemplateMessage> list = templateService.templateSelectAll();
        String concat = list.get(random.nextInt(list.size())).getTemplate();
        return birthday.concat(concat);
    }
}
