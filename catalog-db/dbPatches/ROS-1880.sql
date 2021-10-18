ALTER TABLE `catalog`.`view_information`
ADD COLUMN `processor_plugin_type` ENUM('SQL', 'SCALA') NULL DEFAULT 'SQL' AFTER `is_dynamicsql`,
ADD COLUMN `processor_plugin_main_class` VARCHAR(256) NULL DEFAULT NULL AFTER `processor_plugin_type`,
ADD COLUMN `processor_plugin_jar_loc` VARCHAR(256) NULL DEFAULT NULL AFTER `processor_plugin_main_class`;