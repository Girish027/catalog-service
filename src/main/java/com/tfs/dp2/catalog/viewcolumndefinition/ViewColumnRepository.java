package com.tfs.dp2.catalog.viewcolumndefinition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Repository
public interface ViewColumnRepository extends JpaRepository<ViewColumnDefinition, String> {
    List<ViewColumnDefinition> findByViewIdAndUniqueKeyFlagOrderByUniqueKeyOrder(
            String viewId, int uniqueKeyFlag);
    List<ViewColumnDefinition> findByViewId(String viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_column_definition WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}

