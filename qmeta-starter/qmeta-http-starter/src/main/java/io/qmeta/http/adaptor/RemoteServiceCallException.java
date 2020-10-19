package io.qmeta.http.adaptor;


public class RemoteServiceCallException extends RuntimeException {

    public RemoteServiceCallException(Throwable t, String message)
    {
        super(message, t);
    }

    public RemoteServiceCallException(String message)
    {
        super(message);
    }
}
