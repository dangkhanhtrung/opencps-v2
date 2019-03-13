create table opencps_actionconfig (
	uuid_ VARCHAR(75) null,
	actionConfigId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	actionCode VARCHAR(75) null,
	actionName VARCHAR(75) null,
	extraForm BOOLEAN,
	formConfig TEXT null,
	sampleData TEXT null,
	insideProcess BOOLEAN,
	userNote INTEGER,
	syncType INTEGER,
	eventType INTEGER,
	infoType INTEGER,
	pending BOOLEAN,
	rollbackable BOOLEAN,
	notificationType VARCHAR(75) null,
	documentType VARCHAR(255) null,
	mappingAction VARCHAR(255) null,
	dateOption INTEGER
);

create table opencps_deliverable (
	uuid_ VARCHAR(75) null,
	deliverableId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	deliverableCode VARCHAR(75) null,
	deliverableName VARCHAR(75) null,
	deliverableType VARCHAR(75) null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(75) null,
	applicantIdNo VARCHAR(75) null,
	applicantName VARCHAR(75) null,
	subject VARCHAR(75) null,
	formData TEXT null,
	formScript TEXT null,
	formReport TEXT null,
	expireDate DATE null,
	issueDate DATE null,
	revalidate DATE null,
	deliverableState INTEGER,
	fileEntryId LONG,
	dossierId LONG,
	docSync INTEGER
);

create table opencps_deliverablelog (
	uuid_ VARCHAR(75) null,
	deliverableLogId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	deliverableId LONG,
	dossierUid VARCHAR(75) null,
	author VARCHAR(75) null,
	content TEXT null,
	deliverableAction INTEGER,
	actionDate DATE null,
	payload TEXT null
);

create table opencps_deliverabletype (
	uuid_ VARCHAR(75) null,
	deliverableTypeId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	typeCode VARCHAR(75) null,
	typeName VARCHAR(575) null,
	formScript TEXT null,
	formReport TEXT null,
	formScriptFileId LONG,
	formReportFileId LONG,
	codePattern VARCHAR(275) null,
	dataConfig TEXT null,
	tableConfig TEXT null,
	counter LONG,
	mappingData TEXT null,
	docSync INTEGER,
	govAgencies VARCHAR(255) null
);

create table opencps_deliverabletyperole (
	uuid_ VARCHAR(75) null,
	deliverableTypeRoleId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	deliverableTypeId LONG,
	roleId LONG,
	moderator BOOLEAN
);

create table opencps_documenttype (
	uuid_ VARCHAR(75) null,
	DocumentTypeId LONG not null primary key,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	typeCode VARCHAR(255) null,
	templateClass INTEGER,
	documentName VARCHAR(255) null,
	codePattern VARCHAR(255) null,
	documentScript TEXT null,
	docSync INTEGER
);

