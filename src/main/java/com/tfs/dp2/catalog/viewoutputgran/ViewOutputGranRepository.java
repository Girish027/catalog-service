package com.tfs.dp2.catalog.viewoutputgran;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Repository
public interface ViewOutputGranRepository extends JpaRepository<ViewOutputGranularity, String> {

    /* Query to select distinct Client Information Id from View Output Granularity table */
    @Query(value = "Select distinct client_info_id from catalog.view_output_granularity where client_info_id is not null and is_active = 1", nativeQuery = true)
    List<String> findDistinctClientInfoId();

    /* Query to select distinct View Names with Is_DQ flag */
    @Query(value = "select distinct view_name, is_dq from view_information vi, " +
            "view_output_granularity vog where " +
            "vi.view_id = vog.view_id and vog.is_active = 1 and vog.client_info_id = ?1", nativeQuery = true)
    List<Object[]> findDistinctViewNameandIsDQ(String clientId);

    /* Query to find Reference Timing, Schdedule Start and End Time By ViewName And ClientInfoName */

    @Query(value = "SELECT vog.reference_time_min, vi.schedule_start_time, vi.schedule_end_time, vi.view_id, vog.granularity_in_min, vog.client_info_id, execution_properties, vi.view_group , vi.job_execution_expression as job_execution_expression_view_level, vog.job_execution_expression as job_execution_expression_client_view_level, ci.timezone, vog.granularity FROM view_output_granularity vog, view_information vi, client_information ci WHERE vi.view_name = ?1 AND vog.view_id = vog.final_view_id AND vi.view_id = vog.view_id AND vog.client_info_id = ci.info_id AND ci.info_type = 'Account' AND ci.info_name = ?2", nativeQuery = true)

    List<Object[]> findTimingsByViewNameAndInfoName(String viewName, String infoName);

    /* Query to find processor jar location by ViewName */

    @Query(value = "SELECT DISTINCT vIntermediate.processor_plugin_jar_loc AS processor_plugin_jar_loc FROM view_information vi, view_source vs, view_information vIntermediate WHERE vi.view_name = ?1 AND vIntermediate.processor_plugin_type='SCALA' AND vs.FINAL_VIEW_ID = vi.view_id AND vs.view_id = vIntermediate.VIEW_ID", nativeQuery = true)
    List<String> findProcessorJarLocationByViewName(String viewName);

    /* Query to find distinct Client Info Name for above View Id JOINING View_Output_Granularity and Client_Information tables ON Info_id */
    @Query(value = "SELECT DISTINCT ci.info_name, execution_properties, vog.sla_breach_time, vog.environment_properties,vog.job_start_time,vog.job_end_time FROM client_information ci," +
            "view_output_granularity vog WHERE vog.final_view_id = ?1 AND vog.final_view_id = vog.view_id AND " +
            "vog.client_info_id = ci.info_id And vog.cron_string = ?2 and vog.is_active = 1", nativeQuery = true)
    List<Object[]> findOrchestratorSnapshotClientListByViewId(String viewId, String cronString);

    @Query(value = "SELECT DISTINCT vog.cron_string FROM catalog.view_output_granularity vog WHERE vog.view_id = ?1 and vog.final_view_id = ?1 and vog.is_active = 1", nativeQuery = true)
    List<String> findUniqueCronString(String viewId);


    /* Query to find ProcessorSnapshot By ClientName, ViewName And Final View */
    @Query(value = "SELECT t.view_name, t.view_output_path, t.reference_time_min, t.format,vcd.column_name,t.view_id, t.granularity_in_min, " +
            "t.client_HDFS_folder_id, t.dq_enabled, t.materialization_enabled, t.processor_plugin_type," +
            "t.processor_plugin_main_class, t.prefer_read_materialized_data, t.source_plugin_class,t.strategy,t.customParams as custom_params, t.dynamic_bucket_path " +
            " FROM (SELECT vi.*, vog.reference_time_min, vog.granularity_in_min, vog.client_HDFS_folder_id,COALESCE(vog.load_strategy,vi.load_strategy) AS strategy, COALESCE(vog.custom_params, vi.custom_params) AS customParams" +
            " FROM view_information vi, view_output_granularity vog, client_information ci " +
            " WHERE  vi.view_id = vog.view_id AND vog.client_info_id = ci.info_id AND ci.info_name = ?1" +
            " AND vi.view_name = ?2 AND vog.final_view_id = (SELECT view_id FROM view_information " +
            " WHERE view_name = ?3)) t LEFT OUTER JOIN view_column_definition vcd ON vcd.column_id = t.time_dimension_column_id", nativeQuery = true)
    List<Object[]> findProcessorSnapshotByClientNameViewNameAndFinalView(String clientName, String viewName, String finalView);

