package vn.prostylee.app.service;

import vn.prostylee.app.dto.request.ContactFormRequest;

public interface SupportService {

    boolean supportFromContactForm(ContactFormRequest request);
}
