package com.tfs.dp2.catalog.viewsource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bikesh.singh on 09-03-2018.
 */
public interface ViewSourceRepository extends JpaRepository<ViewSource, String> {

    @Query(value="select * from catalog.view_source where final_view_id = ?1",nativeQuery = true)
    List<ViewSource> findAllWithFinalViewId(String finalView);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_source WHERE final_view_id IN ?1", nativeQuery = true)
    void deleteByFinalViewIds(List<String> viewIdList);

    @Query(value="SELECT DISTINCT vi.view_name FROM catalog.view_source vs, catalog.view_information vi WHERE vs.view_id = ?1 AND vs.final_view_id != vs.view_id AND vs.final_view_id = vi.view_id",nativeQuery = true)
    List<String> findFinalViewsWithSourceViews(String viewId);
}
