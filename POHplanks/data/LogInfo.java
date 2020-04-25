package scripts.POHplanks.data;

public enum LogInfo {

    NORMAL(960, 1511, "Logs", 100),
    OAK(8778,1521, "Oak logs", 250),
    TEAK(8780,6333,"Teak logs", 500),
    MAHOGANY(8782,6332, "Mahogany logs", 1500);

    private int Id;
    private int logId;
    private String logName;
    private int cost;

    LogInfo(int id, int logId, String logName, int cost) {
        this.Id = id;
        this.logId = logId;
        this.logName = logName;
        this.cost = cost;
    }
    public int getId() {
        return Id;
    }

    public int getLogId() {
        return logId;
    }

    public String getLogName() {
        return logName;
    }

    public int getCost() {
        return cost;
    }
}
