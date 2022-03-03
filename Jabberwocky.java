/**
 * This program serves as an interpreter for the JABBERWOCK language.
 * 
 * Author: Dakota Kallas
 * Jabberwocky.java
 */

import java.io.*;
import java.util.*;

public class Jabberwocky {

    public static void main(String[] args) {
        // Ensure the correct number of arguments
        if(args.length > 0) {
            try {
                File inputFile = new File(args[0]);
                BufferedReader br = new BufferedReader(new FileReader(inputFile));
    
                // Create a Map for each variables value
                Map<String, String> variableMap = inputIterator(br);
    
                br.close();
                printMap(variableMap);
            } catch (IOException e) {
                System.err.println("Argument '" + args[0] + "' must be a valid .txt file name (Must include .txt).");
                System.exit(1);
            }
        } else {
            System.err.println("Must include an argument that is a valid .txt file name.");
            System.exit(1);
        }
    }

    /**
     * Method used to interpret a JABBERWOCK file input and iterate througuh each JABBERWOCK-string
     * @param br: A BufferedReader used to read in each line from the input file
     * @return: Return a map of all JABBERWOCK-string values
     * @throws IOException
     */
    private static Map<String, String> inputIterator(BufferedReader br) throws IOException {
        // Variable used to represent each JABBERWORK-assignment-statement
        String statement = "";
        Map<String, String> variables = new HashMap<String, String>();

        // Evaluate each line as its own expression until the end of the file
        while((statement = br.readLine()) != null) {
            // Seperate the variable name & expression from the JABBERWORK-assignment-statement
            String [] totExpression = statement.split("\\s+");
            String variable = totExpression[0];
            // Adhere to maximum JABBERWOCK-strings length
            if(variable.length() > 2022) {
                continue;
            }
            // Check to ensure a valid alphabetic variable name
            if(!variable.matches("[a-zA-Z]+")) {
                System.err.println("ERROR: Invalid variable name (" + variable + ")");
                continue;  
            }

            // Remove the variable name from the JABBERWORK-assignment-statement to get the JABBERWORK-expression
            totExpression = Arrays.copyOfRange(totExpression, 1, totExpression.length);
            List<String> expression = new LinkedList<String>(Arrays.asList(totExpression));

            // Store the updated value of the variable in variableMap
            if(variables.containsKey(variable)) {
                variables.replace(variable, evaluate(expression, variables));
            } else {
                variables.put(variable, evaluate(expression, variables));
            }
        }
        return variables;
    }

    /**
     * This method evaluates a JABBERWOCK-expression that is read in from String s.
     * 
     * @param s: The string used to read in a JABBERWOCK-expression.
     * @param m: A Map that contains all variables that have been assigned to a variable name.
     * @return: Return the evaluated values of the JABBERWOCK-expression.
     */
    private static String evaluate(List<String> expression, Map<String,String> m) {
        // Check to ensure the overall JABBERWOCK-expression has an odd number of sub-expressions
        if((expression.size() % 2) != 1) {
            System.err.println("ERROR: Invalid expression length (" + expression.size() + ")");
            return "undefined";
        }

        String value = "";
        // Get the final value of the JABBERWOCK-expression if it one contains 1 sub-expression
        if(expression.size() == 1) {
            value = getValue(expression.get(0), m);
            return value;
        }
   
        // Iterate through the JABBERWOCK-expression until we have the final value
        while(expression.size() > 1) {
            int curSize = expression.size();
            String exp1 = expression.get(curSize - 3);
            String func = expression.get(curSize - 2);
            String exp2 = expression.get(curSize - 1);

            // Check if the expressions are variables from the Map m
            exp1 = getValue(exp1, m);
            exp2 = getValue(exp2, m);

            if(exp1.equals("undefined") || exp2.equals("undefined")) {
                return "undefined";
            }
            
            // Error check the current 2 sub-expressions to ensure they contain ONLY the correct characters
            if(!isValidExpression(exp1) || !isValidExpression(exp2)) {
                return "undefined";
            }

            // Perform the proper operation on the two sub-expressions
            if(func.equals("&")) {
                value = concatenate(exp1, exp2);
            } else if (func.equals("||")) {
                value = interleave(exp1, exp2);
            } else if (func.equals("#")) {
                value = splice(exp1, exp2);
            } else {
                // Error check to ensure a valid function
                System.err.println("ERROR: Invalid function (" + func + ")");
                return "undefined";
            }

            // Remove those expressions from the list and add the new value
            expression.remove(curSize - 1);
            expression.remove(curSize - 2);
            expression.set(curSize - 3, value);
        }

        // Adhere to maximum JABBERWOCK-strings length
        if(value.length() > 2022) {
            return "undefined";
        }
        return value;
    }

