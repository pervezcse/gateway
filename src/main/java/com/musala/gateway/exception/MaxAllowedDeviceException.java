package com.musala.gateway.exception;

public class MaxAllowedDeviceException extends RuntimeException {

    public MaxAllowedDeviceException(int maxAllowedDeviceCount) {
        super("no more than " + maxAllowedDeviceCount + " peripheral devices are allowed for a gateway");
    }
}
