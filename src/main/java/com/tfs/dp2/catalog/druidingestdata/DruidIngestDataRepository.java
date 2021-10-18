package com.tfs.dp2.catalog.druidingestdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bikesh.singh on 24-02-2018.
 */
public interface DruidIngestDataRepository extends JpaRepository<DruidIngestData, String> {

    @Query(value = "SELECT * FROM catalog.druid_ingestion_data WHERE view_id = ?1", nativeQuery = true)
    List<DruidIngestData> findByViewId(String viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.druid_ingestion_data WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}
