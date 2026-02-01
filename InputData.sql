INSERT INTO department (id, name, description) VALUES
(gen_random_uuid(), 'IT', 'Technology Department'),
(gen_random_uuid(), 'HR', 'Human Resources');

----

INSERT INTO work_schedule (id, start_time, end_time, working_days) VALUES
(
 gen_random_uuid(),
 '09:00',
 '15:00',
 'TUESDAY,WEDNESDAY,THURSDAY,SATURDAY,SUNDAY'
);

----

INSERT INTO employee (id, first_name, last_name, email, password, role, active, department_id, schedule_id)
SELECT
 gen_random_uuid(),
 'John',
 'Doe',
 'john@company.com',
 '1234',
 'Developer',
 true,
 d.id,
 s.id
FROM department d, work_schedule s
WHERE d.name='IT'
LIMIT 1;

INSERT INTO employee (id, first_name, last_name, email, password, role, active, department_id, schedule_id)
SELECT
 gen_random_uuid(),
 'Anna',
 'Smith',
 'anna@company.com',
 '1234',
 'HR Manager',
 true,
 d.id,
 s.id
FROM department d, work_schedule s
WHERE d.name='HR'
LIMIT 1;


----

-- John Doe - días normales
INSERT INTO punch (id, employee_id, punch_date, punch_time, type, out_of_schedule)
SELECT
 gen_random_uuid(),
 e.id,
 '2025-12-03',
 '09:00',
 'CHECK_IN',
 false
FROM employee e WHERE email='john@company.com';

INSERT INTO punch VALUES
(gen_random_uuid(), (SELECT id FROM employee WHERE email='john@company.com'), '2025-12-03', '12:00', 'BREAK_START', false),
(gen_random_uuid(), (SELECT id FROM employee WHERE email='john@company.com'), '2025-12-03', '12:30', 'BREAK_END', false),
(gen_random_uuid(), (SELECT id FROM employee WHERE email='john@company.com'), '2025-12-03', '16:30', 'CHECK_OUT', true);


