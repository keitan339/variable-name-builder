package info.tsubame.dev.tool.vnb.component.k2r;

import org.springframework.stereotype.Component;

@Component
public class RomaConverter {

    private Kana2Roma kana2Roma = new Kana2Roma();

    public String execute(String text) {
        try {
            return kana2Roma.convert(text).toLowerCase();
        } catch (Kana2RomaException e) {
            return "";
        }
    }


}
