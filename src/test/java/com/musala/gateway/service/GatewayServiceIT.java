package com.musala.gateway.service;

import com.musala.gateway.domain.Gateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GatewayServiceIT {

    @Autowired
    private GatewayService gatewayService;

    @Test
    void givenInvalidIpv4Address_whenCreateGateway_thenConstraintViolationExceptionThrown(){
        Gateway gateway = new Gateway();
        gateway.setName("test");
        gateway.setIpv4Address("192.168.0.256");

        assertThrows(ConstraintViolationException.class, () -> gatewayService.createGateway(gateway));
    }
}