    /* Query to find ProcessorSnapshot By ClientName, Alias And Final View */
    @Query(value = "SELECT t.view_name, t.view_output_path, t.reference_time_min, t.format, vcd.column_name, t.view_id, t.granularity_in_min, " +
            "t.client_HDFS_folder_id, t.dq_enabled, t.materialization_enabled, t.processor_plugin_type," +
            "t.processor_plugin_main_class, t.prefer_read_materialized_data, t.source_plugin_class,t.strategy,t.customParams as custom_params, t.dynamic_bucket_path " +
            " FROM (SELECT vi.*, vog.reference_time_min, vog.granularity_in_min, vog.client_HDFS_folder_id,COALESCE(vog.load_strategy,vi.load_strategy) AS strategy,COALESCE(vog.custom_params, vi.custom_params) AS customParams " +
            " FROM view_information vi, view_source vs, view_output_granularity vog, client_information ci " +
            " WHERE  vi.view_id = vog.view_id AND vog.client_info_id = ci.info_id AND ci.info_name = ?1 " +
            " AND vs.view_alias = ?2 AND vi.view_id = vs.source_view_id AND vs.final_view_id = (Select view_id from view_information " +
            " where view_name = ?3) AND vog.final_view_id = (SELECT view_id FROM view_information " +
            " WHERE view_name = ?4)) t LEFT OUTER JOIN view_column_definition vcd ON vcd.column_id = t.time_dimension_column_id", nativeQuery = true)
    List<Object[]> findProcessorSnapshotByClientNameAliasAndFinalView(String clientName, String alias, String finalAliasView, String finalView);


    @Query(value = "SELECT ci.info_name FROM catalog.view_output_granularity vog, catalog.client_information ci " +
            "Where vog.client_hdfs_folder_id = ci.info_id and final_view_id = ?1 And view_id = ?2 " +
            "AND client_info_id = ?3", nativeQuery = true)
    String findClientNameToInsertInPath(String finalViewId, String viewId, String clientId);

    @Query(value = "SELECT vog.final_view_id FROM catalog.view_output_granularity vog WHERE vog.view_id = vog.final_view_id " +
            "AND vog.final_view_id = (SELECT vi.view_id FROM catalog.view_information vi WHERE vi.view_name = ?1) AND " +
            "vog.client_info_id = (SELECT ci.info_id FROM catalog.client_information ci WHERE ci.info_name = ?2 AND ci.info_type = 'Account')", nativeQuery = true)
    String findFinalViewIdByViewNameAndClientName(String viewName, String clientName);

    @Query(value = "SELECT distinct source_view_id, view_id, data_range_end_min FROM catalog.view_source where final_view_id = ?1 " +
            "and final_view_id <> view_id and source_view_id is not null and source_view_id != \"\"", nativeQuery = true)
    List<Object[]> findSourceListForClientViewMappingByFinalViewId(String finalViewId);

    @Query(value = "SELECT distinct client_hdfs_folder_id FROM catalog.view_output_granularity where final_view_id =?2 and view_id =?1", nativeQuery = true)
    String findClientHDFSFolderIdByViewIdAndFinalViewId(String viewId, String finalViewId);

    @Query(value = "SELECT vi.view_name,vog.owner,vc.info_name,vog.sla_breach_time,vog.owner_email_id FROM \n" +
            "view_information vi INNER JOIN view_output_granularity vog ON vi.view_id=vog.view_id \n" +
            "INNER JOIN client_information vc ON vc.info_id = vog.client_info_id\n" +
            "WHERE vog.view_id= vog.final_view_id", nativeQuery = true)
    List<Object[]> findPipelineOwnerDetails();

    @Query(value = "SELECT * FROM catalog.view_output_granularity vog WHERE vog.view_id IN ?1 AND vog.final_view_id = vog.view_id and  vog.is_active = 1", nativeQuery = true)
    List<ViewOutputGranularity> findAllUniqueCronString(List<String>  viewId);

    /* Query to find Client Info Name for above View Id JOINING View_Output_Granularity and Client_Information tables ON Info_id */
    @Query(value = "SELECT ci.info_name, execution_properties, vog.sla_breach_time, vog.environment_properties,vog.job_start_time,vog.job_end_time,vog.view_id,vog.cron_string FROM client_information ci," +
            "view_output_granularity vog WHERE vog.final_view_id IN ?1 AND vog.final_view_id = vog.view_id AND " +
            "vog.client_info_id = ci.info_id AND vog.is_active = 1 GROUP BY ci.info_name, execution_properties, vog.sla_breach_time, vog.environment_properties,vog.job_start_time,vog.job_end_time,vog.view_id,vog.cron_string", nativeQuery = true)
    List<Object[]> findAllOrchestratorSnapshotClientListByViewId(List<String> viewId);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_output_granularity WHERE final_view_id IN ?1", nativeQuery = true)
    void deleteByFinalViewIds(List<String> viewIdList);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_output_granularity WHERE client_info_id IN ?1", nativeQuery = true)
    void deleteByClientIds(List<String> clientIdList);

    @Modifying
    @Query(value = "UPDATE catalog.view_output_granularity SET is_active = ?2 WHERE client_info_id = ?1", nativeQuery = true)
    void updateIsActiveValueForJobWithClient(String clientId, int isActive);

    @Modifying
    @Query(value = "UPDATE catalog.view_output_granularity SET is_active = ?2 WHERE final_view_id = ?1", nativeQuery = true)
    void updateIsActiveValueForJobWithFinalView(String finalViewId, int isActive);

    @Modifying
    @Query(value = "UPDATE catalog.view_output_granularity SET is_active = ?3 WHERE client_info_id = ?1 AND final_view_id = ?2", nativeQuery = true)
    void updateIsActiveValueForJobWithClientAndFinalView(String clientId, String finalViewId, int isActive);
}

