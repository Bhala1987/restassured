import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public final class ServiceHeaders implements IHeaders {

    @SerializedName("Locale")
    private String locale;
    @SerializedName("TimeZone")
    private String timeZone;
    @SerializedName("Content-Type")
    private String contentType;
    @SerializedName("Public-Version")
    private String publicVersion;
    @SerializedName("Accept-Charset")
    private String acceptCharset;
    @SerializedName("Accept")
    private String accept;

    public static ServiceHeadersBuilder getBaseBuilder() {

        return ServiceHeaders.builder()
                .locale("en-gb")
                .timeZone("Europe/London")
                .contentType("application/json")
                .accept("application/json")
                .publicVersion("V212")
                .acceptCharset("UTF-8");

    }

    public static ServiceHeadersBuilder getFileBuilder() {

        return ServiceHeaders.builder()
                .locale("en-gb")
                .timeZone("Europe/London")
                .publicVersion("V212");

    }

    public static ServiceHeadersBuilder getFileBuilderZ() {

        return ServiceHeaders.builder()
                .locale("en-us")
                .timeZone("America/New_York")
                .publicVersion("V212");

    }

    public static ServiceHeadersBuilder getNoHeaders() {

        return ServiceHeaders.builder();

    }

    /**
     * @return the MultivaluedMap that is required by the jersey client to set the headers for the request
     */
    public Map<String, Object> get() {
        Map<String, Object> headers = new HashMap<>();

        if (locale != null) {
            headers.put("Locale", locale);
        }
        if (timeZone != null) {
            headers.put("TimeZone", timeZone);
        }
        if (contentType != null) {
            headers.put("Content-Type", contentType);
        }
        if (accept != null) {
            headers.put("Accept", accept);
        }
        if (publicVersion != null) {
            headers.put("Public-Version", publicVersion);
        }
        if (acceptCharset != null) {
            headers.put("Accept-Charset", acceptCharset);
        }
        return headers;
    }

}
