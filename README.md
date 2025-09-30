# Coficab Production Tracker  
JavaFX desktop app that lets supervisors and operators log, track and export daily cable-processing operations (formage, glaçage, ajustage, etc.) per worker.

---

## Features
| Role | Capabilities |
|------|--------------|
| **Operator** | Add/remove workers, record diameters per operation, view monthly presence counter, add remarks. |
| **Supervisor** | Global dashboard of all workers, total quantities per operation, remarks, export diameter/quantity lists by date. |

---

## Screens
1. **Home** – choose Operator or Supervisor.  
2. **Workers List** – add/delete workers (persisted in `workers.txt`).  
3. **Operator Sheet** – live entry of diameters (Φ) and automatic quantity counter; data saved to `~/Desktop/operations_<worker>.txt`.  
4. **Supervisor Dashboard** – read-only view of every worker’s operations and remarks.  
5. **Export Tool** – manually export any diameter + quantity to `~/Desktop/exported_data.txt` with date stamp.

---

## Tech Stack
* Java 17+  
* JavaFX 21+ (OpenJFX)  
* Plain-text files for storage (no DB required)  
* Maven or IDE-run (no installer yet)

---

## Quick Start
1. Clone / unzip the project.
2. Place the required images in  
   `C:\Users\Admin\Downloads\` **(or edit paths in FXML & controllers)**.  
3. Compile & run `application.Main`.
4. Choose “Opérateur” to start recording or “Chef d’équipe” to inspect.

---

## File Layout
project-root
├─ src
│  └─ application
│     ├─ Main.java
│     ├─ Page1Controller.java  … Page5Controller.java
│     └─ *.fxml
├─ workers.txt                  (auto-created)
└─ README.md

Data files are written to the **Desktop** of the user running the app.

---

## Known Limitations
* Image paths are **absolute Windows paths** – edit before deploying elsewhere.  
* Worker names can be duplicated – planned fix: unique ID.  
* Remarks are appended, not overwritten – planned fix: in-place update.  
* No authentication – anyone with access can open supervisor view.

---

## Road-map / Contributions Welcome
* [ ] Replace text files with SQLite.  
* [ ] Bundle images inside JAR (relative paths).  
* [ ] PDF or Excel export instead of txt.  
* [ ] Login screen with role check.  
* [ ] Unit & integration tests.

---

## License
MIT – feel free to fork and adapt for your own shop-floor needs.
