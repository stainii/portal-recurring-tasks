package be.stijnhooft.portal.housagotchi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortalHousagotchi {

    public static final String APPLICATION_NAME = "Housagotchi";

    public static void main(String[] args) {
        SpringApplication.run(PortalHousagotchi.class, args);
    }

}