create table opencps_dossier (
	uuid_ VARCHAR(75) null,
	dossierId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	referenceUid VARCHAR(75) null,
	counter INTEGER,
	registerBookCode VARCHAR(100) null,
	registerBookName VARCHAR(75) null,
	dossierRegister VARCHAR(75) null,
	processNo VARCHAR(75) null,
	serviceCode VARCHAR(75) null,
	serviceName VARCHAR(75) null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(255) null,
	applicantName VARCHAR(500) null,
	applicantIdType VARCHAR(75) null,
	applicantIdNo VARCHAR(75) null,
	applicantIdDate DATE null,
	address TEXT null,
	cityCode VARCHAR(75) null,
	cityName VARCHAR(255) null,
	districtCode VARCHAR(75) null,
	districtName VARCHAR(255) null,
	wardCode VARCHAR(75) null,
	wardName VARCHAR(255) null,
	contactName TEXT null,
	contactTelNo VARCHAR(75) null,
	contactEmail VARCHAR(75) null,
	delegateName VARCHAR(75) null,
	delegateIdNo VARCHAR(75) null,
	delegateTelNo VARCHAR(75) null,
	delegateEmail VARCHAR(75) null,
	delegateAddress VARCHAR(75) null,
	delegateCityCode VARCHAR(75) null,
	delegateCityName VARCHAR(75) null,
	delegateDistrictCode VARCHAR(75) null,
	delegateDistrictName VARCHAR(75) null,
	delegateWardCode VARCHAR(75) null,
	delegateWardName VARCHAR(75) null,
	dossierTemplateNo VARCHAR(75) null,
	dossierTemplateName TEXT null,
	dossierNote TEXT null,
	submissionNote TEXT null,
	applicantNote TEXT null,
	briefNote TEXT null,
	dossierNo VARCHAR(255) null,
	submitting BOOLEAN,
	processDate DATE null,
	submitDate DATE null,
	receiveDate DATE null,
	dueDate DATE null,
	extendDate DATE null,
	releaseDate DATE null,
	finishDate DATE null,
	cancellingDate DATE null,
	correcttingDate DATE null,
	dossierStatus VARCHAR(75) null,
	dossierStatusText TEXT null,
	dossierSubStatus VARCHAR(75) null,
	dossierSubStatusText TEXT null,
	folderId LONG,
	dossierActionId LONG,
	viaPostal INTEGER,
	postalServiceCode VARCHAR(255) null,
	postalServiceName VARCHAR(255) null,
	postalAddress VARCHAR(255) null,
	postalCityCode VARCHAR(255) null,
	postalCityName VARCHAR(255) null,
	postalDistrictCode VARCHAR(255) null,
	postalDistrictName VARCHAR(255) null,
	postalWardCode VARCHAR(255) null,
	postalWardName VARCHAR(255) null,
	postalTelNo VARCHAR(75) null,
	password_ VARCHAR(75) null,
	notification BOOLEAN,
	online_ BOOLEAN,
	original BOOLEAN,
	serverNo VARCHAR(75) null,
	endorsementDate DATE null,
	lockState VARCHAR(200) null,
	originality INTEGER,
	originDossierId LONG,
	sampleCount LONG,
	durationUnit INTEGER,
	durationCount DOUBLE,
	dossierName VARCHAR(1000) null,
	originDossierNo VARCHAR(255) null
);

create table opencps_dossieraction (
	uuid_ VARCHAR(75) null,
	dossierActionId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	serviceProcessId LONG,
	previousActionId LONG,
	fromStepCode VARCHAR(255) null,
	fromStepName VARCHAR(255) null,
	fromSequenceNo VARCHAR(255) null,
	actionCode VARCHAR(100) null,
	actionUser VARCHAR(255) null,
	actionName VARCHAR(255) null,
	actionNote VARCHAR(500) null,
	actionOverdue INTEGER,
	syncActionCode VARCHAR(100) null,
	pending BOOLEAN,
	rollbackable BOOLEAN,
	stepCode VARCHAR(500) null,
	stepName VARCHAR(500) null,
	sequenceNo VARCHAR(255) null,
	dueDate DATE null,
	nextActionId LONG,
	payload TEXT null,
	stepInstruction VARCHAR(500) null,
	state_ INTEGER,
	eventStatus INTEGER
);

create table opencps_dossieractionsync (
	uuid_ VARCHAR(75) null,
	dossierActionSyncId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	dossierActionId LONG,
	createDossier BOOLEAN,
	referenceUid VARCHAR(75) null,
	actionCode VARCHAR(255) null,
	actionUser VARCHAR(500) null,
	actionNote VARCHAR(500) null
);

create table opencps_dossieractionuser (
	uuid_ VARCHAR(75) null,
	dossierActionId LONG not null,
	userId LONG not null,
	dossierId LONG,
	stepCode VARCHAR(255) null,
	moderator INTEGER,
	assigned INTEGER,
	visited BOOLEAN,
	primary key (dossierActionId, userId)
);

create table opencps_dossierdocument (
	uuid_ VARCHAR(75) null,
	DossierDocumentId LONG not null primary key,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	referenceUid VARCHAR(75) null,
	dossierActionId LONG,
	documentType VARCHAR(255) null,
	documentName VARCHAR(255) null,
	documentCode VARCHAR(100) null,
	documentFileId LONG,
	docSync INTEGER
);

