package propets.accounting;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import propets.accounting.dao.AccountingRepository;
import propets.accounting.model.UserAccount;

@SpringBootApplication
public class ProPetsAccountingServiceApplication implements CommandLineRunner {

    @Autowired
    AccountingRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ProPetsAccountingServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(!repository.existsById("admin")) {
            String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
            UserAccount admin = UserAccount.builder()
                    .email("admin")
                    .password(hashPassword)
                    .role("USER")
                    .role("MODERATOR")
                    .role("ADMIN")
                    .build();
            repository.save(admin);
            
        }

    }

}
