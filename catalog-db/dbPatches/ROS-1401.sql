ALTER TABLE catalog.view_information
ADD COLUMN view_group VARCHAR(50) NOT NULL;

ALTER TABLE catalog.view_output_granularity
ADD COLUMN execution_properties VARCHAR(10000);



INSERT INTO `view_group` (`id`,`name`,`description`,`owner`,`product`,`execution_properties`,`created_unixtimestamps`,`modified_unixtimestamps`,`created_by`,`modified_by`) VALUES ('viewGroup_725f1f2b-8ad5-4d8b-998f-5a53fde7a83b','IVR2Chat','test','test','test','{\"queue\": \"LongRun\"}',1542772587000,NULL,'system',NULL);
INSERT INTO `view_group` (`id`,`name`,`description`,`owner`,`product`,`execution_properties`,`created_unixtimestamps`,`modified_unixtimestamps`,`created_by`,`modified_by`) VALUES ('viewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944','InsightsRevOpt','test','test','test','{\"queue\": \"LongRun\"}',1542772587000,NULL,'system',NULL);
INSERT INTO `view_group` (`id`,`name`,`description`,`owner`,`product`,`execution_properties`,`created_unixtimestamps`,`modified_unixtimestamps`,`created_by`,`modified_by`) VALUES ('viewGroup_c53f2ae1-8829-4e7d-afdc-a90317995b26','Default','Default','Default','Default','{\"queue\": \"default\"}',1542772587000,NULL,'system',NULL);
update view_information set view_group ='viewGroup_c53f2ae1-8829-4e7d-afdc-a90317995b26';
update view_information set view_group ='viewGroup_909d79cf-a8a9-4d18-98f0-f8fccd4fd944' where view_name='View_DCF';
update view_information set view_group ='viewGroup_725f1f2b-8ad5-4d8b-998f-5a53fde7a83b' where view_name like 'View_IVR2Chat%';
commit;