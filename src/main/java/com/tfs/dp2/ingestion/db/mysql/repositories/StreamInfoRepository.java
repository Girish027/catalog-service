package com.tfs.dp2.ingestion.db.mysql.repositories;

import com.tfs.dp2.ingestion.db.mysql.entities.StreamInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamInfoRepository extends JpaRepository<StreamInfoEntity, String> {
}
