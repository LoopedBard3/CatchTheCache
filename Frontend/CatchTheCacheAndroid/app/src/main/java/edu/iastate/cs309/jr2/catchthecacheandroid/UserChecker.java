package edu.iastate.cs309.jr2.catchthecacheandroid;


/**
 *  A class that holds the username and password validation methods.
 * @author Parker Bibus
 */
public class UserChecker {

    /**
     * Checks if the specified username meets the username expectations
     * @param username
     * @return true if a username is valid, false otherwise
     * @author Parker Bibus
     */
    public boolean isUsernameValid(String username) {
        //https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
        return username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    /**
     * Checks if the specified password meets the password expectations
     * @param password
     * @return true if a password is valid, false otherwise
     * @author Parker Bibus
     */
    public boolean isPasswordValid(String password) {
        //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
    }
}
