package com.tfs.dp2.catalog.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bikesh.singh on 27-02-2018.
 */
@Repository
@Transactional
public interface ViewRepository extends JpaRepository<View, String> {

    View findByViewName(String viewName);

    List<View> findByIsActiveAndViewTypeIn(int isActive, List<String> viewType);

    /* Query to find attributes of ViewDefinition entity by View Name */
    @Query(value = "SELECT t.view_id,t.view_name,t.view_description,t.layer_id,t.layer_name,t.definition_name," +
            "t.sql_definition,t.ttl_Days, t.es_index_enabled, t.druid_index_enabled, t.view_output_path,vcd.column_name, t.format," +
            "t.materialization_enabled, t.load_strategy, t.owner, t.queue, t.custom_params, t.dynamic_bucket_path " +
            "           FROM (SELECT vi.*,vl.layer_name, vsd.definition_name, vsd.sql_definition, (SELECT execution_properties FROM view_group WHERE id = vi.view_group) queue " +
            "           FROM view_information vi, view_layer vl, view_sql_definition vsd WHERE vi.layer_id = vl.layer_id AND vi.definition_id = vsd.definition_id AND vi.view_name = ?1) t LEFT OUTER JOIN view_column_definition vcd ON vcd.column_id = t.time_dimension_column_id;", nativeQuery = true)

    List<Object[]> findViewDefinitionByViewName(String viewName);

    /* Query to find attributes of Imports entity by ViewId and View Name */
    @Query(value = "select DISTINCT T.ViewName,T.StartTime,T.EndTime from ( " +
            "             SELECT DISTINCT vi.view_name AS 'ViewName',vs.data_range_start_min as 'StartTime',vs.data_range_end_min as 'EndTime' " +
            "            FROM view_information vi, view_source vs  " +
            "             WHERE vs.source_view_id = vi.view_id AND vs.source_view_id IN (SELECT source_view_id FROM view_source  " +
            "             WHERE source_type not in('tempView') AND view_id = ?1 AND final_view_id = (Select view_id from view_information where view_name = ?2)) " +
            "             AND vs.view_id = ?1 AND vs.final_view_id = (Select view_id from view_information where view_name = ?2) AND vs.view_alias is null " +
            "             UNION " +
            "             SELECT DISTINCT " +
            "             concat(view_alias,'__',(SELECT view_name FROM view_information WHERE view_id = ?1)) AS 'ViewName', " +
            "             data_range_start_min as 'StartTime', " +
            "             data_range_end_min as 'EndTime'  " +
            "             FROM view_source " +
            "             WHERE view_id=?1 and final_view_id=(select view_id from view_information where view_name=?2) AND view_alias is not null) T", nativeQuery = true)
    List<Object[]> findImportsByViewIdAndViewName(String viewId, String viewName);


    /* Query to find Processor Snapshot sqls by View Id */
    @Query(value = "SELECT vsd.sql_definition FROM view_information vi," +
            " view_sql_definition vsd WHERE vsd.definition_id = vi.definition_id AND vi.view_id= ?1", nativeQuery = true)
    List<String> findProcessorSnapshotSQLsByViewId(String viewId);

    @Query(value = "SELECT vi.is_dynamicsql FROM view_information vi WHERE vi.view_id= ?1", nativeQuery = true)
    String findIsDynamicSQL(String viewId);

    @Query(value = "select view_name from view_information where view_id=?1", nativeQuery = true)
    String findViewNameByViewId(String viewId);

    @Query(value = "select view_id from view_information where view_name=?1", nativeQuery = true)
    String findViewIdByViewName(String viewName);

    @Query(value = "select vcd.column_name,rsd.rule_definition_name, rsd.sql_definition, rsd.column_list,vcd.column_path " +
            "from rule_sql_definition rsd, rule_on_view rov ,view_column_definition vcd where rov.view_id = ?1 AND rov.rule_definition_id is not null " +
            "AND rov.rule_definition_id = rsd.rule_definition_id AND rov.is_active = 1 and rov.view_column_id =vcd.column_id order by rov.Order", nativeQuery = true)
    List<Object[]> findColumnsForDq(String viewId);

