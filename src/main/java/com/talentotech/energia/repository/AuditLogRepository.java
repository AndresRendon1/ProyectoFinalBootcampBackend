package com.talentotech.energia.repository;

import com.talentotech.energia.model.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserIdOrderByActionDateDesc(Long userId);
}
