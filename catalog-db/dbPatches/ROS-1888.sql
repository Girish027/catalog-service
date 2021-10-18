ALTER TABLE `catalog`.`view_information`
ADD COLUMN `source_plugin_class` VARCHAR(45) NULL DEFAULT 'NULL' AFTER `view_group`;

CREATE TABLE `source_adapter_config` (
  `view_id` varchar(50) NOT NULL,
  `source_type` varchar(50) NOT NULL,
  `key_name` varchar(50) NOT NULL,
  `value_name` varchar(50) NOT NULL,
  `source_config_id` varchar(50) NOT NULL,
  PRIMARY KEY (`source_config_id`)
)