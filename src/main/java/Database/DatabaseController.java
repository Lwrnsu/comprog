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

                String columnSql = "SELECT exam, totalExam, activity, totalActivity, pTask, totalPTask FROM " + tableName + ";";
                var columnStmt = conn.createStatement();
                var columnRs = columnStmt.executeQuery(columnSql);

                while (columnRs.next()) {
                    String exam = String.valueOf(columnRs.getInt("exam"));
                    String totalExam = String.valueOf(columnRs.getInt("totalExam"));
                    String act = String.valueOf(columnRs.getInt("activity"));
                    String totalAct = String.valueOf(columnRs.getInt("totalActivity"));
                    String pTask = String.valueOf(columnRs.getInt("pTask"));
                    String totalPTask = String.valueOf(columnRs.getInt("totalPTask"));
                    activities.get(tableName).add(exam);
                    activities.get(tableName).add(totalExam);
                    activities.get(tableName).add(act);
                    activities.get(tableName).add(totalAct);
                    activities.get(tableName).add(pTask);
                    activities.get(tableName).add(totalPTask);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTable(String tableName) {

        var sql = "CREATE TABLE \"" + tableName + "\" ("
                + "	exam INTEGER, "
                + " totalExam INTEGER,"
                + " activity INTEGER,"
                + " totalActivity INTEGER,"
                + " pTask INTEGER,"
                + " totalPTask INTEGER,"
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

        public static void addActivities(String tableName, int exam, int totalExam, int activity, int totalActivity, int pTask, int totalPTask, String date) {

            String sql = "INSERT INTO \"" + tableName + "\" (exam, totalExam, activity, TotalActivity, pTask, totalPTask, created_at) VALUES(?,?,?,?,?,?,?);";

            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, exam);
                pstmt.setInt(2, totalExam);
                pstmt.setInt(3, activity);
                pstmt.setInt(4, totalActivity);
                pstmt.setInt(5, pTask);
                pstmt.setInt(6, totalPTask);
                pstmt.setString(7, date);
                pstmt.executeUpdate();
                connect();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void viewNames(String tableName) {

            String sql = "SELECT exam, activity, pTask, created_at FROM \"" + tableName + "\";";
            int i  = 1;

            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while(rs.next()) {
                    int exam = rs.getInt("exam");
                    int activity = rs.getInt("activity");
                    int pTask = rs.getInt("pTask");
                    String date = rs.getString("created_at");
                    System.out.println(i + ". "
                            + " | Exam: " + exam
                            + " | Activity: " + activity
                            + " | Performance Task: " + pTask
                            + " | Date: " + date);
                    i++;
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void updateActivity(String tableName,
                                          int updatedExam, int updatedActivity, int updatedPTask) {
            String sql = "UPDATE \"" + tableName
                    + "\" SET exam='" + updatedExam
                    + "', activity='" + updatedActivity
                    + "', pTask='" + updatedPTask
                    + "';";
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

        public static void calculate(String tableName, int rExam, int rAct, int rPTask) {
            String sql = "SELECT exam, totalExam, activity, totalActivity, pTask, totalPTask FROM " + tableName
                    + ";";
            try (var conn = DriverManager.getConnection(url);
                 var stmt = conn.prepareStatement(sql)) {
                var rs = stmt.executeQuery();

                while (rs.next()) {
                    int exam = rs.getInt("exam");
                    int totalExam = rs.getInt("totalExam");
                    int activity = rs.getInt("activity");
                    int totalActivity = rs.getInt("totalActivity");
                    int pTask = rs.getInt("pTask");
                    int totalPTask = rs.getInt("totalPTask");

                    double examCalc = ((double) exam/totalExam) * 100;
                    double actCalc = ((double)activity/totalActivity) * 100;
                    double pTaskCalc = ((double)pTask/totalPTask) * 100;
                    double finalGrade = (examCalc * ((double) rExam /100)) + (actCalc * ((double) rAct /100)) + (pTaskCalc * ((double) rPTask /100));

                    System.out.println("Computed Grades for: " + tableName);
                    System.out.printf("Exam: %.2f | Activity: %.2f | Performance Task: %.2f\n", examCalc, actCalc, pTaskCalc);
                    System.out.printf("Final Grade: %.2f\n", finalGrade);
                    System.out.println("Rounded off: " + Math.round(finalGrade));
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
}

