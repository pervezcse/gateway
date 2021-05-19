package com.musala.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@Entity
public class Gateway {

    private static final String IPV4_ADDRESS_REG_EXP = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final String IPV4_ADDRESS_ERR_MSG = "IPv4 address is not valid";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Pattern(regexp = IPV4_ADDRESS_REG_EXP, message = IPV4_ADDRESS_ERR_MSG)
    @Column(name = "ipv4_address")
    private String ipv4Address;

    @OneToMany(mappedBy="gateway", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("gateway")
    private List<Device> devices;

    public Gateway(UUID id, String name, String ipv4Address, List<Device> devices) {
        this.id = id;
        this.name = name;
        this.ipv4Address = ipv4Address;
        this.devices = devices;
    }

    public Gateway() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
