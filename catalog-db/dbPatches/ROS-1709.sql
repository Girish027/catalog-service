ALTER TABLE view_output_granularity ADD sla_breach_time INT DEFAULT -1;
ALTER TABLE view_output_granularity ADD OWNER VARCHAR(100) NOT NULL;
ALTER TABLE view_output_granularity ADD owner_email_id VARCHAR(100);
