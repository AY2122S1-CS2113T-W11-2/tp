package seedu.duke;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddFunctionTest {

    @Test
    public void addExpense_oneAddition_expectSuccess() {
        LocalDate date = LocalDate.now();
        ExpenseList expenseList = new ExpenseList();
        Expense expense = new Expense("burger", "2021-10-10", "10");
        String expenseName = expense.getName();
        String expenseDate = expense.getDate().toString();
        String expenseAmount = Double.toString(expense.getAmount());
        String expenseCatNum = "1";
        expenseList.addExpense(expenseName, expenseDate, expenseAmount,expenseCatNum);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.
        expenseList.viewExpense();
        String expectedOutput  = "Here is the list of your expenses:" + System.lineSeparator()
                + "      Food       | 2021-10-10 | burger | $10.00" + System.lineSeparator();// Notice the \n for new line.
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void addExpense_twoAdditions_expectSuccess() {
        LocalDate date = LocalDate.now();
        ExpenseList expenseList = new ExpenseList();
        Expense expenseFood = new Expense("burger", "2021-10-10", "10", "1");
        Expense expenseEntertainment = new Expense("movie", "2021-10-10", "13", "2");
        String foodExpenseName = expenseFood.getName();
        String foodExpenseDate = expenseFood.getDate().toString();
        String foodExpenseAmount = Double.toString(expenseFood.getAmount());
        String entertainmentExpenseName = expenseEntertainment.getName();
        String entertainmentDate = expenseEntertainment.getDate().toString();
        String entertainmentExpenseAmount = Double.toString(expenseEntertainment.getAmount());
        String foodCatNum = "1";
        String entertainmentCatNum = "2";
        expenseList.addExpense(foodExpenseName, foodExpenseDate, foodExpenseAmount, foodCatNum);
        expenseList.addExpense(entertainmentExpenseName, entertainmentDate, entertainmentExpenseAmount, entertainmentCatNum);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // After this all System.out.println() statements will come to outContent stream.
        expenseList.viewExpense();
        String expectedOutput  = "Here is the list of your expenses:" + System.lineSeparator()
                + "      Food       | 2021-10-10 | burger | $10.00" + System.lineSeparator()
                + " Entertainment   | 2021-10-10 | movie | $13.00" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void addExpense_wrongAmountFormat_expectErrorMessage() throws MintException {
        ExpenseList expenseList = new ExpenseList();
        Parser parser = new Parser();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        parser.executeCommand("add n/burger d/2021-12-23 a/ABCD c/1", expenseList);
        String expectedOutput  = "Please enter a valid amount!" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }


    @Test
    public void addExpense_wrongDateFormat_expectErrorMessage() throws MintException {
        ExpenseList expenseList = new ExpenseList();
        Parser parser = new Parser();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        parser.executeCommand("add n/movie d/ABCD a/10 c/3", expenseList);
        String expectedOutput  = "Please enter a valid date!" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}