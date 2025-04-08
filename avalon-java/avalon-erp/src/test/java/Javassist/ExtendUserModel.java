package Javassist;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/05 10:41
 */
@Slf4j
public class ExtendUserModel extends Model {
    private String name = "default";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void customMethod() {
        log.info("Custom method called with name: " + name);
    }
}
