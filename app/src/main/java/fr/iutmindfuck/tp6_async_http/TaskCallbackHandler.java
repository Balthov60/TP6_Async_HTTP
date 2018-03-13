package fr.iutmindfuck.tp6_async_http;


public interface TaskCallbackHandler {
    void onTaskStart();
    void onTaskCompleted(RequestType type, String data);
}
