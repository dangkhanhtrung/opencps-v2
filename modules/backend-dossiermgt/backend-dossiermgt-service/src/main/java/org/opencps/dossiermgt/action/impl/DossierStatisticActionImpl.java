package org.opencps.dossiermgt.action.impl;

import java.util.LinkedHashMap;

import org.opencps.dossiermgt.action.DossierStatistic;
import org.opencps.dossiermgt.service.DossierStatisticLocalServiceUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.ServiceContext;

public class DossierStatisticActionImpl implements DossierStatistic {

	private Log _log = LogFactoryUtil.getLog(DossierStatisticActionImpl.class);

	@Override
	public org.opencps.dossiermgt.model.DossierStatistic insertDossierStatistic(
			org.opencps.dossiermgt.model.DossierStatistic dossierStatistic) {
		return DossierStatisticLocalServiceUtil.addDossierStatistic(dossierStatistic);
	}

	@Override
	public org.opencps.dossiermgt.model.DossierStatistic updateDossierStatistic(
			org.opencps.dossiermgt.model.DossierStatistic dossierStatistic) {
		return DossierStatisticLocalServiceUtil.updateDossierStatistic(dossierStatistic);
	}

	@Override
	public org.opencps.dossiermgt.model.DossierStatistic deleteDossierStatistic(long dossierStatisticId)
			throws PortalException {
		return DossierStatisticLocalServiceUtil.deleteDossierStatistic(dossierStatisticId);
	}

	@Override
	public JSONObject getDossierStatistic(long userId, long companyId, long groupId, LinkedHashMap<String, Object> params,
			Sort[] sorts, int start, int end, ServiceContext serviceContext) {
		JSONObject result = JSONFactoryUtil.createJSONObject();

		Hits hits = null;

		SearchContext searchContext = new SearchContext();
		searchContext.setCompanyId(companyId);

		try {

			hits = DossierStatisticLocalServiceUtil.searchLucene(params, sorts, start, end, searchContext);

			result.put("data", hits.toList());
			result.put("total", hits.toList().size());

		} catch (Exception e) {
			_log.error(e);
		}

		return result;
	}

}
