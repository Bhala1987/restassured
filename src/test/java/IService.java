public interface IService {

    void invoke() throws Throwable;

    IResponse getResponse();
}