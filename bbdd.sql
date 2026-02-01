CREATE DATABASE fichajes
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

---

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE department (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE work_schedule (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    working_days VARCHAR(100) NOT NULL
);

CREATE TABLE employee (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    department_id UUID REFERENCES department(id),
    schedule_id UUID REFERENCES work_schedule(id)
);

CREATE TABLE punch (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    employee_id UUID REFERENCES employee(id),
    punch_date DATE,
    punch_time TIME,
    type VARCHAR(30),
    out_of_schedule BOOLEAN
);

CREATE INDEX idx_punch_employee_date ON punch(employee_id, punch_date);

