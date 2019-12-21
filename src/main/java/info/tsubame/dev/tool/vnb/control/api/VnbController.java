package info.tsubame.dev.tool.vnb.control.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import info.tsubame.dev.tool.vnb.service.VnbService;

@RestController
@RequestMapping("/service")
public class VnbController {

    @Autowired
    private VnbService vnbService;

    @RequestMapping(value = "/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<String> generate(@RequestBody GenerateRequest request) {
        List<String> results = new ArrayList<>();
        for (String text : request.getTexts()) {
            results.add(vnbService.build(text, request.getFormat()));
        }

        return results;
    }

}
