package phoenix.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phoenix.entities.Birthday;
import phoenix.exceptions.BirthdayException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Service
public class BirthdayService {

    @PersistenceContext
    private EntityManager entityManager;

    public BirthdayService(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

    public Birthday selectById(int id) {
        return entityManager.find(Birthday.class, id);
    }

    public List<Birthday> selectByDate (LocalDate date) {
        Query query =entityManager.createNativeQuery("select * from bdays.birthdays where date_of_birth=?1", Birthday.class).
                setParameter(1, date);
        return query.getResultList();
    }

    public List<Birthday> selectAll () {
        Query query = entityManager.createNativeQuery("select * from bdays.birthdays order by id", Birthday.class);
        return query.getResultList();
    }

    @Transactional
    public void insertBirthday(Birthday birthday) throws BirthdayException {
        if(birthday.getDateOfBirth().isAfter(LocalDate.now())) {
            String message="TRYING TO SAVE BIRTHDAY WITH DATE OF BIRTH GREATER THAN TODAY'S "
                    .concat(birthday.toString());
            throw new BirthdayException(message);
        }
        entityManager.persist(birthday);
    }

    @Transactional
    public void deleteBirthday(int id) {
        entityManager.createNativeQuery("DELETE from bdays.birthdays where id=?1")
                .setParameter(1, id).executeUpdate();
    }

    @Transactional
    public void editBirthday(Birthday birthday) throws BirthdayException {
        if(birthday.getDateOfBirth().isAfter(LocalDate.now()))  {
            String message="TRYING TO SAVE BIRTHDAY WITH DATE OF BIRTH GREATER THAN TODAY'S "
                    .concat(birthday.toString());
            throw new BirthdayException(message);
        }
        Birthday changed = selectById(birthday.getId());
        changed.setDateOfBirth(birthday.getDateOfBirth());
        changed.setUserName(birthday.getUserName());
        entityManager.merge(changed);
    }
}
