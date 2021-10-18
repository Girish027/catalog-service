alter table client_information add constraint uc_client unique(info_name,info_type,parent_info_id);
alter table view_information add column def_client_view_map varchar(5000) default '';
alter table client_view_exporter_mapping add constraint uc_exportermapping unique(client_id,view_id,exporter_id);
insert into view_group values('viewGroup_0068e341-0360-4426-8aa5-4tc689d4951d', 'AIVA', 'AIVA', 'Insights', 'AIVA', '{\"queue\": \"LongRun\"}', NULL, NULL, NULL, NULL);
update view_information set view_group ='viewGroup_0068e341-0360-4426-8aa5-4tc689d4951d' where view_name in ('Resolution_Raw_Report_Digital_Summary','Resolution_Raw_Report_Digital','View_AIVA_Transaction_Report','Resolution_Raw_Report_Digital_Scala');
commit;