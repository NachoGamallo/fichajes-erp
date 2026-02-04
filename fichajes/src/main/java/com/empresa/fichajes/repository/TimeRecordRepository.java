package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {

    List<TimeRecord> findByEmployee_IdAndTimestampAfter(Long employeeId, LocalDateTime timestampAfter);

}
