package info.tsubame.dev.tool.vnb.component.format;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FormatterFactory {

    public IForrmatter create(String formatType) {
        if (StringUtils.equals(formatType, FormatType.CLASS)) {
            return new ClassFormatter();
        } else if (StringUtils.equals(formatType, FormatType.FIELD)) {
            return new FieldFormatter();
        } else {
            return new ConstantFormatter();
        }
    }

}
