import java.util.ArrayList;
import java.util.Scanner;

// Class for Bank Account
class BankAccount {
    private String accountId;
    private String accountHolderName;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private double balance;
    private ArrayList<String> transactionHistory;

    public BankAccount(String accountId, String accountHolderName, String password, String securityQuestion, String securityAnswer, double initialDeposit) {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account created with initial deposit: " + initialDeposit);
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public boolean verifySecurityAnswer(String answer) {
        return this.securityAnswer.equalsIgnoreCase(answer);
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: " + amount);
        System.out.println("Deposit successful. Current balance: " + balance);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds! Withdrawal denied.");
        } else {
            balance -= amount;
            transactionHistory.add("Withdrew: " + amount);
            System.out.println("Withdrawal successful. Current balance: " + balance);
        }
    }

    public void showTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}

// Custom Exception for Insufficient Funds
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// Main Bank Management System Class
public class BankManagementSystem {
    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Bank Management System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Reset Password");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        System.out.println("Thank you for using the Bank Management System. Goodbye!");
                        choice = 0; // Break the loop
                    } else {
                        choice = -1; // Prevent exit
                    }
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    private static void createAccount() {
        System.out.println("\n--- Create Account ---");
        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Set Password: ");
        String password = scanner.nextLine();
        System.out.print("Set Security Question (e.g., Your favorite color): ");
        String securityQuestion = scanner.nextLine();
        System.out.print("Set Security Answer: ");
        String securityAnswer = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double initialDeposit = scanner.nextDouble();

        accounts.add(new BankAccount(accountId, accountHolderName, password, securityQuestion, securityAnswer, initialDeposit));
        System.out.println("Account created successfully!");
    }

    private static void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        BankAccount account = findAccount(accountId);
        if (account != null && account.authenticate(password)) {
            System.out.println("Login successful. Welcome " + account.getAccountHolderName() + "!");
            manageAccount(account);
        } else {
            System.out.println("Invalid Account ID or Password. Login failed.");
        }
    }

    private static BankAccount findAccount(String accountId) {
        for (BankAccount account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    private static void manageAccount(BankAccount account) {
        int choice;
        do {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction History");
            System.out.println("5. Delete Account");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: " + account.getBalance());
                    break;
                case 2:
                    System.out.print("Enter Deposit Amount: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter Withdrawal Amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    try {
                        account.withdraw(withdrawalAmount);
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    account.showTransactionHistory();
                    break;
                case 5:
                    System.out.print("Are you sure you want to delete your account? (yes/no): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        accounts.remove(account);
                        System.out.println("Account deleted successfully. Goodbye!");
                        choice = 6; // Force logout
                    }
                    break;
                case 6:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 6);
    }

    private static void resetPassword() {
        System.out.println("\n--- Reset Password ---");
        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();
        BankAccount account = findAccount(accountId);
        if (account != null) {
            System.out.println("Security Question: " + account.getSecurityQuestion());
            System.out.print("Enter your answer: ");
            String answer = scanner.nextLine();
            if (account.verifySecurityAnswer(answer)) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                account.resetPassword(newPassword);
                System.out.println("Password reset successfully!");
            } else {
                System.out.println("Security answer incorrect! Password reset failed.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }
}
