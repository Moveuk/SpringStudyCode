package hello.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //Field에 붙을지 type에 붙을지 정하는 옵션
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
