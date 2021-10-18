ALTER TABLE `catalog`.`view_group` ADD COLUMN `environment_properties` VARCHAR(1000) NULL AFTER `modified_by`;
UPDATE `catalog`.`view_group` SET environment_properties='{}';

ALTER TABLE `catalog`.`view_information` ADD COLUMN `environment_properties` VARCHAR(1000) NULL AFTER `def_client_view_map`;
UPDATE `catalog`.`view_information` SET environment_properties='{}';

ALTER TABLE `catalog`.`view_output_granularity` ADD COLUMN `environment_properties` VARCHAR(1000) NULL AFTER `load_strategy`;
UPDATE `catalog`.`view_group` SET environment_properties='{}';

ALTER TABLE `orchestrator`.`views` ADD COLUMN `data_center` VARCHAR(25) NULL AFTER `date_modified`;
UPDATE `orchestrator`.`view_group` SET data_center='va1';

