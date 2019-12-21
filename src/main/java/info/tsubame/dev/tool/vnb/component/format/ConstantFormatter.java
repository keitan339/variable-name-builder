package info.tsubame.dev.tool.vnb.component.format;


import java.util.List;

public class ConstantFormatter implements IForrmatter {

    @Override
    public String format(List<String> romas) {

        StringBuffer sb = new StringBuffer();
        for (String roma : romas) {
            if (sb.length() != 0) {
                sb.append("_");
            }

            sb.append(roma.toUpperCase());
        }

        return sb.toString();

    }
}
