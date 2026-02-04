# 🕒 ERP Attendance & Time-Tracking System

A professional full-stack solution for employee attendance management. This system features automated **overtime calculation**, **absence tracking**, and **dynamic performance analytics** based on a fixed corporate schedule.



## 🚀 Key Features

* **Smart Attendance Logic**: Prevents operational errors such as clocking out before clocking in or opening a break without an active session.
* **Fixed Schedule Management**:
    * **Core Hours**: Tuesday to Sunday (09:00 - 15:00).
    * **Automatic Overtime**: Hours worked on Mondays, Fridays, or exceeding 6 hours on standard days are automatically flagged as "Extra Hours".
* **Absence & Punctuality Tracking**: 
    * Automatically identifies missing logs on mandatory workdays.
    * Flags "Lateness" for entries recorded after the 10-minute grace period (09:10).
* **Dynamic Analytics Dashboard**: Real-time filtering (Day, Week, Month, Year, All) that updates all metrics, including absences and effective work hours.

---

## 🛠️ Tech Stack

* **Backend**: Java 17+, Spring Boot, Spring Data JPA.
* **Frontend**: HTML5, JavaScript (Vanilla ES6+), Bootstrap 5.
* **Database**: H2 (In-memory for development).

---

## 📋 Business Rules

| Rule | Description |
| :--- | :--- |
| **Mandatory Days** | Tuesday, Wednesday, Thursday, Saturday, Sunday. |
| **Overtime Days** | Monday and Friday (All hours are counted as extra). |
| **Standard Shift** | 09:00 to 15:00. |
| **Grace Period** | 10 minutes (Lateness starts at 09:11). |
| **Single Session** | Only one Clock-In/Out cycle is allowed per day to ensure data integrity. |

---

## 🔌 API Endpoints Reference

The backend exposes a RESTful API for seamless integration:

* **GET** `/api/departments`: Returns all available departments.
* **GET** `/api/employees/{deptId}`: Fetches employees by department.
* **POST** `/api/punch`: Records timestamps (In/Out/Breaks). Requires `empId`, `pass`, and `action`.
* **GET** `/api/stats/{empId}`: Returns 2-month general averages and metrics.
* **GET** `/api/stats/dynamic/{empId}?range={range}`: Provides filtered stats for specific timeframes.



---

## 🚀 Future Roadmap

* **Geofencing**: Implement GPS validation for mobile clock-ins.
* **RBAC Security**: Integration of Spring Security for Admin/Employee role separation.
* **Automated Alerts**: Email notifications for employees with high absence rates.
* **Export Engine**: PDF/Excel report generation for payroll processing.

---

## 👤 Author

* **Ignacio Gamallo Lafon** - *Full Stack Development & System Architecture*
* **LinkedIn**: [https://www.linkedin.com/in/ignacio-gamallo-lafon-43450b312/]
* **GitHub**: [https://github.com/NachoGamallo]

---

## 📝 License

This project is licensed under the MIT License.
