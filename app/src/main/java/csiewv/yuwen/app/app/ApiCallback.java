package csiewv.yuwen.app.app;

/**
 * Created by mac on 18/10/2017.
 */

public interface ApiCallback {
    void studentIdExist(String studentId);
    void studentIdNonExist(String studentId);
    void studentIdCheckError();
    void studentIdCheckProcessing(String studentId);
}
