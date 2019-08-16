import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetServiceResponse implements IResponse {

    private String token;
    private String email;
    private String clientLocale;
    private String clientTimezone;
    private String successMessage;
    private Integer accountId;
    private Integer organizationId;
}
