package propets.accounting.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "email" })
@Document(collection = "users")
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
    public UserAccount(String name, String email) {
        this.name = name;
        this.email = email;
        roles.add("USER");
        isBlocked=false;
    }
    
    
}
