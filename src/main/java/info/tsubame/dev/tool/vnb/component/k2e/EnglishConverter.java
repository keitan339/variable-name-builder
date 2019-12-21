package info.tsubame.dev.tool.vnb.component.k2e;

import org.springframework.stereotype.Component;

@Component
public class EnglishConverter {

    private EnglishRepository englishRepository = EnglishRepository.getInstance();

    public String[] execute(String text) {
        return englishRepository.getEnglish(text);
    }

    public boolean canConvert(String text) {
        return this.englishRepository.canConvert(text);
    }
}
