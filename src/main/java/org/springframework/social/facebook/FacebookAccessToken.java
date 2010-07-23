package org.springframework.social.facebook;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * This isn't particularly portable or necessarily in line with what we may do with SS-OAuth and OAuth 2. But,
 * it's a start and it helps me get the FB access token.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FacebookAccessToken {

}