create table opencps_dossierfile (
	uuid_ VARCHAR(75) null,
	dossierFileId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	referenceUid VARCHAR(75) null,
	dossierTemplateNo VARCHAR(255) null,
	dossierPartNo VARCHAR(255) null,
	dossierPartType INTEGER,
	fileTemplateNo VARCHAR(255) null,
	displayName VARCHAR(500) null,
	formData TEXT null,
	fileEntryId LONG,
	original BOOLEAN,
	eForm BOOLEAN,
	isNew BOOLEAN,
	removed BOOLEAN,
	signCheck INTEGER,
	signInfo TEXT null,
	formScript TEXT null,
	formReport TEXT null,
	formSchema TEXT null,
	deliverableCode VARCHAR(500) null
);

create table opencps_dossierlog (
	uuid_ VARCHAR(75) null,
	dossierLogId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	author VARCHAR(75) null,
	content TEXT null,
	notificationType VARCHAR(200) null,
	payload TEXT null
);

create table opencps_dossiermark (
	uuid_ VARCHAR(75) null,
	dossierMarkId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	dossierPartNo VARCHAR(255) null,
	fileCheck INTEGER,
	fileMark INTEGER,
	fileComment VARCHAR(75) null
);

create table opencps_dossierpart (
	uuid_ VARCHAR(75) null,
	dossierPartId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	templateNo VARCHAR(75) null,
	partNo VARCHAR(500) null,
	partName VARCHAR(500) null,
	partTip TEXT null,
	partType INTEGER,
	multiple BOOLEAN,
	formScript TEXT null,
	formReport TEXT null,
	sampleData TEXT null,
	required BOOLEAN,
	fileTemplateNo VARCHAR(500) null,
	eSign BOOLEAN,
	deliverableType VARCHAR(500) null,
	deliverableAction INTEGER,
	eForm BOOLEAN,
	fileMark INTEGER
);

create table opencps_dossierrequests (
	uuid_ VARCHAR(75) null,
	dossierRequestId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	referenceUid VARCHAR(75) null,
	requestType VARCHAR(75) null,
	comment_ VARCHAR(75) null,
	isNew INTEGER,
	statusReg INTEGER
);

create table opencps_dossierstatistic (
	uuid_ VARCHAR(75) null,
	dossierStatisticId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
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
	govAgencyCode VARCHAR(255) null,
	govAgencyName VARCHAR(255) null,
	domainCode VARCHAR(255) null,
	domainName VARCHAR(255) null,
	reporting BOOLEAN
);

create table opencps_dossiersync (
	uuid_ VARCHAR(75) null,
	DossierSyncId LONG not null primary key,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	dossierRefUid VARCHAR(75) null,
	syncRefUid VARCHAR(255) null,
	dossierActionId LONG,
	actionCode VARCHAR(75) null,
	actionName VARCHAR(75) null,
	actionUser VARCHAR(75) null,
	actionNote VARCHAR(75) null,
	syncType INTEGER,
	infoType INTEGER,
	payload TEXT null,
	serverNo VARCHAR(75) null,
	state_ INTEGER,
	retry INTEGER,
	messageText TEXT null,
	acknowlegement TEXT null
);

create table opencps_dossiertemplate (
	uuid_ VARCHAR(75) null,
	dossierTemplateId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	templateName STRING null,
	description TEXT null,
	templateNo VARCHAR(255) null
);

create table opencps_dossieruser (
	uuid_ VARCHAR(75) null,
	dossierId LONG not null,
	userId LONG not null,
	moderator INTEGER,
	visited BOOLEAN,
	primary key (dossierId, userId)
);

create table opencps_dynamicreport (
	uuid_ VARCHAR(75) null,
	dynamicReportId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	reportCode VARCHAR(75) null,
	reportName VARCHAR(75) null,
	sharing INTEGER,
	filterConfig VARCHAR(75) null,
	tableConfig VARCHAR(75) null,
	userConfig VARCHAR(75) null,
	reportType VARCHAR(75) null
);

