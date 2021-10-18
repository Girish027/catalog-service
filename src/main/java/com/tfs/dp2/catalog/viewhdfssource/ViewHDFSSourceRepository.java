package com.tfs.dp2.catalog.viewhdfssource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Repository
public interface ViewHDFSSourceRepository extends JpaRepository<ViewHDFSSource, String> {

    @Query(value = "Select vhs.hdfs_source_view_id, vhs.hdfs_path, vs.data_range_start_min, " +
            "vs.data_range_end_min from view_hdfs_source vhs, view_source vs where vs.final_view_id = ?1 " +
            "AND vhs.view_id = ?2 AND vhs.hdfs_source_view_id = vs.source_view_id", nativeQuery = true)
    List<Object[]> findHDFSSourcePathDataRangeByFinalViewIdAndViewId(String finalViewId,String viewId);

    @Query(value = "Select vi.view_name, vhs.hdfs_path from view_hdfs_source vhs, view_information vi where vhs.view_id = ?1 " +
            "AND vhs.hdfs_source_view_id = vi.view_id", nativeQuery = true)
    List<Object[]> findHDFSSourceNameAndPathByViewId(String viewId);

    @Query(value = "Select vhs.hdfs_path from catalog.view_hdfs_source vhs where vhs.view_id = ?1 " +
            "AND vhs.hdfs_source_view_id = ?2", nativeQuery = true)
    String findHDFSSourcePathByViewIdAndFinalViewId(String finalViewId, String viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_hdfs_source WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);
}