    @Query(value = "select vcd.column_path from view_column_definition vcd where vcd.view_id = ?1 AND vcd.unique_key_flag = 1 ORDER BY Unique_Key_Order;", nativeQuery = true)
    ArrayList<String> findUniqueKeyByView(String viewId);

    @Query(value = "select vcd.column_path from view_column_definition vcd where vcd.view_id = ?1 AND vcd.column_identifier = 1", nativeQuery = true)
    String findClientIdColumnPathByView(String viewId);

    @Query(value = "select vcd.column_path from view_column_definition vcd where vcd.view_id = ?1 AND vcd.column_identifier = 2", nativeQuery = true)
    String findEventTimeColumnPathByView(String viewId);

    @Query(value = "select column_id from view_column_definition where view_id =(select view_id from view_information where view_name = ?1) and column_name= ?2", nativeQuery = true)
    String findcolumnIdBycolumnName(String viewName, String columnName);

    @Query(value = "SELECT distinct view_id FROM catalog.rule_on_view WHERE dq_view_id =(select view_id from  view_information where view_name= ?1)", nativeQuery = true)
    List<Object[]> findProcessorSnapshotSourceViewIdByDqViewId(String viewName);

    @Query(value = "SELECT layer_id from catalog.view_layer where layer_name = ?1", nativeQuery = true)
    String findLayerIdByLayerName(String layerName);

    @Modifying
    @Query(value = "UPDATE catalog.view_information SET dq_enabled = ?2 WHERE view_name = ?1", nativeQuery = true)
    int updateDqEnabledValueForView(String viewName, int status);

    @Modifying
    @Query(value = "UPDATE catalog.view_information SET materialization_enabled = ?2 WHERE view_name = ?1", nativeQuery = true)
    int updateMaterializationEnabledValueForView(String viewName, int status);

    /* Query to find attributes of ViewDefinition entity by View Name */
    @Query(value = "select view_alias from view_source where view_id =?1" +
            " and source_view_id=" +
            "(select view_id from view_information where view_name=?2) and view_alias=?3", nativeQuery = true)
    List<String> findAllAliasesForViewNameSourceName(String viewID, String sourceName, String alias);

    @Modifying
    @Query(value = "DELETE FROM catalog.view_information WHERE view_id IN ?1", nativeQuery = true)
    void deleteByViewIds(List<String> viewIdList);

    @Modifying
    @Query(value = "UPDATE catalog.view_information SET is_active = ?2 WHERE view_name = ?1", nativeQuery = true)
    void updateIsActiveValueForView(String viewName, int isActive);

    @Query(value = "select view_name,def_client_view_map from view_information where view_group=(select id from view_group where name = ?1)", nativeQuery = true)
    List<Object[]> findByViewGroup(String viewGroupId);

    @Query(value = "SELECT DISTINCT view_name,a.view_id,schedule_start_time,schedule_end_time,granularity,cron_string,a.exporter_id FROM (SELECT * FROM `client_view_exporter_mapping` WHERE exporter_id=?2 AND client_id=?1 AND is_active=1)a LEFT JOIN (SELECT view_id,view_name,schedule_start_time,schedule_end_time,is_active FROM `view_information` WHERE is_active=1)b ON a.view_id=b.view_id LEFT JOIN (SELECT view_id,client_info_id,granularity,cron_string,is_active FROM `view_output_granularity` WHERE is_active=1)c ON a.view_id=c.view_id AND a.client_id=c.client_info_id WHERE view_name IS NOT NULL AND a.view_id IS NOT NULL", nativeQuery = true)
    List<Object[]> findViewByClientAndExporterType(String clientId, String exporterType);

    @Query(value = "SELECT config_key,config_value FROM `exporter_configuration` WHERE exporter_id=?1", nativeQuery = true)
    List<Object[]> findExporterConfig(String exporterType);

    @Query(value = "SELECT info_name FROM `client_information` WHERE info_id=?1",nativeQuery = true)
    String getClientName(String clientId);
}
