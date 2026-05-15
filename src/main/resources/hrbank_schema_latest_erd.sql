-- HR Bank schema.sql
-- 기준:
-- - 최신 ERD 기준
-- - id는 UUID
-- - employee_change_logs는 employees FK 없음
-- - employee_change_diffs만 employee_change_logs를 FK로 참조
-- - employees.profile_image_id, backup_histories.file_id는 files를 참조
-- - profile_image_id/file_id는 nullable
DROP TABLE IF EXISTS employee_change_diffs CASCADE;
DROP TABLE IF EXISTS employee_change_logs CASCADE;
DROP TABLE IF EXISTS backup_histories CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

CREATE TABLE departments (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    established_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE files (
    id UUID PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    storage_path VARCHAR(500) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE employees (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    employee_number VARCHAR(50) NOT NULL UNIQUE,
    department_id UUID NOT NULL,
    profile_image_id UUID,
    position VARCHAR(100) NOT NULL,
    hire_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),

    CONSTRAINT fk_employees_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id),

    CONSTRAINT fk_employees_profile_image
        FOREIGN KEY (profile_image_id)
        REFERENCES files(id),

    CONSTRAINT uk_employees_profile_image
            UNIQUE (profile_image_id)
);

CREATE TABLE employee_change_logs (
    id UUID PRIMARY KEY,
    type VARCHAR(30) NOT NULL,
    employee_number VARCHAR(50) NOT NULL,
    memo TEXT,
    ip_address VARCHAR(50) NOT NULL,
    at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE employee_change_diffs (
    id UUID PRIMARY KEY,
    change_log_id UUID NOT NULL,
    property_name VARCHAR(100) NOT NULL,
    before_value TEXT,
    after_value TEXT,

    CONSTRAINT fk_employee_change_diffs_change_log
        FOREIGN KEY (change_log_id)
        REFERENCES employee_change_logs(id)
        ON DELETE CASCADE
);

CREATE TABLE backup_histories (
    id UUID PRIMARY KEY,
    worker VARCHAR(100) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    ended_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    status VARCHAR(30) NOT NULL,
    file_id UUID,

    CONSTRAINT fk_backup_histories_file
        FOREIGN KEY (file_id)
        REFERENCES files(id),

    CONSTRAINT uk_backup_histories_file
        UNIQUE (file_id)
);

