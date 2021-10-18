package com.tfs.dp2.catalog.sourceAdapter;

import com.tfs.dp2.catalog.dataset.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceAdapterRepository extends JpaRepository<SourceAdapterConfig, String>
{
    /* Query to find all values for Source for given viewId */
    List<SourceAdapterConfig> findByViewId(String viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.source_adapter_config WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}