create table opencps_menuconfig (
	uuid_ VARCHAR(75) null,
	menuConfigId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	menuGroup VARCHAR(75) null,
	menuName VARCHAR(75) null,
	order_ INTEGER,
	menuType INTEGER,
	queryParams VARCHAR(500) null,
	tableConfig TEXT null,
	buttonConfig TEXT null
);

create table opencps_menurole (
	uuid_ VARCHAR(75) null,
	menuRoleId LONG not null primary key,
	menuConfigId LONG,
	roleId LONG
);

create table opencps_paymentconfig (
	uuid_ VARCHAR(75) null,
	paymentConfigId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName TEXT null,
	govAgencyTaxNo VARCHAR(75) null,
	invoiceTemplateNo TEXT null,
	invoiceIssueNo TEXT null,
	invoiceLastNo TEXT null,
	invoiceForm TEXT null,
	bankInfo STRING null,
	epaymentConfig TEXT null
);

create table opencps_paymentfile (
	uuid_ VARCHAR(75) null,
	paymentFileId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	referenceUid VARCHAR(75) null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(500) null,
	paymentFee VARCHAR(500) null,
	advanceAmount LONG,
	feeAmount LONG,
	serviceAmount LONG,
	shipAmount LONG,
	paymentAmount LONG,
	paymentNote VARCHAR(500) null,
	epaymentProfile STRING null,
	bankInfo STRING null,
	paymentStatus INTEGER,
	paymentMethod VARCHAR(75) null,
	confirmDatetime DATE null,
	confirmPayload STRING null,
	confirmFileEntryId LONG,
	confirmNote VARCHAR(75) null,
	approveDatetime DATE null,
	accountUserName VARCHAR(500) null,
	govAgencyTaxNo VARCHAR(500) null,
	invoiceTemplateNo VARCHAR(500) null,
	invoiceIssueNo VARCHAR(500) null,
	invoiceNo VARCHAR(500) null,
	invoicePayload STRING null,
	einvoice STRING null
);

create table opencps_process_plugin (
	uuid_ VARCHAR(75) null,
	processPluginId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	stepCode VARCHAR(75) null,
	serviceProcessId LONG,
	pluginName VARCHAR(500) null,
	pluginType INTEGER,
	sequenceNo VARCHAR(75) null,
	pluginForm TEXT null,
	sampleData STRING null,
	autoRun BOOLEAN
);

create table opencps_processaction (
	uuid_ VARCHAR(75) null,
	processActionId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	serviceProcessId LONG,
	preStepCode STRING null,
	postStepCode STRING null,
	autoEvent STRING null,
	preCondition STRING null,
	actionCode VARCHAR(255) null,
	actionName VARCHAR(500) null,
	allowAssignUser INTEGER,
	assignUserId LONG,
	requestPayment INTEGER,
	paymentFee VARCHAR(500) null,
	createDossierFiles STRING null,
	returnDossierFiles STRING null,
	makeBriefNote VARCHAR(500) null,
	syncActionCode VARCHAR(75) null,
	rollbackable BOOLEAN,
	createDossierNo BOOLEAN,
	eSignature BOOLEAN,
	configNote TEXT null,
	dossierTemplateNo VARCHAR(255) null,
	signatureType VARCHAR(255) null,
	createDossiers VARCHAR(255) null,
	checkInput INTEGER
);

create table opencps_processoption (
	uuid_ VARCHAR(75) null,
	processOptionId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	serviceConfigId LONG,
	optionOrder INTEGER,
	optionName VARCHAR(500) null,
	autoSelect VARCHAR(500) null,
	dossierTemplateId LONG,
	serviceProcessId LONG,
	instructionNote TEXT null,
	submissionNote TEXT null,
	sampleCount LONG,
	registerBookCode VARCHAR(100) null
);

