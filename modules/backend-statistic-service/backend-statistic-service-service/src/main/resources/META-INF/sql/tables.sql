create table opencps_statistic (
	uuid_ VARCHAR(75) null,
	dossierStatisticId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	month INTEGER,
	year INTEGER,
	totalCount INTEGER,
	deniedCount INTEGER,
	cancelledCount INTEGER,
	processCount INTEGER,
	remainingCount INTEGER,
	receivedCount INTEGER,
	onlineCount INTEGER,
	onegateCount INTEGER,
	releaseCount INTEGER,
	betimesCount INTEGER,
	ontimeCount INTEGER,
	overtimeCount INTEGER,
	doneCount INTEGER,
	releasingCount INTEGER,
	unresolvedCount INTEGER,
	processingCount INTEGER,
	undueCount INTEGER,
	overdueCount INTEGER,
	pausingCount INTEGER,
	ontimePercentage INTEGER,
	govAgencyCode VARCHAR(75) null,
	groupAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(75) null,
	domainCode VARCHAR(75) null,
	domainName VARCHAR(75) null,
	reporting BOOLEAN,
	overtimeInside INTEGER,
	overtimeOutside INTEGER,
	interoperatingCount INTEGER,
	waitingCount INTEGER,
	outsideCount INTEGER,
	insideCount INTEGER
);