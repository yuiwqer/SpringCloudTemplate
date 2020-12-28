package sui.cloud.user.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private Object data;
    private Integer status;
    private String statusText;
}
