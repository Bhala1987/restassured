import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.mortbay.log.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static net.serenitybdd.rest.SerenityRest.given;


public abstract class ServiceClass implements IService {

    public static final ThreadLocal<String> theJSessionCookie = new ThreadLocal<>();
    public static final ThreadLocal<String> theRememberMeCookie = new ThreadLocal<>();
    private static final String JSESSIONID = "JSESSIONID";
    private static final String REMEMBER_ME = "REMEMBER_ME";
    protected final ServiceRequest request;
    protected final String endPoint;
    protected RequestSpecification restRequest;
    @Getter
    @Setter
    protected Response restResponse;
    protected boolean successful;
    private long startTime;
    private Set<String> unrecognizedProperties;

    /**
     * a voyantaService comprises a request, a client and an endpoint
     *
     * @param request  the request object required
     * @param endPoint the endpoint of the voyantaService
     */
    public ServiceClass(IRequest request, String endPoint) {
        this.request = (ServiceRequest) request;
        this.endPoint = endPoint;
    }

    protected abstract void checkThatResponseBodyIsPopulated();

    protected abstract void mapResponse();

    /**
     * adds the query parameters
     * adds the path parameters
     * adds the headers
     * invokes the voyantaService call
     * sets the success state based upon the return type
     * maps the response to either errors or the valid response domain model and verifies mapping has been successful
     */
    @Override
    public void invoke() {
        unrecognizedProperties = new HashSet<>();
        restRequest = given().config(
                RestAssured.config()
                        .encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) -> new ObjectMapper().addHandler(new DeserializationProblemHandler() {
                            @Override
                            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                                String position = StringUtils.join(beanOrClass.toString().substring(beanOrClass.toString().lastIndexOf('.') + 1, beanOrClass.toString().lastIndexOf('@')).split("\\$"), " -> ");
                                unrecognizedProperties.add(propertyName + " in " + position);
                                jp.skipChildren();
                                return true;
                            }
                        }))))
                .log().all().baseUri(endPoint);
        if (request.getQueryParameters() != null) {
            restRequest.queryParams(request.getQueryParameters().getParameters());
        }
        if (request.getPathParameters() != null) {
            restRequest.basePath(request.getPathParameters().get());
        }
        restResponse = addHeadersAndExecuteRequest().then().log().all().extract().response();

        if (Objects.nonNull(restResponse.getCookie(JSESSIONID))) {
            theJSessionCookie.set(restResponse.getCookie(JSESSIONID));
        }

        if (Objects.nonNull(restResponse.getCookie(REMEMBER_ME))) {
            theRememberMeCookie.set(restResponse.getCookie(REMEMBER_ME));
        } else {
            theRememberMeCookie.set(null);
        }

        stopTheClock();
        setSuccessState();
        if (successful) {
            mapResponse();
            checkThatResponseBodyIsPopulated();
        }
    }

    public void invokeFormData(File file) {
        unrecognizedProperties = new HashSet<>();
        restRequest = given().config(
                RestAssured.config()
                        .encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                        .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) -> new ObjectMapper().addHandler(new DeserializationProblemHandler() {
                            @Override
                            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                                String position = StringUtils.join(beanOrClass.toString().substring(beanOrClass.toString().lastIndexOf('.') + 1, beanOrClass.toString().lastIndexOf('@')).split("\\$"), " -> ");
                                unrecognizedProperties.add(propertyName + " in " + position);
                                jp.skipChildren();
                                return true;
                            }
                        }))))
                .multiPart(new MultiPartSpecBuilder(file).fileName(file.getName())
                        .controlName("file")
                        .build())
                .log().all().baseUri(endPoint);
        if (request.getQueryParameters() != null) {
            restRequest.queryParams(request.getQueryParameters().getParameters());
        }
        if (request.getPathParameters() != null) {
            restRequest.basePath(request.getPathParameters().get());
        }
        restResponse = addHeadersAndExecuteRequest().then().log().all().extract().response();

        if (Objects.nonNull(restResponse.getCookie(JSESSIONID))) {
            theJSessionCookie.set(restResponse.getCookie(JSESSIONID));
        }

        if (Objects.nonNull(restResponse.getCookie(REMEMBER_ME))) {
            theRememberMeCookie.set(restResponse.getCookie(REMEMBER_ME));
        } else {
            theRememberMeCookie.set(null);
        }

        stopTheClock();
        setSuccessState();
        if (successful) {
            mapResponse();
            checkThatResponseBodyIsPopulated();
        }
    }

    protected void checkThatResponseBodyIsPopulated(Object expectedResponseContent) {
        Assert.assertNotNull(
                "The message body was not populated but the voyantaService reported a " +
                        restResponse.getStatusCode() +
                        " " +
                        restResponse.getStatusLine(),
                expectedResponseContent
        );
    }

    private void startTheClock() {
        startTime = System.currentTimeMillis();
    }

    private void stopTheClock() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        Log.info("The voyantaService " + this.getClass().toString() + " responded in " + elapsedTime + "ms." + "Call was to " + restResponse.toString());
    }

    /**
     * Add headers and execute the request
     *
     * @return The response from the request.
     */
    private Response addHeadersAndExecuteRequest() {

        // Add all the headers.
        request.getHeaders().get().forEach((key, value) -> restRequest.header(key, value));

        // Set the cookie and default content type.
        if (Objects.nonNull(theJSessionCookie.get())) {
            restRequest.cookie(JSESSIONID, theJSessionCookie.get());
        }

        // Set the request body if it's relevant.
        if (request.getRequestBody() != null) {
            restRequest.body(request.getRequestBody());
        }

        // Prepare a response (cyclomatic complexity)
        Response response;

        startTheClock();

        // Rest Assured 3 could do this much more succinctly.
        switch (request.getHttpMethod()) {
            case GET:
                response = restRequest.when().get();
                break;
            case PUT:
                response = restRequest.when().put();
                break;
            case POST:
                response = restRequest.when().post();
                break;
            case DELETE:
                response = restRequest.when().delete();
                break;
            default:
                response = restRequest.when().get().then().log().all().extract().response();
                break;
        }

        // Return the response.
        return response;
    }


    /**
     * Asserts the successful property to see if the request was NOT a success.
     */
    protected void assertThatServiceCallWasNotSuccessful() {
        Assert.assertFalse(
                "The voyantaService returned a: " +
                        restResponse.getStatusCode() +
                        ":" +
                        restResponse.getStatusLine(),
                successful
        );
    }


    /**
     * If a 200 was returned set successful to be true.
     */
    private void setSuccessState() {
        this.successful = restResponse.getStatusCode() == 200;
    }

    public int getStatusCode() {
        return restResponse.getStatusCode();
    }

    public String getExceptionMessage() {
        return restResponse.getBody().prettyPrint();
    }
}
