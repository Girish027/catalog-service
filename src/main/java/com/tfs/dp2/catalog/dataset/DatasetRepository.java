package com.tfs.dp2.catalog.dataset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bikesh.singh on 27-08-2018.
 */

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {

    @Query(value = "SELECT * FROM catalog.dataset ds WHERE ds.view_id = (SELECT vi.view_id FROM catalog.view_information vi WHERE vi.view_name = ?1)", nativeQuery = true)
    List<Dataset> findByViewName(String viewName);

    @Query(value = "SELECT * FROM catalog.dataset ds WHERE ds.dataset_name = ?2 AND ds.view_id = ?1", nativeQuery = true)
    Dataset findByViewIdAndDatasetName(String viewId, String datasetName);

    @Query(value = "SELECT dataset_id FROM catalog.dataset WHERE view_id IN ?1", nativeQuery = true)
    List<String> findAllByViewIds(List<String> viewIdList);

    @Modifying
    @Query(value = "DELETE FROM catalog.dataset WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}
