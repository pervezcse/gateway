package com.musala.gateway.exception;


public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(Long id) {
        super("no device found: " + id);
    }
}
