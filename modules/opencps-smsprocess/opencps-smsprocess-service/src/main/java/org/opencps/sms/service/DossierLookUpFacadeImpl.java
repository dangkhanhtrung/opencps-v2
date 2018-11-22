
package org.opencps.sms.service;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.HashMap;

import org.opencps.sms.service.application.SMSGateway;
import org.opencps.sms.service.dto.DossierRequest;
import org.opencps.sms.service.dto.DossierResponse;
import org.opencps.sms.service.util.Constants;
import org.opencps.sms.service.util.ServiceProps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import opencps.statistic.common.webservice.exception.UpstreamServiceFailedException;
import opencps.statistic.common.webservice.exception.UpstreamServiceTimedOutException;
import opencps.statistic.common.webservice.facade.OpencpsRestFacade;

public class DossierLookUpFacadeImpl extends OpencpsRestFacade<DossierRequest, DossierResponse>
    implements DossierLookUpFacade<DossierRequest, DossierResponse> {

    private static final Log _log = LogFactoryUtil.getLog(DossierLookUpFacadeImpl.class);

    @Override
    public DossierResponse callDossierRestService(DossierRequest payload)
        throws UpstreamServiceTimedOutException, UpstreamServiceFailedException {

        return makeServiceCall(payload);
    }

    @Override
    protected DossierResponse makeServiceCall(DossierRequest payload)
        throws UpstreamServiceTimedOutException, UpstreamServiceFailedException {

        MultiValueMap<String, String> urlQueryParams = new LinkedMultiValueMap<>();

        urlQueryParams.add("start", Integer.toString(payload.getStart()));
        urlQueryParams.add("end", Integer.toString(payload.getEnd()));
        urlQueryParams.add("dossierNo", payload.getDossierNo());
        urlQueryParams.add("applicantIdNo", payload.getApplicantIdNo());

        String endPoint = ServiceProps.get(Constants.OPENCPS_REST_ENDPOINT_DOSSIER);
        HashMap<String, String> urlPathSegments = new HashMap<>();
        String url = buildUrl(endPoint, urlPathSegments, urlQueryParams);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders = setHttpHeadersAuthorization(
            httpHeaders, ServiceProps.get(Constants.OPENCPS_BACKEND_USERNAME),
            ServiceProps.get(Constants.OPENCPS_BACKEND_PASSWORD));

        httpHeaders.add(Constants.GROUP_ID, ServiceProps.get(Constants.OPENCPS_GROUP_ID_CONFIG));
        
        return (DossierResponse) this.executeGenericRestCall(
            url, HttpMethod.GET, httpHeaders, payload, DossierResponse.class).getBody();
    }

}