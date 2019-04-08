package edu.iastate.cs309.jr2.catchthecacheandroid;

public class UserChecker {

    public boolean isUsernameValid(String username) {
        //https://stackoverflow.com/questions/12018245/regular-expression-to-validate-username
        return username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    public boolean isPasswordValid(String password) {
        //https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
    }
}
