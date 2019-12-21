package info.tsubame.dev.tool.vnb.controller.api;

import lombok.Data;

@Data
public class GeneratesRequest {

    private String[] texts;
    private String format;
}
