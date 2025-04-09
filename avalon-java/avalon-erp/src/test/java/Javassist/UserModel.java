package Javassist;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/05 10:41
 */

@Slf4j
public class UserModel extends Model {
    private String userField = "user default";

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    @Override
    public void sayHello() {
        log.info("hello user model, userField = " + userField);
        super.sayHello();
    }

    public void userMethod() {
        log.info("UserModel method called");
    }
}
