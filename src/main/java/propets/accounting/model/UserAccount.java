package propets.accounting.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "email" })
@Document(collection = "users")
@ToString
@Builder
public class UserAccount implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 157005433026450946L;
    String name;
    String password;
    @Id
    String email;
    String phone;
    String avatar;
    @Singular
    Set<String> roles = new HashSet<>();
    boolean isBlocked;
    
    List<String> activities; // lostAndFound
    List<String> favorites; // messages
    
//    List<String> activityMessages;
//    List<String> activityLostAndFound;
//    
//    List<String> favoriteMessages;
//    List<String> favoriteLostAndFound;
    
    
    public UserAccount() {
    }
    
    public UserAccount(String name, String email) {
        this.name = name;
        this.email = email;
        roles.add("USER");
        isBlocked=false;
        avatar = "";
        phone = "";
    }
    
    public Set<String> addUserRole(String role) {
    	roles.add(role);
    	return roles;
    }
    
    public Set<String> removeUserRole(String role) {
    	roles.remove(role);
    	return roles;
    }
    
}
