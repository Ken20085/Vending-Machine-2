import java.util.ArrayList;
import java.util.Arrays;

/**
 * This represents a monetary denomination value and provides methods to validate whether a given amount
 * is accepted as a denomination.
 */
public class Denomination {
    private final int value;

    /** Standard accepted/valid denominations from highest to lowest */
    private static final ArrayList<Integer> VALID_VALUES = initializeValidValues();

    /**
     * Constructs a Denomination object with a given monetary value.
     *
     * @param value the numeric value of the given denomination
     */
    public Denomination(int value)
    {
        this.value = value;
    }

    private static ArrayList<Integer> initializeValidValues()
    {
        ArrayList<Integer> values = new ArrayList<>();

        values.add(1000);
        values.add(500);
        values.add(200);
        values.add(100);
        values.add(50);
        values.add(20);
        values.add(10);
        values.add(5);
        values.add(1);

        return values;
    }

//    /**
//     * Gets the value of this specific denomination instance.
//     *
//     * @return the denomination value
//     */
//    public int getValue() {
//        return this.value;
//    }

    /**
     * Gets the complete list of all valid denomination values.
     *
     * @return an int array containing all allowed denomination integers
     */
    public static ArrayList<Integer> getValidValues()
    {
        return VALID_VALUES;
    }

    /**
     * Checks if a given integer matches any of the allowed denominations.
     *
     * @param value the denomination value to validate
     * @return true if value is a valid denomination; false otherwise
     */
    public static boolean isValid(int value) {
        //loop through all valid denominations
        for (int valid : VALID_VALUES){
            if (valid == value) {
                return true;       //returns true if its valid
            }
        }
        return false;              //denomination doesnt exist
    }
}