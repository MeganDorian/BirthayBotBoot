package phoenix.services;

import org.springframework.stereotype.Component;

@Component
public class UsersInformationProvider {

    public String userRole(String role) {
        switch(role) {
            case "ADMIN":
                return "isAdmin";
            case "USER":
                return "isUser";
        }
        return role;
    }
}
