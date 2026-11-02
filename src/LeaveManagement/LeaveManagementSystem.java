package LeaveManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class LeaveManagementSystem {

    private final List<Employee> employees = new ArrayList<>();
    private final Set<String> emails = new HashSet<>();
    private final Map<String, Employee> employeeMap = new HashMap<>();
    private final List<LeaveRequest> leaveRequests = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    // ---------- Input helpers ----------
    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        String input = readString(prompt);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }

    // ---------- Core operations ----------
    private Employee findEmployee(String employeeId) throws EmployeeNotFoundException {
        Employee emp = employeeMap.get(employeeId.toUpperCase());
        if (emp == null) {
            throw new EmployeeNotFoundException(employeeId.toUpperCase());
        }
        return emp;
    }

    private LeaveRequest findRequest(String requestId) {
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getRequestId().equalsIgnoreCase(requestId)) {
                return lr;
            }
        }
        throw new IllegalArgumentException("Leave request " + requestId.toUpperCase() + " does not exist.");
    }

    private void addEmployee() {
        String id = readString("Enter Employee ID: ").toUpperCase();
        String name = readString("Enter Employee Name: ");
        String dept = readString("Enter Department: ");
        String email = readString("Enter Email: ");
        int balance = readInt("Enter Leave Balance: ");
        String type = readString("Enter Employee Type (Permanent/Contract): ");

        if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
            throw new IllegalArgumentException("ID, name and department cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Leave balance cannot be negative.");
        }
        if (employeeMap.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate employee ID: " + id);
        }
        if (emails.contains(email.toLowerCase())) {
            throw new IllegalArgumentException("Duplicate email: " + email);
        }

        Employee emp;   // polymorphism: parent reference, child object
        if (type.equalsIgnoreCase("Permanent")) {
            emp = new PermanentEmployee(id, name, dept, email, balance);
        } else if (type.equalsIgnoreCase("Contract")) {
            emp = new ContractEmployee(id, name, dept, email, balance);
        } else {
            throw new IllegalArgumentException("Employee type must be Permanent or Contract.");
        }

        employees.add(emp);
        emails.add(email.toLowerCase());
        employeeMap.put(id, emp);
        System.out.println("Employee added successfully.");
    }

    private void displayAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        Iterator<Employee> it = employees.iterator();
        while (it.hasNext()) {
            it.next().displayDetails();
            System.out.println("-----------------------------");
        }
    }

    private void searchEmployee() throws EmployeeNotFoundException {
        String id = readString("Enter Employee ID: ");
        findEmployee(id).displayDetails();
    }

    private void applyLeave() throws EmployeeNotFoundException, InsufficientLeaveException {
        String id = readString("Enter Employee ID: ");
        Employee emp = findEmployee(id);

        String typeInput = readString("Enter Leave Type (CASUAL/SICK/EARNED): ").toUpperCase();
        LeaveType type;
        try {
            type = LeaveType.valueOf(typeInput);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Leave type must be CASUAL, SICK or EARNED.");
        }

        int days = readInt("Enter Number of Days: ");
        String reason = readString("Enter Reason: ");

        if (days <= 0) {
            throw new IllegalArgumentException("Leave days must be greater than 0.");
        }
        if (days > emp.getAvailableLeaveBalance()) {
            throw new InsufficientLeaveException(emp.getAvailableLeaveBalance(), days);
        }

        LeaveRequest request = new LeaveRequest(emp.getEmployeeId(), type, days, reason);
        leaveRequests.add(request);
        System.out.println("Leave request created successfully.");
        System.out.println("Request ID: " + request.getRequestId());
        System.out.println("Status: " + request.getStatus());
    }

    private void approveLeave() throws EmployeeNotFoundException, InsufficientLeaveException {
        LeaveRequest request = findRequest(readString("Enter Request ID: "));
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Request is already " + request.getStatus() + ".");
        }

 
        Employee emp = findEmployee(request.getEmployeeId());
        int previous = emp.getAvailableLeaveBalance();
        if (request.getNumberOfDays() > previous) {
        throw new InsufficientLeaveException(previous, request.getNumberOfDays());
        }

        emp.deductLeave(request.getNumberOfDays(), request.getLeaveType());
        request.setStatus(LeaveStatus.APPROVED);

        System.out.println("Leave Request Approved.");
        System.out.println("Employee ID : " + emp.getEmployeeId());
        System.out.println("Previous Balance: " + previous);
        System.out.println("Leave Used : " + request.getNumberOfDays());
        System.out.println("Remaining Leave : " + emp.getAvailableLeaveBalance());
    }

    private void rejectLeave() {
        LeaveRequest request = findRequest(readString("Enter Request ID: "));
        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Request is already " + request.getStatus() + ".");
        }
        request.setStatus(LeaveStatus.REJECTED);
        System.out.println("Leave Request Rejected. Leave balance unchanged.");
    }

    private void displayAllLeaveRequests() {
        if (leaveRequests.isEmpty()) {
            System.out.println("No leave requests found.");
            return;
        }
        for (LeaveRequest lr : leaveRequests) {
            System.out.println("Request ID : " + lr.getRequestId());
            System.out.println("Employee ID : " + lr.getEmployeeId());
            System.out.println("Leave Type : " + lr.getLeaveType());
            System.out.println("Days : " + lr.getNumberOfDays());
            System.out.println("Status : " + lr.getStatus());
            System.out.println("-----------------------------");
        }
    }

    private void displayLeaveBalance() throws EmployeeNotFoundException {
        Employee emp = findEmployee(readString("Enter Employee ID: "));
        System.out.println("Employee ID : " + emp.getEmployeeId());
        System.out.println("Current Leave Balance : " + emp.getAvailableLeaveBalance());
    }

    // ---------- Menu ----------
    private void showMenu() {
        System.out.println("\n===== Employee Leave Management System =====");
        System.out.println("1. Add Employee");
        System.out.println("2. Display All Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Apply Leave");
        System.out.println("5. Approve Leave");
        System.out.println("6. Reject Leave");
        System.out.println("7. Display All Leave Requests");
        System.out.println("8. Display Employee Leave Balance");
        System.out.println("9. Exit");
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            try {
                int choice = readInt("Enter your choice: ");
                switch (choice) {
                    case 1: addEmployee(); break;
                    case 2: displayAllEmployees(); break;
                    case 3: searchEmployee(); break;
                    case 4: applyLeave(); break;
                    case 5: approveLeave(); break;
                    case 6: rejectLeave(); break;
                    case 7: displayAllLeaveRequests(); break;
                    case 8: displayLeaveBalance(); break;
                    case 9:
                        System.out.println("Thank you. Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Error: Invalid choice. Enter 1-9.");
                }
            } catch (EmployeeNotFoundException | InsufficientLeaveException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                running = false;   // input stream closed
            }
        }
    }

    public static void main(String[] args) {
        new LeaveManagementSystem().run();
    }
}
