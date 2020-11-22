package vn.prostylee.business.service;

import vn.prostylee.business.dto.request.ContactFormRequest;

public interface SupportService {

    boolean supportFromContactForm(ContactFormRequest request);
}
