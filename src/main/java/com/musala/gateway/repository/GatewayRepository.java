package com.musala.gateway.repository;

import com.musala.gateway.domain.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GatewayRepository extends JpaRepository<Gateway, UUID> {
}
