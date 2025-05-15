# 🌤️ Weather Data Analyzer

A Java Swing-based application that allows users to analyze weather data from CSV files. The app provides visual summaries of daily high and low temperatures, average and median temperatures, and supports date-based filtering for more specific analysis.

---

## 📌 Features

- ✅ Modern graphical user interface (GUI) using **Java Swing**
- 📂 Load and read weather data from `.csv` files
- 📈 Automatically calculates:
  - Highest temperature
  - Lowest temperature
  - Average of daily averages
  - **Median** of daily averages (via dynamic array storage)
- 🗓️ Filter by specific month or date range
- 💾 Save weather summaries to a `.txt` file
- 🎨 Custom splash screen and stylized components

---

## 📁 File Format

The application expects a **CSV file** in the following format:

```csv
Date,High,Low
2024-01-01,55.0,38.2
2024-01-02,53.6,36.9
...
```

- **Date** in `YYYY-MM-DD` format
- **High/Low** temperatures as decimal values (Fahrenheit)

---

## 🖥️ How It Works

1. **Launch the App**  
   - Displays a custom splash screen for 4 seconds
   - Opens the main window with options

2. **Open Weather File**  
   - Click “Open Weather Data File”
   - Select a `.csv` file
   - Analysis is displayed in the main window and can be saved

3. **Filter Weather Data**  
   - Click “Filter Data”
   - Enter a month (`YYYY-MM`) or date range (`YYYY-MM-DD to YYYY-MM-DD`)
   - Select a file and view the filtered result

4. **Save Output**
   - After analysis, click “Save Summary” to export results to a `.txt` file

---

## ⚙️ Technologies Used

| Tool        | Purpose                         |
|-------------|----------------------------------|
| Java        | Primary programming language     |
| Swing       | GUI components (JFrame, JTextArea, etc.) |
| AWT         | Advanced graphics (custom splash, colors, layout) |
| Scanner     | Reading input from CSV files     |
| ArrayList   | Storing average temperatures      |
| Collections | Sorting for median calculation    |

---

## 📚 Learning Outcomes

This project demonstrates your understanding of:

- GUI design and event handling in Java
- File I/O (reading `.csv`, writing `.txt`)
- Using arrays and `ArrayList` for data processing
- Sorting and median calculations
- Custom graphics rendering and layout management

---

## 🧠 How to Run

1. Compile:
   ```bash
   javac Weather.java
   ```

2. Run:
   ```bash
   java Weather
   ```

> Note: Ensure the `icons/open.png` file exists in your `resources/icons/` directory if using the icon loading feature.

---

## 📎 Future Enhancements (Optional Ideas)

- Add support for CSV files with headers in different orders
- Add standard deviation or chart generation
- Support for metric (Celsius) conversion
- Save filtered and full datasets separately

---

## 🧑‍💻 Author

**Tamarius Jones**  
Built as part of a university project for learning Java GUI development and data analysis techniques.
