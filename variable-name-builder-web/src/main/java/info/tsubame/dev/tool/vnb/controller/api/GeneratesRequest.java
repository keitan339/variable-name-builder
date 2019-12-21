package info.tsubame.dev.tool.vnb.controller.api;

import info.tsubame.dev.tool.vnb.SuppressFBWarnings;
import lombok.Data;

@Data
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP"}, justification = "for generation by lombok")
public class GeneratesRequest {

    private String[] texts;
    private String format;
}
