# MrStock
> This project was developed as part of my coursework. No judgements please!

MrStock is a basic stock screening application written in Java. It uses JDBC for database connection, SQLite for database, and AWT for UI. It currently implements a dummy database, but it shall easily be integrated with the real-time API.
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![AWT](https://img.shields.io/badge/AWT-757575?style=for-the-badge&logo=java&logoColor=white)

## Features
- Stock search functionality
- View stock data (price, market cap, volume, etc.)
- Analyze financial metrics like PE ratio, debt, and cash flow.

## Getting Started

+ Download JDBC driver
```bash
https://github.com/xerial/sqlite-jdbc/releases
```

+ compile all files
```bash
javac -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" *.java
```

+ setup db
```bash
javac -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" setupDB.java
java -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" setupDB
```

+ Run app
```bash
java -cp ".;lib/sqlite-jdbc-3.49.1.0.jar" Main
```


## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is open-source and licensed under MIT. [View License](https://choosealicense.com/licenses/mit/)