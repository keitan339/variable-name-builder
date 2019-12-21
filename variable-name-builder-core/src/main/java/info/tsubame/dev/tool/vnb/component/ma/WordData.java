package info.tsubame.dev.tool.vnb.component.ma;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表記
     */
    private String surface;

    /**
     * 読み
     */
    private String reading;

}
