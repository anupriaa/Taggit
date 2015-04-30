package views.formdata;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing class for Login form data.
 */
public class LoginFormData {
  public String email ="";
  public String password = "";

  /**
   * No-arg constructor required by play.
   */
  public LoginFormData() {
    //no
  }

  public LoginFormData (String email, String password) {
    this.email = email;
    this.password = password;
  }
  /**
   * Checks if the form fields are valid. Called bu bindRequestForm().
   * @return null if no errors else ist of errors.
   */
  public List<ValidationError> validate() {
    List<ValidationError> errors = new ArrayList<>();

    if (email == null || email.length() == 0) {
      errors.add(new ValidationError("email", "Email is required"));
    }
    if (password == null || password.length() == 0) {
      errors.add(new ValidationError("password", "Password is required"));
    }
    return errors.isEmpty() ? null : errors;
  }
}
