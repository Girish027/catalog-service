package com.tfs.dp2.catalog.viewsqldefinition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bikesh.singh on 09-03-2018.
 */
public interface ViewSQLDefinitionRepository extends JpaRepository<ViewSQLDefinition, String> {

    @Modifying
    @Query(value = "DELETE FROM catalog.view_sql_definition WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}
