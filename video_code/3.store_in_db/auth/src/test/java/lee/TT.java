package lee;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TT {

    @Test
    public void testPass(){
        String encode = new BCryptPasswordEncoder().encode("123123");
        System.out.println(encode);
    }
}
