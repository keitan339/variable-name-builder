package info.tsubame.dev.tool.vnb.component.format;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FormatterFactory {

    public static final String CLASS = "class";

    public static final String FIELD = "field";

    public static final String CONSTANT = "constant";

    public IForrmatter create(String formatType) {
        if (StringUtils.equals(formatType, CLASS)) {
            return new ClassFormatter();
        } else if (StringUtils.equals(formatType, FIELD)) {
            return new FieldFormatter();
        } else {
            return new ConstantFormatter();
        }
    }

}
