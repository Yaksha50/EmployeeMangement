package LeaveManagement;

public abstract class Employee {
    private String employeeId;
    private String employeeName;
    private String department;
    private String email;
    private int availableLeaveBalance;

    // protected: accessible to subclasses
    protected String employeeType;

    public Employee() {
        this("", "", "", "", 0);
    }

    public Employee(String employeeId, String employeeName, String department,
                    String email, int availableLeaveBalance) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.email = email;
        this.availableLeaveBalance = availableLeaveBalance;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAvailableLeaveBalance() { return availableLeaveBalance; }
    public void setAvailableLeaveBalance(int availableLeaveBalance) {
        this.availableLeaveBalance = availableLeaveBalance;
    }

    // Overloaded methods
    public void deductLeave(int days) {
        this.availableLeaveBalance -= days;
    }

    public void deductLeave(int days, LeaveType type) {
        deductLeave(days);
    }

    // Overloaded display methods
    public void displayDetails() {
        System.out.println("Employee ID : " + employeeId);
        System.out.println("Employee Name : " + employeeName);
        System.out.println("Department : " + department);
        System.out.println("Email : " + email);
        System.out.println("Leave Balance : " + availableLeaveBalance);
    }
 
     public void displayDetails(String title) {
    	 System.out.println("------"+ title +"------");
    	 displayDetails();
 
    }

    // Overridden by subclasses
    public abstract String getEmployeeType();
}
