package com.volthermtechnologies.volthermbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.voltherm.VolthermBackendApplication;

@SpringBootTest(classes = VolthermBackendApplication.class)
@ActiveProfiles("test")
class VolthermBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
