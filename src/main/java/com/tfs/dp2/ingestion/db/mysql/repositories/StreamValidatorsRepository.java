package com.tfs.dp2.ingestion.db.mysql.repositories;

import com.tfs.dp2.ingestion.db.mysql.entities.StreamValidatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamValidatorsRepository extends JpaRepository<StreamValidatorEntity, String> {
}
