package com.tfs.dp2.catalog.exporters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bikesh.singh on 07-01-2019.
 */

@Repository
public interface ExporterMappingRepository extends JpaRepository<ExporterMapping, String> {

    @Query(value = "SELECT exp.exporter_name FROM catalog.exporters exp, catalog.client_view_exporter_mapping cvem WHERE cvem.client_id = ?1 AND cvem.view_id = ?2 AND exp.exporter_id = cvem.exporter_id", nativeQuery = true)
    List<String> findByClientIdAndViewId(String clientId, String viewId);

    ExporterMapping findByClientIdAndViewIdAndExporterId(String clientId, String viewId, String exporterId);

    @Query(value = "SELECT exp.exporter_name, exp.exporter_id, exp.exporter_jar_loc, exp.exporter_main_class, cvem.client_view_exporter_mapping_id FROM catalog.exporters exp, catalog.client_view_exporter_mapping cvem WHERE cvem.client_id = ?1 AND cvem.view_id = ?2 AND cvem.is_active = 1 AND exp.exporter_id = cvem.exporter_id", nativeQuery = true)
    List<Object[]> findExporterMappingByClientIdAndViewId(String clientId, String viewId);

    @Query(value = "SELECT distinct exp.exporter_jar_loc FROM catalog.exporters exp, catalog.client_view_exporter_mapping cvem WHERE cvem.client_id =?1 AND cvem.view_id =?2 AND exp.exporter_id = cvem.exporter_id AND exp.exporter_jar_loc is NOT NULL", nativeQuery = true)
    List<String> findAllExporterJarLocByClientIdAndViewId(String clientId, String viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.client_view_exporter_mapping WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);

    @Modifying
    @Query(value = "DELETE FROM catalog.client_view_exporter_mapping WHERE client_id IN ?1", nativeQuery = true)
    void deleteByClientIds(List<String> clientIdList);
}
