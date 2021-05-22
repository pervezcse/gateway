package com.musala.gateway.resource;

import com.musala.gateway.domain.Device;
import com.musala.gateway.domain.Gateway;
import com.musala.gateway.service.GatewayService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class GatewayResource {

    private final GatewayService gatewayService;

    public GatewayResource(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/gateways")
    public Gateway createGateway(@RequestBody Gateway gateway) {
        return gatewayService.createGateway(gateway);
    }

    @GetMapping("/gateways")
    public List<Gateway> getAllGateways() {
        return gatewayService.getAllGateways();
    }

    @GetMapping("/gateways/{id}")
    public Gateway getGatewayDetailsById(@PathVariable UUID id) {
        return gatewayService.getGatewayDetailsById(id);
    }

    @DeleteMapping("/gateways/{id}")
    public void deleteGateway(@PathVariable UUID id) {
        gatewayService.deleteGateway(id);
    }

    @PostMapping("/gateways/{id}/devices")
    public Device addDeviceToGateway(@PathVariable UUID id, @RequestBody Device device) {
        return gatewayService.addDeviceToGateway(id, device);
    }

    @DeleteMapping("/gateways/{gatewayId}/devices/{deviceId}")
    public void removeDeviceFromGateway(@PathVariable UUID gatewayId, @PathVariable Long deviceId) {
        gatewayService.removeDeviceFromGateway(gatewayId, deviceId);
    }

}
