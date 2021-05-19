package com.musala.gateway.service;

import com.musala.gateway.exception.DeviceNotFoundException;
import com.musala.gateway.exception.GatewayNotFoundException;
import com.musala.gateway.exception.MaxAllowedDeviceException;
import com.musala.gateway.domain.Device;
import com.musala.gateway.domain.Gateway;
import com.musala.gateway.repository.DeviceRepository;
import com.musala.gateway.repository.GatewayRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GatewayService {

    private static final int MAX_ALLOWED_DEVICE_COUNT = 10;
    private final GatewayRepository gatewayRepository;
    private final DeviceRepository deviceRepository;

    public GatewayService(GatewayRepository gatewayRepository, DeviceRepository deviceRepository) {
        this.gatewayRepository = gatewayRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<Gateway> getAllGateways() {
        return gatewayRepository.findAll();
    }

    public Gateway createGateway(Gateway gateway) {
        if (!CollectionUtils.isEmpty(gateway.getDevices())) {
            if (gateway.getDevices().size() > MAX_ALLOWED_DEVICE_COUNT) {
                throw new MaxAllowedDeviceException(MAX_ALLOWED_DEVICE_COUNT);
            }
            for (Device device : gateway.getDevices()) {
                device.setGateway(gateway);
                device.setCreationDateTime(ZonedDateTime.now());
            }
        }
        return gatewayRepository.save(gateway);
    }

    public Gateway getGatewayDetailsById(UUID id) {
        return gatewayRepository.findById(id)
                .orElseThrow(() -> new GatewayNotFoundException(id));
    }

    public Device addDeviceToGateway(UUID gatewayId, Device device) {
        Gateway gateway = gatewayRepository.findById(gatewayId)
                .orElseThrow(() -> new GatewayNotFoundException(gatewayId));
        if(gateway.getDevices().size() == MAX_ALLOWED_DEVICE_COUNT) {
            throw new MaxAllowedDeviceException(MAX_ALLOWED_DEVICE_COUNT);
        }
        device.setGateway(gateway);
        device.setCreationDateTime(ZonedDateTime.now());
        return deviceRepository.save(device);
    }

    public void removeDeviceFromGateway(UUID gatewayId, Long deviceId) {
        Gateway gateway = gatewayRepository.findById(gatewayId)
                .orElseThrow(() -> new GatewayNotFoundException(gatewayId));
        if(CollectionUtils.isEmpty(gateway.getDevices())) {
            throw new DeviceNotFoundException(deviceId);
        }
        Device device = gateway.getDevices().stream()
                .filter(d -> d.getId().equals(deviceId)).findFirst()
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        gateway.getDevices().remove(device);
        gatewayRepository.save(gateway);
    }

}
