package info.tsubame.dev.tool.vnb.component.k2r;

public class Kana2RomaException extends Exception {

    private static final long serialVersionUID = 1L;

    public static enum Type {
        NOT_KATAKANA_CHARACTER("カタカナではない文字(%s)が入力されました。"), //
        ILLEGAL_KANA_SEQUENCE("不正なカナの並び(%s)が入力されました。"), //
        ILLEGAL_HEAD_CHAR("文字(%s)は先頭に使えません。");

        private String message;

        private Type(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return this.message;
        }
    }

    private Type type;
    private String cause;

    public Kana2RomaException(Type type, String cause) {
        this.type = type;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return this.type.toString().replace("%s", this.cause);
    }
}
