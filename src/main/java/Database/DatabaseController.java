package Database;

import java.sql.*;
import java.util.*;
public class DatabaseController {

    private static final String url = "jdbc:sqlite:user.db";
    public static List<String> tables = new ArrayList<>();
    public static TreeMap<String, List<String>> activities = new TreeMap<>();

    public static void connect() {
        tables.clear();
        activities.clear();
        try (var conn = DriverManager.getConnection(url)) {
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'";
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql);

            var meta = conn.getMetaData();
            System.out.println("SQLite is connected: " + meta.getDriverName());

            while (rs.next()) {
                String tableName = rs.getString("name");
                tables.add(tableName);

                activities.put(tableName, new ArrayList<>());

                String columnSql = "SELECT name FROM " + tableName + ";";
                var columnStmt = conn.createStatement();
                var columnRs = columnStmt.executeQuery(columnSql);

                while (columnRs.next()) {
                    String columnValue = columnRs.getString("name");
                    activities.get(tableName).add(columnValue);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTable(String tableName) {

        var sql = "CREATE TABLE \"" + tableName + "\" ("
                + "	name text NOT NULL, "
                + "	exam INTEGER, "
                + " activity INTEGER,"
                + " pTask INTEGER,"
                + " created_at text NOT NULL"
                + " );";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            connect();
            System.out.println("Table created successfully: " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        public static void addActivities(String tableName, String name, int score, String date) {

            String sql = "INSERT INTO \"" + tableName + "\" (name, score, created_at) VALUES(?,?,?);";

            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, score);
                pstmt.setString(3, date);
                pstmt.executeUpdate();
                connect();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void viewActivities(String tableName) {

            String sql = "SELECT name, score, created_at FROM \"" + tableName + "\";";
            int i  = 1;

            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while(rs.next()) {
                    String name = rs.getString("name");
                    int score = rs.getInt("score");
                    String date = rs.getString("created_at");
                    System.out.println(i + ". " + "Activity Name: " + name + " | Score: " + score + " | Date: " + date);
                    i++;
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void deleteActivity(String tableName, String name) {
            String sql = "DELETE FROM \"" + tableName + "\" WHERE name='" + name + "';";
            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {
                    pstmt.execute();
                    connect();
                    System.out.println("\nSuccessfully Deleted.");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void updateActivity(String tableName, String updatedName, String originalName,
                                          int updatedScore) {
            String sql = "UPDATE \"" + tableName + "\" SET name='" + updatedName
                    + "', score='" + updatedScore + "'" + " WHERE name='"
                    + originalName + "';";
            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {
                pstmt.execute();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void dropSubject(String tableName) {
            String sql = "DROP TABLE \"" + tableName + "\";";
            try (var conn = DriverManager.getConnection(url);
                 var stmt = conn.prepareStatement(sql)) {
                stmt.execute();
                connect();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
}

