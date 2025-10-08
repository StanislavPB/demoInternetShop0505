package org.demointernetshop0505.service.exception;

public class MailSendingException extends RuntimeException {
    public MailSendingException(String message) {
        super(message);
    }
}
