public class ServiceFactory {

    public GetService getService(GetServiceRequest request, String uri) {
        return new GetService(request, uri);
    }
}
