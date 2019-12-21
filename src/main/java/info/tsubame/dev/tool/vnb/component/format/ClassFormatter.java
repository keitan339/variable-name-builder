package info.tsubame.dev.tool.vnb.component.format;


import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ClassFormatter implements IForrmatter {

    @Override
    public String format(List<String> romas) {
        StringBuffer sb = new StringBuffer();
        for (String roma : romas) {
            sb.append(StringUtils.capitalize(roma.toLowerCase()));
        }

        return sb.toString();
    }
}
