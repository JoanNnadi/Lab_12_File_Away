# Lab 12: File Away — Student Record Entry System

A Java command-line utility for collecting, validating, exporting, reading, and editing student records in `.csv` or `.txt` formats.

## Key Features
* **Validated User Input:** Enforces non-empty fields, year-of-birth range checks (1900–2025), and strict `.csv`/`.txt` file extensions.
* **File Export & Reading:** Writes formatted records to disk and uses `JFileChooser` to load files back into memory.
* **Dynamic ASCII Table & Editing:** Automatically calculates column widths to render clean terminal tables and allows inline row editing.
