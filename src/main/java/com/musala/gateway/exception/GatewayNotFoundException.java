package com.musala.gateway.exception;

import java.util.UUID;

public class GatewayNotFoundException extends RuntimeException {

    public GatewayNotFoundException(UUID id) {
        super("no gateway found: " + id);
    }
}
