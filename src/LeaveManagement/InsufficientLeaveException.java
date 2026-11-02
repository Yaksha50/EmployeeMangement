package LeaveManagement;

public class InsufficientLeaveException extends Exception {
    private final int available;
    private final int requested;

    public InsufficientLeaveException(int available, int requested) {
        super("Insufficient leave balance.\nAvailable Leave: " + available
                + "\nRequested Leave: " + requested);
        this.available = available;
        this.requested = requested;
    }

    public int getAvailable() {
        return available;
    }

    public int getRequested() {
        return requested;
    }
}
