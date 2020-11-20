package demo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeqiang
 * @since 11/19/20 3:51 PM
 */
@RestController
public class UserInfoController {

    @RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public Authentication authentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
