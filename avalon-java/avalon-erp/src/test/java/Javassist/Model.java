package Javassist;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/05 10:41
 */

@Slf4j
public abstract class Model {
    public void sayHello() {
        log.info("hello model");
    }
}
