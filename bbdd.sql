CREATE DATABASE "fichajesERP"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Create Tables
CREATE TABLE departments (
    id SERIAL PRIMARY KEY, 
    name VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY, 
    full_name VARCHAR(100), 
    position VARCHAR(50), 
    password VARCHAR(20), 
    department_id INT REFERENCES departments(id)
);

CREATE TABLE time_records (
    id SERIAL PRIMARY KEY, 
    employee_id INT REFERENCES employees(id), 
    action_type VARCHAR(20), 
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Initial Data
INSERT INTO departments (name) VALUES ('IT'), ('Sales');

INSERT INTO employees (full_name, position, password, department_id) VALUES 
('Alice Smith', 'Lead Dev', '1234', 1), 
('Bob Miller', 'Junior Dev', '1234', 1),
('Charlie Day', 'Manager', '1234', 2), 
('Diana Prince', 'Executive', '1234', 2);

-- HISTORIAL DE DICIEMBRE 2025
-- Día 2 (Martes): Jornada normal de 6h
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2025-12-02 09:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2025-12-02 15:00:00');

-- Día 3 (Miércoles): Con descanso de 30 min (Trabaja 6h efectivas, sale 15:30)
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2025-12-03 09:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_IN', '2025-12-03 11:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_OUT', '2025-12-03 11:30:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2025-12-03 15:30:00');

-- Día 5 (Viernes): FUERA DE HORARIO (Todo cuenta como Extra)
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2025-12-05 10:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2025-12-05 14:00:00');

-- HISTORIAL DE ENERO 2026
-- Día 13 (Martes): IMPUNTUALIDAD (Entra 09:15) y HORAS EXTRA (>6h)
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2026-01-13 09:15:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2026-01-13 17:15:00');

-- Día 14 (Miércoles): Jornada normal
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2026-01-14 09:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2026-01-14 15:00:00');

-- Día 15 (Jueves): Muchos descansos
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2026-01-15 08:55:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_IN', '2026-01-15 10:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_OUT', '2026-01-15 10:45:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_IN', '2026-01-15 13:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'BREAK_OUT', '2026-01-15 13:15:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2026-01-15 16:00:00');

-- Día 25 (Domingo): Trabajo en fin de semana (Día laboral según requisito)
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_IN', '2026-01-25 09:00:00');
INSERT INTO time_records (employee_id, action_type, timestamp) VALUES (1, 'CLOCK_OUT', '2026-01-25 15:00:00');