    /**
     * Method used to check if a JABBERWOCK-expression has the correct syntax
     * @param exp: JABBERWOCK-expression
     * @return: Return false if invalid, true if valid
     */
    private static boolean isValidExpression(String exp) {
        // Error check the current 2 sub-expressions to ensure they contain ONLY the correct characters
        if(!exp.matches("^[-*?]+")) {
            System.err.println("ERROR: Invalid expression (" + exp + ")");
            return false;
        }
        return true;
    }

    /**
     * Method used to check if a JABBERWOCK-string is a variable that exsists in the Map m
     * @param s: JABBERWOCK-string
     * @param m
     * @return: Return the value of the JABBERWOCK-string
     */
    private static String getValue(String s, Map<String,String> m) {
        // Check if the provided string is strictly alphabetic
        if(s.matches("[a-zA-Z]+")) {
            if(s.equals("undefined")) {
                return "undefined";
            }
            else if(m.containsKey(s)) {
                String value = m.get(s);
                // Adhere to maximum JABBERWOCK-strings length
                if(value.length() > 2022) {
                    return "undefined";
                }
                return value;
            }
            return "undefined";
        }

        return s;
    }

    /**
     * Method to perform the splice functionality on two JABBERWOCK-expressions
     * @param exp1: JABBERWOCK-expression
     * @param exp2: JABBERWOCK-expression
     * @return: Returns the spliced value of the two sub-expressions
     */
    private static String splice(String exp1, String exp2) {
        // Split the 2nd sub-expression in half to splice in the 1st
        String firstHalf = exp2.substring(0, exp2.length() / 2);
        String secondHalf = exp2.substring(exp2.length() / 2, exp2.length());
        return firstHalf + exp1 + secondHalf;        
    }

    /**
     * Method to perform the interleaving functionality on two JABBERWOCK-expressions
     * @param exp1: JABBERWOCK-expression
     * @param exp2: JABBERWOCK-expression
     * @return: Returns the interleaved value of the two sub-expressions
     */
    private static String interleave(String exp1, String exp2) {
        String value = "";
        List<String> subchars1 = new LinkedList<String>(Arrays.asList(exp1.split("")));
        List<String> subchars2 = new LinkedList<String>(Arrays.asList(exp2.split("")));

        // Iterate through both sub-expressions until the end of the 1st
        while(!subchars1.isEmpty()) {
            value += subchars1.get(0);
            subchars1.remove(0);
            // If there are still remaining characters in the 2nd sub-espression, add them to the final value
            if(!subchars2.isEmpty()) {
                value += subchars2.get(0);
                subchars2.remove(0);
            }
        }

        // While there are still remaining elements in the 2nd sub-expression, add them to the final value
        while(!subchars2.isEmpty()) {
            value += subchars2.get(0);
            subchars2.remove(0);
        }
        return value;
    }

    /**
     * Method to perform the concatenation functionality on two JABBERWOCK-expressions
     * @param exp1: JABBERWOCK-expression
     * @param exp2: JABBERWOCK-expression
     * @return: Returns the concatenated value of the two sub-expressions
     */
    private static String concatenate(String exp1, String exp2) {
        String value = "";
        String combined = exp1 + exp2;
        List<String> subchars = new LinkedList<String>(Arrays.asList(combined.split("")));
        List<String> finalChars = new LinkedList<String>();
        // Add the first element to prevent OutOfBoundsException
        finalChars.add(subchars.get(0));
        subchars.remove(0);

        // Iterate through the concatenated expression
        while(!subchars.isEmpty()) {
            // If the current character is not the last character in the final value, add it
            if(!finalChars.get(finalChars.size() - 1).equals(subchars.get(0))) {
                finalChars.add(subchars.get(0));
            }
            subchars.remove(0);
        }

        while(!finalChars.isEmpty()) {
            value += finalChars.get(0);
            finalChars.remove(0);
        }

        return value;
    }
    
    /**
     * Method used to print out the map of JABBERWOCK-strings in alphabetical order
     * @param m
     */
    private static void printMap(Map<String, String> m) {
        // Fill a TreeMap with the values from the HashMap, because TreeMaps are naturally sorted
        TreeMap<String, String> sorted = new TreeMap<>();
        sorted.putAll(m);

        // Print out the resulting values
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            System.out.println((String)entry.getKey() + " = " + (String)entry.getValue());
        }
    }
}