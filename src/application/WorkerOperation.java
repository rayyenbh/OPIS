package application;

public class WorkerOperation {
    private final String workerName;
    private final String operation;
    private final String diameter;
    private final int quantity;

    public WorkerOperation(String workerName, String operation, String diameter, int quantity) {
        this.workerName = workerName;
        this.operation = operation;
        this.diameter = diameter;
        this.quantity = quantity;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getOperation() {
        return operation;
    }

    public String getDiameter() {
        return diameter;
    }

    public int getQuantity() {
        return quantity;
    }
}
