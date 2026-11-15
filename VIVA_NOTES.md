# Viva Cheat Sheet

## Unit 1 — Classes and fundamentals
- `Donor`, `BloodUnit`, `Request`: constructors, fields and object creation.
- `Donor`: overloaded constructors for new and database-loaded donor objects.
- `Validator`: `String` and `Character` checks for names and phone numbers.
- `BloodGroup`: enum values for all eight blood groups.
- `DonorDAO.DonationEntry`: non-static inner class for a donation entry.
- `Request`: `StringBuffer` receipt generation and `Comparable` priority ordering.
- `StockChartPanel`: `Math.max` and arithmetic scaling for chart bars.
- `DonorDAO`, `HistoryDAO` and UI models: arrays and collections for records.

## Unit 2 — OOP principles
- `Person`: abstract parent class.
- `Donor` and `Recipient`: inheritance from `Person`.
- `BloodRecord`: abstract parent for `BloodUnit` and `Request`.
- `Screenable`: eligibility interface implemented by `Donor`.
- `Donor`: overrides role and eligibility methods.
- `bloodbank.core`: domain classes are grouped in a package.

## Unit 3 — Exceptions and threads
- `InvalidDonorException`, `InvalidRequestException`, `BloodNotAvailableException`: custom checked exceptions.
- `ExpiryMonitor`: background thread checks expiry every five seconds.
- `StockDAO` and `StockService`: synchronized stock mutation methods.
- Swing status updates from the monitor use `SwingUtilities.invokeLater`.
- DAO and UI methods use try-with-resources and catch/finally handling.

## Unit 4 — JDBC
- `DBConnection`: loads connection settings from `db.properties`.
- `DonorDAO`, `StockDAO`, `RequestDAO`, `HistoryDAO`: JDBC data access.
- DAOs use `PreparedStatement`, `ResultSet`, `executeQuery` and `executeUpdate`.
- `RequestDAO.approveWithStock`: transaction, row locks, commit and rollback.
- `StockDAO.recordDonation`: transaction updates stock, donation history and donor eligibility date together.

## Unit 5 — Swing
- `LoginFrame`, `DashboardFrame`: application frames and navigation.
- Panels use `BorderLayout`, `GridLayout` and `FlowLayout`.
- Forms use `JTextField`, `JButton`, `JComboBox`, `JSpinner` and `JTable`.
- Event handling uses action, mouse and key listeners.
- `StockChartPanel`: Java2D custom painting through `paintComponent`.
