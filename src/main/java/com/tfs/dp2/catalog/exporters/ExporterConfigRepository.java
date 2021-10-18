package com.tfs.dp2.catalog.exporters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bikesh.singh on 07-01-2019.
 */

@Repository
public interface ExporterConfigRepository extends JpaRepository<ExporterConfig, String> {

    List<ExporterConfig> findByExporterId(String exporterId);
}
