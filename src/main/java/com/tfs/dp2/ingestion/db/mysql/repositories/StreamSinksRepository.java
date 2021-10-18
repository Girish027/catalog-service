package com.tfs.dp2.ingestion.db.mysql.repositories;

import com.tfs.dp2.ingestion.db.mysql.entities.StreamSinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamSinksRepository extends JpaRepository<StreamSinkEntity, String> {
}

