ALTER TABLE `catalog`.`view_information`
ADD COLUMN `load_strategy` ENUM('STATIC', 'RELATIVE') NOT NULL DEFAULT 'RELATIVE';

ALTER TABLE `catalog`.`view_output_granularity`
ADD COLUMN `load_strategy` ENUM('STATIC', 'RELATIVE') NULL;
