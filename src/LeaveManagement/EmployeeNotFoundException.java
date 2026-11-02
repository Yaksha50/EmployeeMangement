 package LeaveManagement;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String employeeId) {
        super("Employee " + employeeId + " does not exist.");
    }
}
