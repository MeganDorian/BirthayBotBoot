package phoenix.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import phoenix.entities.TemplateMessage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class TemplateService {

    @PersistenceContext
    private EntityManager entityManager;

    public TemplateService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List <TemplateMessage> templateSelectAll () {
        Query query = entityManager.createNativeQuery("select * from bdays.templates order by id", TemplateMessage.class);
        return query.getResultList();
    }

    public TemplateMessage templateSearchId (int id) {
        return entityManager.find(TemplateMessage.class, id);
    }


    @Transactional
    public void insertNewTemplateInDB(TemplateMessage templateMessage) {
        entityManager.persist(templateMessage);
    }


    @Transactional
    public void deleteTemplate(int id) {
        entityManager.createNativeQuery("DELETE from bdays.templates where id=?1")
                .setParameter(1, id).executeUpdate();
    }

    @Transactional
    public void editTemplate(TemplateMessage templateMessage) {
        TemplateMessage changedTemplate = templateSearchId(templateMessage.getId());
        changedTemplate.setTemplate(templateMessage.getTemplate());
        entityManager.merge(changedTemplate);
    }
}
