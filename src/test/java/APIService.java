import org.mortbay.log.Log;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class APIService {

    private ServiceFactory factory = new ServiceFactory();

    private GetService getService;


    public void getService(String uri) {

        getService = factory.getService(new GetServiceRequest(ServiceHeaders.getNoHeaders().build()),uri);
        getService.invoke();

    }

    public void getServiceStatus(int status) {
        assertThat(Objects.nonNull(getService.getResponse()) && getService.getStatusCode() == status)
                .withFailMessage("Expected service response status is : "+status+ " but it is : "+getService.getStatusCode())
                .isTrue();
        Log.info("GetService status is as expected");
    }
}
