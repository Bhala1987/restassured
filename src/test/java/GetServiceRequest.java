public class GetServiceRequest extends ServiceRequest implements IRequest {

    /**
     * only headers need to be provided to the constructor
     *
     * @param headers
     */
    public GetServiceRequest(ServiceHeaders headers) {
        super(headers, HttpMethods.GET, null, null, null);
    }
}
