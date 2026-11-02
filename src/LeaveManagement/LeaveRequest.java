package LeaveManagement;

public class LeaveRequest {
    private static int counter = 1000;

    private final String requestId;
    private final String employeeId;
    private final LeaveType leaveType;
    private final int numberOfDays;
    private final String reason;
    private LeaveStatus status;

    public LeaveRequest(String employeeId, LeaveType leaveType, int numberOfDays, String reason) {
        this.requestId = "LR" + (++counter);
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
    }

    public String getRequestId() { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getReason() { return reason; }
    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }

    public void displayDetails() {
        System.out.println("Request ID : " + requestId);
        System.out.println("Employee ID : " + employeeId);
        System.out.println("Leave Type : " + leaveType);
        System.out.println("Days : " + numberOfDays);
        System.out.println("Reason : " + reason);
        System.out.println("Status : " + status);
    }
}