create table opencps_processsequence (
	uuid_ VARCHAR(75) null,
	processSequenceId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	serviceProcessId LONG,
	sequenceNo VARCHAR(255) null,
	sequenceName VARCHAR(512) null,
	sequenceRole VARCHAR(255) null,
	durationCount DOUBLE
);

create table opencps_processstep (
	uuid_ VARCHAR(75) null,
	processStepId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	stepCode VARCHAR(255) null,
	serviceProcessId LONG,
	stepName VARCHAR(500) null,
	sequenceNo VARCHAR(255) null,
	dossierStatus VARCHAR(75) null,
	dossierSubStatus VARCHAR(75) null,
	durationCount DOUBLE,
	customProcessUrl VARCHAR(255) null,
	stepInstruction TEXT null,
	briefNote TEXT null,
	editable BOOLEAN,
	restrictDossier VARCHAR(255) null,
	lockState VARCHAR(255) null,
	groupName VARCHAR(255) null,
	roleAsStep VARCHAR(255) null,
	checkInput INTEGER
);

create table opencps_processsteprole (
	uuid_ VARCHAR(75) null,
	processStepId LONG not null,
	roleId LONG not null,
	roleCode VARCHAR(255) null,
	roleName VARCHAR(255) null,
	moderator BOOLEAN,
	condition_ TEXT null,
	primary key (processStepId, roleId)
);

create table opencps_publish_queue (
	uuid_ VARCHAR(75) null,
	publishQueueId LONG not null primary key,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	dossierId LONG,
	serverNo VARCHAR(255) null,
	status INTEGER,
	retry INTEGER,
	publishType INTEGER,
	publishData TEXT null,
	messageText TEXT null,
	acknowlegement TEXT null
);

create table opencps_registration (
	uuid_ VARCHAR(75) null,
	registrationId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	applicantName VARCHAR(275) null,
	applicantIdType VARCHAR(75) null,
	applicantIdNo VARCHAR(75) null,
	applicantIdDate DATE null,
	address VARCHAR(575) null,
	cityCode VARCHAR(75) null,
	cityName VARCHAR(275) null,
	districtCode VARCHAR(75) null,
	districtName VARCHAR(275) null,
	wardCode VARCHAR(75) null,
	wardName VARCHAR(275) null,
	contactName VARCHAR(275) null,
	contactTelNo VARCHAR(275) null,
	contactEmail VARCHAR(275) null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(275) null,
	registrationState INTEGER,
	registrationClass VARCHAR(75) null,
	submitting BOOLEAN,
	representativeEnterprise VARCHAR(255) null
);

create table opencps_registrationform (
	uuid_ VARCHAR(75) null,
	registrationFormId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	registrationId LONG,
	referenceUid VARCHAR(75) null,
	formNo VARCHAR(75) null,
	formName VARCHAR(275) null,
	formData TEXT null,
	formScript TEXT null,
	formReport TEXT null,
	fileEntryId LONG,
	isNew BOOLEAN,
	removed BOOLEAN
);

create table opencps_registrationlog (
	uuid_ VARCHAR(75) null,
	registrationLogId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	registrationId LONG,
	author TEXT null,
	content TEXT null,
	payload TEXT null
);

create table opencps_registrationtemplate (
	uuid_ VARCHAR(75) null,
	registrationTemplateId LONG not null primary key,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	govAgencyCode VARCHAR(75) null,
	govAgencyName VARCHAR(255) null,
	formNo VARCHAR(75) null,
	formName VARCHAR(75) null,
	multiple BOOLEAN,
	formScript TEXT null,
	formReport TEXT null,
	sampleData TEXT null
);

create table opencps_serviceconfig (
	uuid_ VARCHAR(75) null,
	serviceConfigId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	serviceInfoId LONG,
	govAgencyCode VARCHAR(75) null,
	govAgencyName STRING null,
	serviceInstruction TEXT null,
	serviceLevel INTEGER,
	serviceUrl VARCHAR(500) null,
	forCitizen BOOLEAN,
	forBusiness BOOLEAN,
	postService BOOLEAN,
	registration BOOLEAN
);

