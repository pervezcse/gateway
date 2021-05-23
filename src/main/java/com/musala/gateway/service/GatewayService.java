package com.musala.gateway.service;

import com.musala.gateway.exception.DeviceNotFoundException;
import com.musala.gateway.exception.GatewayNotFoundException;
import com.musala.gateway.domain.Device;
import com.musala.gateway.domain.Gateway;
import com.musala.gateway.repository.DeviceRepository;
import com.musala.gateway.repository.GatewayRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GatewayService {


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
        gateway.validate();
        if (!CollectionUtils.isEmpty(gateway.getDevices())) {
            for (Device device : gateway.getDevices()) {
                device.setId(null);
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

    public void deleteGateway(UUID gatewayId) {
        Gateway gatewayToDelete = gatewayRepository.findById(gatewayId)
                .orElseThrow(() -> new GatewayNotFoundException(gatewayId));
        gatewayRepository.delete(gatewayToDelete);
    }

    public Device addDeviceToGateway(UUID gatewayId, Device device) {
        device.validate();
        Gateway gateway = gatewayRepository.findById(gatewayId)
                .orElseThrow(() -> new GatewayNotFoundException(gatewayId));
        gateway.getDevices().size();
        gateway.getDevices().add(device);
        gateway.validate();
        device.setId(null);
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
