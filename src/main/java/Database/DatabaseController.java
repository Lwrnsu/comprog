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
                    Integer exam = (Integer) columnRs.getObject("exam");
                    Integer totalExam = (Integer) columnRs.getObject("totalExam");
                    Integer act = (Integer) columnRs.getObject("activity");
                    Integer totalAct = (Integer) columnRs.getObject("totalActivity");
                    Integer pTask = (Integer) columnRs.getObject("pTask");
                    Integer totalPTask = (Integer) columnRs.getObject("totalPTask");
                    if (exam != null) activities.get(tableName).add(String.valueOf(exam));
                    if (totalExam != null) activities.get(tableName).add(String.valueOf(totalExam));
                    if (act != null) activities.get(tableName).add(String.valueOf(act));
                    if (totalAct != null) activities.get(tableName).add(String.valueOf(totalAct));
                    if (pTask != null) activities.get(tableName).add(String.valueOf(pTask));
                    if (totalPTask != null) activities.get(tableName).add(String.valueOf(totalPTask));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTable(String tableName) {

        var sql = "CREATE TABLE IF NOT EXISTS \"" + tableName + "\" ("
                + "	exam INTEGER, "
                + " totalExam INTEGER,"
                + " activity INTEGER,"
                + " totalActivity INTEGER,"
                + " pTask INTEGER,"
                + " totalPTask INTEGER,"
                + " rubrics_exam INTEGER,"
                + " rubrics_activity INTEGER,"
                + " rubrics_pTask INTEGER,"
                + " created_at text NOT NULL"
                + " )";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            connect();
            System.out.println("Table created successfully: " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        public static void addActivities(String tableName, int exam, int totalExam, int activity, int totalActivity, int pTask, int totalPTask) {

            String sql = "UPDATE \"" + tableName
                    + "\" SET exam='" + exam
                    + "', totalExam='" + totalExam
                    + "', activity='" + activity
                    + "', totalActivity='" + totalActivity
                    + "', pTask='" + pTask
                    + "', totalPTask='" +totalPTask
                    + "';";

            try (var conn = DriverManager.getConnection(url);
                 var pstmt = conn.prepareStatement(sql)) {
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
                pstmt.executeUpdate();
                connect();
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

        public static void calculate(String tableName) {
            String sql = "SELECT exam, totalExam, activity, totalActivity, " +
                    "pTask, totalPTask, rubrics_exam, rubrics_activity, " +
                    "rubrics_pTask FROM " + tableName
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
                    int rExam = rs.getInt("rubrics_exam");
                    int rAct = rs.getInt("rubrics_activity");
                    int rPTask = rs.getInt("rubrics_pTask");

                    double examCalc = (totalExam == 0) ? 0 : ((double) exam / totalExam) * 100;
                    double actCalc = (totalActivity == 0) ? 0 : ((double) activity / totalActivity) * 100;
                    double pTaskCalc = (totalPTask == 0) ? 0 : ((double) pTask / totalPTask) * 100;

                    double finalGrade = (examCalc * ((double) rExam / 100)) +
                            (actCalc * ((double) rAct / 100)) +
                            (pTaskCalc * ((double) rPTask / 100));

                    System.out.println("Computed Grades for: " + tableName);
                    System.out.printf("Exam: %.2f | Activity: %.2f | Performance Task: %.2f\n",
                            examCalc, actCalc, pTaskCalc);
                    System.out.printf("Final Grade: %.2f\n", finalGrade);
                    System.out.println("Rounded off: " + Math.round(finalGrade));
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        public static void addSubject(String tableName, int rExam, int rAct, int rPTask, String date) {

            String sql = "INSERT INTO \"" + tableName + "\" (rubrics_exam, rubrics_activity, rubrics_pTask, created_at) VALUES(?,?,?,?);";

            try (var conn = DriverManager.getConnection(url);
                 var stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, rExam);
                stmt.setInt(2, rAct);
                stmt.setInt(3, rPTask);
                stmt.setString(4, date);
                stmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }


}