create table opencps_serviceinfo (
	uuid_ VARCHAR(75) null,
	serviceInfoId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	serviceCode VARCHAR(75) null,
	serviceName STRING null,
	processText TEXT null,
	methodText TEXT null,
	dossierText TEXT null,
	conditionText TEXT null,
	durationText TEXT null,
	applicantText TEXT null,
	resultText TEXT null,
	regularText TEXT null,
	feeText TEXT null,
	administrationCode VARCHAR(75) null,
	administrationName VARCHAR(500) null,
	administrationIndex VARCHAR(75) null,
	domainCode VARCHAR(75) null,
	domainName TEXT null,
	domainIndex VARCHAR(75) null,
	maxLevel INTEGER,
	public_ BOOLEAN
);

create table opencps_serviceprocess (
	uuid_ VARCHAR(75) null,
	serviceProcessId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	processNo VARCHAR(255) null,
	processName STRING null,
	description TEXT null,
	durationCount DOUBLE,
	durationUnit INTEGER,
	counter LONG,
	generateDossierNo BOOLEAN,
	dossierNoPattern TEXT null,
	generateDueDate BOOLEAN,
	dueDatePattern VARCHAR(500) null,
	generatePassword BOOLEAN,
	directNotification BOOLEAN,
	serverNo VARCHAR(255) null,
	serverName TEXT null,
	requestPayment BOOLEAN,
	paymentFee VARCHAR(255) null
);

create table opencps_serviceprocessrole (
	uuid_ VARCHAR(75) null,
	serviceProcessId LONG not null,
	roleId LONG not null,
	roleCode VARCHAR(255) null,
	roleName VARCHAR(255) null,
	moderator BOOLEAN,
	condition_ VARCHAR(75) null,
	primary key (serviceProcessId, roleId)
);

create table opencps_services_filetemplates (
	uuid_ VARCHAR(75) null,
	serviceInfoId LONG not null,
	fileTemplateNo VARCHAR(75) not null,
	templateName VARCHAR(1000) null,
	fileEntryId LONG,
	primary key (serviceInfoId, fileTemplateNo)
);

create table opencps_statistic (
	uuid_ VARCHAR(75) null,
	dossierStatisticId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
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
	govAgencyCode VARCHAR(512) null,
	groupAgencyCode VARCHAR(512) null,
	govAgencyName STRING null,
	domainCode VARCHAR(512) null,
	domainName STRING null,
	reporting BOOLEAN,
	overtimeInside INTEGER,
	overtimeOutside INTEGER,
	interoperatingCount INTEGER,
	waitingCount INTEGER,
	outsideCount INTEGER,
	insideCount INTEGER
);

create table opencps_stepconfig (
	uuid_ VARCHAR(75) null,
	stepConfigId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	stepCode VARCHAR(75) null,
	stepName VARCHAR(75) null,
	stepType INTEGER,
	dossierStatus VARCHAR(75) null,
	dossierSubStatus VARCHAR(75) null,
	menuGroup VARCHAR(75) null,
	menuStepName VARCHAR(75) null,
	buttonConfig TEXT null
);

create table opencps_userinfolog (
	uuid_ VARCHAR(75) null,
	userLogId LONG not null primary key,
	userId LONG,
	createDate DATE null,
	payload VARCHAR(75) null
);

create table opencps_voting_statistic (
	uuid_ VARCHAR(75) null,
	votingStatisticId LONG not null primary key,
	companyId LONG,
	groupId LONG,
	userId LONG,
	userName VARCHAR(255) null,
	createDate DATE null,
	modifiedDate DATE null,
	month INTEGER,
	year INTEGER,
	govAgencyCode VARCHAR(255) null,
	govAgencyName STRING null,
	domainCode VARCHAR(255) null,
	domainName STRING null,
	votingCode VARCHAR(255) null,
	votingSubject STRING null,
	totalVoted INTEGER,
	veryGoodCount INTEGER,
	goodCount INTEGER,
	badCount INTEGER,
	percentVeryGood INTEGER,
	percentGood INTEGER,
	percentBad INTEGER,
	totalCount INTEGER
);