public class GetService extends ServiceClass implements IService {

    /**
     * @param request  the request object required
     * @param endPoint the endpoint of the voyantaService
     *                 <p>
     */
    public GetService(IRequest request, String endPoint) {
        super(request, endPoint);
    }

    private GetServiceResponse serviceResponse;
    @Override
    protected void checkThatResponseBodyIsPopulated() {
        checkThatResponseBodyIsPopulated(serviceResponse);
    }

    @Override
    protected void mapResponse() {
        serviceResponse = restResponse.as(GetServiceResponse.class);
    }

    @Override
    public GetServiceResponse getResponse() {
        return serviceResponse;
    }
}
