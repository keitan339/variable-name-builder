package info.tsubame.dev.tool.vnb.control.api;

import lombok.Data;

@Data
public class GenerateRequest {

    private String[] texts;

    private String format;
}
