package com.musala.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musala.gateway.domain.enumeration.DeviceStatus;
import com.musala.gateway.utils.BeanValidator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "vendor")
    private String vendor;

    @JsonIgnoreProperties("devices")
    @ManyToOne
    @JoinColumn(name="gateway_id", nullable = false)
    private Gateway gateway;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    @Column(name = "creation_date_time", updatable = false)
    private ZonedDateTime creationDateTime;

    public Device(Long id, String vendor, Gateway gateway, DeviceStatus status, ZonedDateTime creationDateTime) {
        this.id = id;
        this.vendor = vendor;
        this.gateway = gateway;
        this.status = status;
        this.creationDateTime = creationDateTime;
    }

    public Device() {

    }

    public void validate() {
        BeanValidator.validate(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
