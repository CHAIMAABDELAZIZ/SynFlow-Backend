-- Drop existing daily_reports table if it exists
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE daily_reports CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

-- Create daily_reports table
CREATE TABLE daily_reports (
    id NUMBER(19) PRIMARY KEY,
    report_name VARCHAR2(255) NOT NULL,
    report_date DATE NOT NULL,
    puit_id NUMBER(19) NOT NULL,
    current_phase_id NUMBER(19),
    current_depth NUMBER(10,2),
    lithology VARCHAR2(500),
    daily_cost NUMBER(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create sequence for primary key
CREATE SEQUENCE daily_reports_seq START WITH 1 INCREMENT BY 1;

-- Create trigger for auto-increment
CREATE OR REPLACE TRIGGER daily_reports_trigger
BEFORE INSERT ON daily_reports
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT daily_reports_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

-- Add foreign key constraints
ALTER TABLE daily_reports 
ADD CONSTRAINT fk_daily_reports_puit 
    FOREIGN KEY (puit_id) REFERENCES puits(id);

ALTER TABLE daily_reports 
ADD CONSTRAINT fk_daily_reports_phase 
    FOREIGN KEY (current_phase_id) REFERENCES phases(id);

-- Add unique constraint to prevent duplicate reports for same well on same date
ALTER TABLE daily_reports 
ADD CONSTRAINT uk_daily_reports_puit_date 
    UNIQUE (puit_id, report_date);

-- Add indexes for better performance
CREATE INDEX idx_daily_reports_puit_id ON daily_reports(puit_id);
CREATE INDEX idx_daily_reports_date ON daily_reports(report_date);
CREATE INDEX idx_daily_reports_phase_id ON daily_reports(current_phase_id);

-- Add daily_report_id column to operations table if it doesn't exist
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE operations ADD daily_report_id NUMBER(19)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1430 THEN -- column already exists
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

-- Add foreign key constraint for operations
ALTER TABLE operations 
ADD CONSTRAINT fk_operations_daily_report 
    FOREIGN KEY (daily_report_id) REFERENCES daily_reports(id);

-- Add index for operations daily_report_id
CREATE INDEX idx_operations_daily_report_id ON operations(daily_report_id);

-- Add daily_report_id column to indicateurs table if it doesn't exist
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE indicateurs ADD daily_report_id NUMBER(19)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -1430 THEN -- column already exists
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

-- Add foreign key constraint for indicateurs
ALTER TABLE indicateurs 
ADD CONSTRAINT fk_indicateurs_daily_report 
    FOREIGN KEY (daily_report_id) REFERENCES daily_reports(id);

-- Add index for indicateurs daily_report_id
CREATE INDEX idx_indicateurs_daily_report_id ON indicateurs(daily_report_id);

-- Add comments to document the table structure
COMMENT ON TABLE daily_reports IS 'Daily drilling reports containing well status and activities';
COMMENT ON COLUMN daily_reports.report_name IS 'Name/title of the daily report';
COMMENT ON COLUMN daily_reports.report_date IS 'Date of the report';
COMMENT ON COLUMN daily_reports.puit_id IS 'Reference to the well (puit) this report concerns';
COMMENT ON COLUMN daily_reports.current_phase_id IS 'Current drilling phase of the well';
COMMENT ON COLUMN daily_reports.current_depth IS 'Current drilling depth in meters';
COMMENT ON COLUMN daily_reports.lithology IS 'Description of rock formations encountered';
COMMENT ON COLUMN daily_reports.daily_cost IS 'Total cost for the day';

COMMIT;
