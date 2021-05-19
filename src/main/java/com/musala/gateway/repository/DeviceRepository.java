package com.musala.gateway.repository;

import com.musala.gateway.domain.Device;
import com.musala.gateway.domain.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Long countDevicesByGateway(Gateway gateway);
}
