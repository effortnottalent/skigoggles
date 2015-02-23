package com.ymdrech.testandroidprodge;

/**
 * Created by e4t on 2/23/2015.
 */
public class BadRouteException extends RuntimeException {
    public BadRouteException() {
    }

    public BadRouteException(String detailMessage) {
        super(detailMessage);
    }

    public BadRouteException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BadRouteException(Throwable throwable) {
        super(throwable);
    }
}
