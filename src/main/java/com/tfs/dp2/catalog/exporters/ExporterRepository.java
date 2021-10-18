package com.tfs.dp2.catalog.exporters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by bikesh.singh on 07-01-2019.
 */
@Repository
public interface ExporterRepository extends JpaRepository<Exporter, String> {

    @Query(value = "SELECT DISTINCT exporter_id FROM catalog.exporters WHERE exporter_name = ?1 ", nativeQuery = true)
    String findExporterIdByExporterName(String exporterName);
}
