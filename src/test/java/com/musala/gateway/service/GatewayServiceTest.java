package com.musala.gateway.service;

import com.musala.gateway.domain.Device;
import com.musala.gateway.domain.Gateway;
import com.musala.gateway.domain.enumeration.DeviceStatus;
import com.musala.gateway.exception.DeviceNotFoundException;
import com.musala.gateway.repository.DeviceRepository;
import com.musala.gateway.repository.GatewayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class GatewayServiceTest {

    private GatewayService gatewayService;
    @Mock
    private GatewayRepository gatewayRepository;
    @Mock
    private DeviceRepository deviceRepository;
    private Gateway gatewayWithMultipleDevices;
    private Gateway gatewayWithSingleDevice;
    private Gateway gatewayWithNoDevice;
    private Gateway gatewayWithMaxDevices;
    private ZonedDateTime currentDateTime;
    private Device device;

    @BeforeEach
    void initUseCase() {
        currentDateTime = ZonedDateTime.now();
        gatewayService = new GatewayService(gatewayRepository, deviceRepository);
        gatewayWithMultipleDevices = new Gateway(UUID.fromString("8dd5f315-9788-4d00-87bb-10eed9eff501"), "name 1", "192.168.0.1", null);
        Device device1 = new Device(1L, "vendor 1", gatewayWithMultipleDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device2 = new Device(2L, "vendor 2", gatewayWithMultipleDevices, DeviceStatus.ONLINE, currentDateTime);
        gatewayWithMultipleDevices.setDevices(new ArrayList<>(Arrays.asList(device1, device2)));
        gatewayWithSingleDevice = new Gateway(UUID.fromString("8dd5f315-9788-4d00-87bb-10eed9eff502"), "name 2", "192.168.0.2", null);
        Device device3 = new Device(3L, "vendor 3", gatewayWithSingleDevice, DeviceStatus.ONLINE, currentDateTime);
        gatewayWithSingleDevice.setDevices(new ArrayList<>(Arrays.asList(device3)));
        gatewayWithNoDevice = new Gateway(UUID.fromString("8dd5f315-9788-4d00-87bb-10eed9eff503"), "name 3", "192.168.0.3", null);
        device = new Device(4L, "vendor 4", gatewayWithMultipleDevices, DeviceStatus.ONLINE, currentDateTime);

        gatewayWithMaxDevices = new Gateway(null, "name 1", "192.168.0.1", null);
        Device device4 = new Device(null, "vendor 4", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device5 = new Device(null, "vendor 5", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device6 = new Device(null, "vendor 6", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device7 = new Device(null, "vendor 7", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device8 = new Device(null, "vendor 8", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device9 = new Device(null, "vendor 9", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        Device device10 = new Device(null, "vendor 10", gatewayWithMaxDevices, DeviceStatus.ONLINE, currentDateTime);
        gatewayWithMaxDevices.setDevices(new ArrayList<>(Arrays.asList(device1, device2, device3, device4, device5,
                device6, device7, device8, device9, device10)));
    }

    @Test
    void givenValidGatewayWithMultipleDevices_whenCreateGateway_thenNewGatewayIsCreated() {
        Device device1 = new Device(null, "vendor 1", null, DeviceStatus.ONLINE, currentDateTime);
        Device device2 = new Device(null, "vendor 2", null, DeviceStatus.ONLINE, currentDateTime);
        Gateway gatewayToCreate = new Gateway(null, "name 1", "192.168.0.1", Arrays.asList(device1, device2));
        when(gatewayRepository.save(gatewayToCreate)).thenReturn(gatewayWithMultipleDevices);

        Gateway newGateway = gatewayService.createGateway(gatewayToCreate);

        assertThat(newGateway.getId(), equalTo(UUID.fromString("8dd5f315-9788-4d00-87bb-10eed9eff501")));
        assertThat(newGateway.getName(), equalTo(gatewayToCreate.getName()));
        assertThat(newGateway.getIpv4Address(), equalTo(gatewayToCreate.getIpv4Address()));
        assertThat(newGateway.getDevices().size(), equalTo(gatewayToCreate.getDevices().size()));
    }

    @Test
    void givenValidGatewayWithMoreThan10Devices_whenCreateGateway_thenMaxAllowedDeviceExceptionThrown() {
        Gateway gatewayToCreate = new Gateway(null, "name 1", "192.168.0.1", null);
        Device device1 = new Device(null, "vendor 1", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device2 = new Device(null, "vendor 2", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device3 = new Device(null, "vendor 3", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device4 = new Device(null, "vendor 4", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device5 = new Device(null, "vendor 5", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device6 = new Device(null, "vendor 6", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device7 = new Device(null, "vendor 7", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device8 = new Device(null, "vendor 8", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device9 = new Device(null, "vendor 9", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device10 = new Device(null, "vendor 10", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        Device device11 = new Device(null, "vendor 11", gatewayToCreate, DeviceStatus.ONLINE, currentDateTime);
        gatewayToCreate.setDevices(Arrays.asList(device1, device2, device3, device4, device5,
                device6, device7, device8, device9, device10, device11));

        assertThrows(ConstraintViolationException.class, () -> gatewayService.createGateway(gatewayToCreate));
    }

    @Test
    void givenValidGatewayWithSingleDevice_whenAddDeviceToGateway_thenNewDeviceIsAddedToGateway() {
        UUID uuid1 = gatewayWithSingleDevice.getId();
        Device device1 = new Device(null, "vendor 4", null, DeviceStatus.ONLINE, currentDateTime);
        when(gatewayRepository.findById(uuid1)).thenReturn(Optional.of(gatewayWithSingleDevice));
        when(deviceRepository.save(device1)).thenReturn(device);

        Device newDevice = gatewayService.addDeviceToGateway(uuid1, device1);

        assertThat(newDevice.getId(), equalTo(device.getId()));
        assertThat(newDevice.getVendor(), equalTo(device.getVendor()));
        assertThat(newDevice.getStatus(), equalTo(device.getStatus()));
        assertThat(newDevice.getGateway().getId(), equalTo(device.getGateway().getId()));
        assertThat(newDevice.getCreationDateTime(), equalTo(device.getCreationDateTime()));
    }

    @Test
    void givenValidGatewayWithMaxDevices_whenAddDeviceToGateway_thenMaxAllowedDeviceExceptionThrown() {
        UUID uuid1 = gatewayWithMaxDevices.getId();
        Device device1 = new Device(null, "vendor 11", null, DeviceStatus.ONLINE, currentDateTime);
        when(gatewayRepository.findById(uuid1)).thenReturn(Optional.of(gatewayWithMaxDevices));

        assertThrows(ConstraintViolationException.class, () -> gatewayService.addDeviceToGateway(uuid1, device1));
    }

    @Test
    void givenValidGatewayWithNoDevices_whenRemoveDeviceToGateway_thenDeviceNotFoundExceptionThrown() {
        UUID uuid1 = gatewayWithNoDevice.getId();
        Long deviceId = 1L;
        when(gatewayRepository.findById(uuid1)).thenReturn(Optional.of(gatewayWithNoDevice));

        assertThrows(DeviceNotFoundException.class, () -> gatewayService.removeDeviceFromGateway(uuid1, deviceId));
    }

}
