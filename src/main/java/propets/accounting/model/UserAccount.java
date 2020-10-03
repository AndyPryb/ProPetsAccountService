package propets.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "email" })
@Document(collection = "forum_users")
public class UserAccount {
    String name;
    String password;
    String email;
    String phone;
    String avatar;
    Set<String> roles = new HashSet<>();
}
