package info.tsubame.dev.tool.vnb.component.k2e;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import lombok.Data;

public class EnglishRepository {

    private static final String EDICT_FILE_NAME = "edict.csv";

    private Map<String, EnglishData> dictionary = new HashMap<>();

    private static EnglishRepository INSTANCE = new EnglishRepository();

    public static EnglishRepository getInstance() {
        return INSTANCE;
    }

    private EnglishRepository() {
        try {
            setup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setup() throws IOException {
        ClassPathResource resource = new ClassPathResource(EDICT_FILE_NAME);
        if (resource.exists()) {
            List<String> contents = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);

            for (String line : contents) {
                String[] items = line.split(",");
                EnglishData english = new EnglishData();
                english.setEnglish1(
                        (items.length <= 1 || StringUtils.isEmpty(items[1])) ? null : convertRepositoryStr(items[1]));
                english.setEnglish2(
                        (items.length <= 2 || StringUtils.isEmpty(items[2])) ? null : convertRepositoryStr(items[2]));
                english.setEnglish3(
                        (items.length <= 3 || StringUtils.isEmpty(items[3])) ? null : convertRepositoryStr(items[3]));
                english.setEnglish4(
                        (items.length <= 4 || StringUtils.isEmpty(items[4])) ? null : convertRepositoryStr(items[4]));
                english.setEnglish5(
                        (items.length <= 5 || StringUtils.isEmpty(items[5])) ? null : convertRepositoryStr(items[5]));

                this.dictionary.put(items[0], english);
            }
        }
    }

    private String convertRepositoryStr(String csvValue) {
        return StringUtils.replace(csvValue, "-", " ");
    }

    public boolean canConvert(String text) {
        if (this.dictionary.containsKey(text) && StringUtils.isNotEmpty(this.dictionary.get(text).getEnglish1())) {
            return true;
        } else {
            return false;
        }
    }

    public String[] getEnglish(String text) {
        if (canConvert(text)) {
            return this.dictionary.get(text).getEnglish1().split(" ");

        } else {
            return null;
        }
    }

    @Data
    private static class EnglishData {

        private String english1;
        private String english2;
        private String english3;
        private String english4;
        private String english5;
    }

}
