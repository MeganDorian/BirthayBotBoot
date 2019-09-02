package phoenix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phoenix.entities.User;
import phoenix.exceptions.UserException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PasswordEncoder encoder;

    public UserService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User getById(int id) {
        return entityManager.find(User.class, id);
    }

    public User getByLogin(String login) {
        User user;
        Query query = entityManager.createNativeQuery("select * from bdays.users where login=?1", User.class)
                .setParameter(1, login);
        try {
            user = (User) query.getSingleResult();
        }catch (NoResultException noResults){
//            noResults.printStackTrace();
            return null;
        }
        return user;
    }

    @Transactional
    public List<User> getAllUsers() {
        return entityManager.createNativeQuery("select * from bdays.users order by id", User.class).getResultList();
    }

    @Transactional
    public void insertUser(User user) throws UserException {
        User isAlreadyExists = getByLogin(user.getLogin());
        if (isAlreadyExists != null) {
            String message = "USER \n".concat(user.toString())
                    .concat("\nHAS THE SAME LOGIN AS THIS ONE IN DB\n")
                    .concat(isAlreadyExists.toString());
            throw new UserException(message);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        entityManager.persist(user);
    }

    @Transactional
    public boolean deleteUser(int id, org.springframework.security.core.userdetails.User principal) {
        User user = getById(id);
        if (!principal.getUsername().equals(user.getUserName())) {
            entityManager.createNativeQuery("DELETE from bdays.users where id=?1")
                    .setParameter(1, id).executeUpdate();
            return true;
        }
        return false;
    }

    @Transactional
    public void updateLastActivity(User user) {
        user.setLastActivity(LocalDate.now());
        entityManager.merge(user);
    }

    @Transactional
    public boolean updateStatus(User user, boolean active, org.springframework.security.core.userdetails.User principal) {
        if (!principal.getUsername().equals(user.getUserName())) {
            user.setActive(active);
            entityManager.merge(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateUser(User user) throws UserException {
        User checkIsThereUserWithSameLogin = getByLogin(user.getLogin());
        if (checkIsThereUserWithSameLogin != null && checkIsThereUserWithSameLogin.getId() != user.getId()) {
            String message = "USER \n".concat(user.toString())
                    .concat("\nHAS THE SAME LOGIN AS THIS ONE IN DB\n")
                    .concat(checkIsThereUserWithSameLogin.toString());
            throw new UserException(message);
        } else {
            User edit = getById(user.getId());
            if (!user.getPassword().equals(""))
                edit.setPassword(encoder.encode(user.getPassword()));
            edit.setRole(user.getRole());
            edit.setLogin(user.getLogin());
            edit.setUserName(user.getUserName());
            entityManager.merge(edit);
        }
    }
}
