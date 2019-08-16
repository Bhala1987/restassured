public interface IRequest {

    IHeaders getHeaders();

    IQueryParams getQueryParameters();

    IRequestBody getRequestBody();

    HttpMethods getHttpMethod();

    IPathParameters getPathParameters();

}