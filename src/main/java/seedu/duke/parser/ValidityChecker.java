package seedu.duke.parser;

import seedu.duke.Expense;
import seedu.duke.MintException;
import seedu.duke.RecurringExpense;
import seedu.duke.Ui;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidityChecker {
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final String FILE_PATH = "data" + File.separator + "Mint.txt";

    static void checkEmptyName(Parser parser) throws MintException {
        boolean hasEmptyName = parser.name.equals(Parser.STRING_EMPTY);
        if (hasEmptyName) {
            logger.log(Level.INFO, "User entered empty name");
            throw new MintException(MintException.ERROR_NO_NAME);
        }
    }

    static void checkInvalidAmount(Parser parser) throws MintException {
        try {
            Double.parseDouble(parser.amount);
        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "User entered invalid amount");
            throw new MintException(MintException.ERROR_INVALID_AMOUNT);
        }
    }

    static void checkInvalidDate(Parser parser) throws MintException {
        try {
            LocalDate.parse(parser.date, Expense.dateFormatter);
        } catch (DateTimeParseException e) {
            logger.log(Level.INFO, "User entered invalid date");
            throw new MintException(MintException.ERROR_INVALID_DATE);
        }
    }

    static void checkInvalidEndDate(Parser parser) throws MintException {
        try {
            LocalDate parsedEndDate = LocalDate.parse(parser.endDate, Expense.dateFormatter);
            LocalDate parsedDate = LocalDate.parse(parser.date, Expense.dateFormatter);
            if (parsedEndDate.isBefore(parsedDate)) {
                throw new MintException("End date must be after start date.");
            }
        } catch (DateTimeParseException e) {
            logger.log(Level.INFO, "User entered invalid date");
            throw new MintException(MintException.ERROR_INVALID_DATE);
        } catch (MintException e) {
            throw new MintException(e.getMessage());
        }
    }

    static void checkInvalidCatNum(Parser parser) throws MintException {
        try {
            int catNumInt = Integer.parseInt(parser.catNum);
            if (catNumInt < Parser.CAT_NUM_FOOD_INT || catNumInt > Parser.CAT_NUM_OTHERS_INT) {
                throw new MintException(MintException.ERROR_INVALID_CATNUM);
            }
        } catch (NumberFormatException e) {
            logger.log(Level.INFO, "User entered invalid category number");
            throw new MintException(MintException.ERROR_INVALID_CATNUM);
        }
    }

    static void checkInvalidInterval(Parser parser) throws MintException {
        try {
            RecurringExpense expense = new RecurringExpense();
            expense.setInterval(parser.interval);
        } catch (MintException e) {
            logger.log(Level.INFO, "User entered invalid interval");
            throw new MintException(e.getMessage());
        }
    }

    static ArrayList<String> identifyValidTags(Parser parser, String userInput,
                                               String[] mandatoryTags) throws MintException {
        ArrayList<String> validTags = new ArrayList<>();
        ArrayList<String> invalidTags = new ArrayList<>();
        String[] tags = parser.isRecurring ? new String[]{"n/", "d/", "a/", "c/", "i/", "e/"}
                : new String[]{"n/", "d/", "a/", "c/"};
        List<String> mandatoryTagsToBeChecked = Arrays.asList(mandatoryTags);

        for (String tag : tags) {
            try {
                if (userInput.contains(tag.trim())) {
                    switch (tag.trim()) {
                    case "n/":
                        checkEmptyName(parser);
                        break;
                    case "d/":
                        checkInvalidDate(parser);
                        break;
                    case "a/":
                        checkInvalidAmount(parser);
                        break;
                    case "c/":
                        checkInvalidCatNum(parser);
                        break;
                    case "i/":
                        checkInvalidInterval(parser);
                        break;
                    case "e/":
                        checkInvalidEndDate(parser);
                        break;
                    default:
                        throw new MintException(MintException.ERROR_INVALID_TAG_ERROR);
                    }
                    validTags.add(tag);
                } else if (mandatoryTagsToBeChecked.contains(tag)) {
                    invalidTags.add(tag);
                }
            } catch (MintException e) {
                invalidTags.add(tag);
            }
        }

        if (invalidTags.size() > 0) {
            Ui.constructErrorMessage(invalidTags);
            throw new MintException(Ui.constructErrorMessage(invalidTags).toString());
        } else if (validTags.size() == 0) {
            throw new MintException("Please enter at least one tag.");
        }
        return validTags;
    }

    static ArrayList<String> checkExistenceAndValidityOfTags(Parser parser, String userInput) throws MintException {
        try {
            String[] mandatoryTags;
            switch (parser.command) {
            case "add":
                mandatoryTags = new String[]{"n/", "a/"};
                break;
            case "addR":
                mandatoryTags = new String[]{"n/", "a/", "i/"};
                break;
            default:
                mandatoryTags = new String[]{};
            }
            return identifyValidTags(parser, userInput, mandatoryTags);
        } catch (MintException e) {
            throw new MintException(e.getMessage());
        }
    }

    static void checkSetFormat(String[] userInput) throws MintException {
        if (userInput.length != 3) {
            throw new MintException(MintException.ERROR_SET_FORMAT_WRONG);
        }
    }
}
