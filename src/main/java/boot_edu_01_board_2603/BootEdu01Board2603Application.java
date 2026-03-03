package boot_edu_01_board_2603;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BootEdu01Board2603Application {

    public static void main(String[] args) {
        SpringApplication.run(BootEdu01Board2603Application.class, args);
    }

}