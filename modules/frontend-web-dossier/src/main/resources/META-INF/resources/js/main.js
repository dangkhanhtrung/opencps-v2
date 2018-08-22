

var funLoadVue = function(stateWindowParam, dossierIdParam, dossierPartNo, emailAddress, agencies){
	const config = {
		headers: {
			'groupId': themeDisplay.getScopeGroupId()
		}
	};

	const configPage = {
		pageSize: 15,
		serviceinfosQuocTe: 'BGTVT0600001,BGTVT0600002,BGTVT0600003,BGTVT0600004,BGTVT0600019,BGTVT0600020,BGTVT0600021,BGTVT0600022',
		serviceinfosLienVan: 'BGTVT0600005,BGTVT0600006,BGTVT0600007,BGTVT0600008,BGTVT0600009,BGTVT0600010,BGTVT0600011,BGTVT0600012,BGTVT0600023,BGTVT0600024,BGTVT0600025,BGTVT0600026,BGTVT0600027,BGTVT0600028,BGTVT0600029,BGTVT0600030,BGTVT0600031,BGTVT0600032,BGTVT0600033,BGTVT0600034,BGTVT0600035,BGTVT0600036,BGTVT0600037,BGTVT0600038,BGTVT0600039,BGTVT0600040,BGTVT0600041,BGTVT0600042,BGTVT0600043,BGTVT0600044',
		serviceinfosChapThuan: 'BGTVT0600013,BGTVT0600014,BGTVT0600015,BGTVT0600016,BGTVT0600017,BGTVT0600030,BGTVT0600031,BGTVT0600032,BGTVT0600033,BGTVT0600034'

	};

	const MAX_RETRY = 200;
	var nOfRetry = 0;
	
	var dossierViewJX = new VueJX({
		el: 'dossierViewJX',
		pk: 1,
		groupid: themeDisplay.getScopeGroupId(),
		data: {
			currentPrintTemplate: {},
			popUpPrintTraCuu: false,
			offsetTop: 0,
			stageFilterView: null,
			detailPage: false,
			detailRegistPage: false,
			viewmore: false,
			detailModel: {},
			detailRegistModel: {},
			xem_them: 'Không tìm thấy hồ sơ nào',
			hoso_title_table: 'Danh sách hồ sơ',
			processSteps: [],
			stepModel: null,
			showContactDetail: false,
			dossierFiles: [],
			dossierFilesView: [],
			indexFile: 0,
			statusParamFilter: null,
			substatusParamFilter: null,
			loadingAlpacajsForm: false,
			stepLoading: false,
			actionsSubmitLoading: false,
			popupResultFile: false,
			traCuuFilter: false,
			registForms: [],
			alapcaJSRei: {},
			menu: false,
			dateFormatted: null,
			advanced_filter: false,
			alpacaAssignUserId: 0,
			subUsers: [],
			currentCounter : 0,
			currentCounterTemp : 0,
			listgroupHoSoFilterselectedIndex: -1,
			advancedFilterServiceInfo : {}, 
			advancedFilterLoaiSanPham : {},
			advancedFilterNhanHieu : {},
			advancedFilterDossierStatus : {},
			stateOnlyFollow : false,
			menuTuNgay: '',
			menuDenNgay: '',
			modelLienVan: {
				so_dang_ky: 'asdfgh',
				historys: [
				{
					loai: 1,
					cua_khau: '12345678',
					thong_tin_lai_xe: 'asdfghjasdfgh',
					giay_phep_lai_xe: 'asdfghjasdfgh',
					thoi_gian: '1/1/2018'
				}
				]
			},
			modelVehicle: {
				registrationNumber: '',
				capacity: '',
				manufacturedYear: '',
				trademarkCode: '',
				trademarkName: '',
				chassisNumber: '',
				engineNumber: '',
				vehicleColor: ''
			},
		    vehicleHeaders: [
		        { text: 'Thông tin', value: 'name' },
		        { text: 'Giá trị', value: 'value' }
		    ],
		    vehicleItems: [
		    ],		
			giayPhepVanTaiQuocTeTableheaders: [
			{
				text: 'STT',
				align: 'center',
				sortable: false,
				value: 'stt'
			},
			{
				text: 'Tên giấy phép. Gíây phép - Số giấy phép',
				align: 'left',
				sortable: true,
				value: 'deliverableCode'
			},
			{
				text: 'Tên doanh nghiệp được cấp phép',
				align: 'left',
				sortable: true,
				value: 'applicantName'
			},
			{
				text: 'Cơ quan cấp',
				align: 'left',
				sortable: true,
				value: 'coQuan'
			},
			{
				text: 'Hiệu lực giấy phép',
				align: 'left',
				sortable: true,
				value: 'hieuLuc'
			},
			{
				text: 'Thao tác',
				align: 'left',
				sortable: true,
				value: 'thaoTac'
			}
			],
			giayPhepVanTaiQuocTeTableItems: [],
			pageGiayPhepVanTaiQuocTeTableLength: 0,
			pageGiayPhepVanTaiQuocTeTable: 1,

			giayPhepLienVanTableheaders: [
			{
				text: 'STT',
				align: 'center',
				sortable: false,
				value: 'stt'
			},
			{
				text: 'Tên giấy phép. Gíây phép - Số giấy phép',
				align: 'left',
				sortable: true,
				value: 'deliverableCode'
			},
			{
				text: 'Số đăng ký phương tiện Đơn vị khai thác',
				align: 'left',
				sortable: true,
				value: 'applicantName'
			},
			{
				text: 'Cơ quan cấp phép',
				align: 'left',
				sortable: true,
				value: 'coQuan'
			},
			{
				text: 'Hiệu lực giấy phép',
				align: 'left',
				sortable: true,
				value: 'hieuLuc'
			},
			{
				text: 'Thao tác',
				align: 'left',
				sortable: true,
				value: 'thaoTac'
			}


			],
			giayPhepLienVanTableItems: [],
			pageGiayPhepLienVanTableLength: 0,
			pageGiayPhepLienVanTable: 1,

			chapThuanKhaiThacTableheaders: [
			{
				text: 'STT',
				align: 'center',
				sortable: false,
				value: 'stt'
			},
			{
				text: 'Tên giấy phép. Gíây phép - Số giấy phép',
				align: 'left',
				sortable: true,
				value: 'so_giay_phep'
			},
			{
				text: 'Doanh nghiệp đăng ký khai thác',
				align: 'left',
				sortable: true,
				value: 'doanh_nghiep'
			},
			{
				text: 'Tuyến khai thác',
				align: 'left',
				sortable: true,
				value: 'tuyen_khai_thac'
			},
			{
				text: 'Cơ quan cấp phép',
				align: 'left',
				sortable: true,
				value: 'co_quan'
			},
			{
				text: 'Số xe tham gia Hiệu lực khai thác',
				align: 'left',
				sortable: true,
				value: 'hieu_luc'
			},
			{
				text: 'Thao tác',
				align: 'left',
				sortable: true,
				value: 'thao_tac'
			}
			],
			chapThuanKhaiThacTableItems: [],
			pageChapThuanKhaiThacTableLength: 0,
			pageChapThuanKhaiThacTable: 1,

			stateTraCuuGiayPhep: 'giay_phep_van_tai_quoc_te',
			detailTraCuuGiayPhepPage: false,
			serviceInfos: [
				{
					serviceCode: 111,
					serviceName: 'thu tuc1'
				},
				{
					serviceCode: 1222,
					serviceName: 'thu tuc2'
				},
				{
					serviceCode: 333,
					serviceName: 'thu tuc3'
				}
			],
			govAgencys: [
			{
				govAgencyName: 1233,
				govAgencyCode: '123'
			},
			{
				govAgencyName: 1234,
				govAgencyCode: '123'
			},
			{
				govAgencyName: 1235,
				govAgencyCode: '123'
			}
			],
			tuyens: [
			{
				itemCode: 1,
				itemName: 'asdf'
			},
			{
				itemCode: 1,
				itemName: 'asdf'
			},
			{
				itemCode: 1,
				itemName: 'asdf'
			},
			{
				itemCode: 1,
				itemName: 'asdf'
			}
			],
			keywordTraCuuGiayPhep: '',
			searchTuNgay: '',
			searchDenNgay: '',
			hinhThucSelect: '',
			hinhThucs: [
				{
					itemCode: 'XC',
					itemName: 'Xuất cảnh'
				},
				{
					itemCode: 'NC',
					itemName: 'Nhập cảnh'
				}
			],
			serviceInfoSelect: '',
			govAgencySelect: '',
			tuyenSelect: '',
			cuaKhaus: [
				{
					itemCode: 1,
					itemName: 'asdf'
				},
				{
					itemCode: 2,
					itemName: 'asdf'
				},
				{
					itemCode: 3,
					itemName: 'asdf'
				}
			],
			cuaKhauSelect: '',
			menuThongTinXeDate: '',
			thongTinXeDate: '',
			thong_tin_lai_xe: '',
			giay_phep_lai_xe: '',
			pageHistory: 1,
			lengthPageHistory: 0,
			gradient: 'to top right, rgba(63,81,181, .7), rgba(25,32,72, .7)',
			formTemplate: {},
			thongTinXeDatePK: '',
			popUpThongTinXe: false,
			addHistoryCar: false,
			historyLienVan: [],
			dialogThuLyHoSo: false,
			dialogSearch: false,
			keywordsSearch: '',
			startDate: null,
			startDateStr: '',
			startDateMenu: '',
			endDate: null,
			endDateStr: '',
			endDateMenu: '',
			dossierIdCTN: '',
			dossierNo: '',
			serviceInfo: null,
			applicantIdNo: null,
			dossierStatus: '',
			dossierStatusItems: [],
			dialogViewFile: false,
			listDocumentInItems: [],
			listDocumentOutItems: [],
			tabs: [
				{
					text: 'Tra cứu hồ sơ',
					value: 'tab1',
					state: 'tat_ca_ho_so'
				},
				{
					text: 'Xử lý hồ sơ',
					value: 'tab2',
					state: 'xu_ly_ho_so'
				},
				{
					text: 'Quản lý giấy phép',
					value: 'tab3',
					state: 'quan_ly_giay_phep'
				},
				{
					text: 'Quản lý phương tiện',
					value: 'tab4',
					state: ''
				}
			],
			tabActive: 'tab2',
			listgroupHoSoFilterItems: [],
			listgroupTraCuuFilterItems: [],
			serviceInfoFilterItems: [],
			applicantNameFilterItems: [],
			maxWidthDialog: '600px',
			blobFileSrc: '',
			indexListStatus: -1,
			loadingDanhSachHoSoTable: false,
			listHistoryProcessingItems: [],
			dialogViewLogs: false
		},
		watch: {
			pageGiayPhepVanTaiQuocTeTable: {
				handler () {
                    this._inigiayPhepVanTaiQuocTeTable()
                }
			},
			pageGiayPhepLienVanTable: {
				handler () {
                    this._inigiayPhepLienVanTable()
                }
			},
			pageChapThuanKhaiThacTable: {
				handler () {
                    this._inichapThuanKhaiThacTable()
                }
			},
			pageHistory: {
				handler () {
                    this.toDetailThongTinXe()
                }
			}
//			stateButtonregistration : true
		},
		onScroll: 'onScroll',
		schema: {
				// TODO menu filter
				'navigationFilter': {
					'id': 'navigationFilter',
					'name': 'navigationFilter',
					"type": "navigation",
					'cssClass': 'pr-4 pt-0',
					"template": "menu_template",
					"onLoad": "initData",
					"template_content": "dossierViewJX_form_template",
					'events': {
						_initApplicantNameFilterData: function(){
							var vm = this;
							
							//TODO: API
							var url = '/o/rest/v2/applicants';
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;

								vm.applicantNameFilterItems = serializable.data;
								
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						},
						_initServiceInfoFilterData: function(){
							var vm = this;
							
							var url = '/o/rest/v2/serviceinfos';
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;

								vm.serviceInfoFilterItems = serializable.data;

							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
							
						},
						groupHoSoFilter: function(item, index){
							var vm = this;

							console.log("item status",item);
							vm.indexListStatus = index
							vm.reloadCounter();


							vm.detailPage = false;
							vm.detailRegistPage = false;
							vm.listgroupHoSoFilterselected = item.id;
							vm.danhSachHoSoTablepage = 1;

							vm.listgroupHoSoFilterselectedIndex = item.index - 1;

							if ( item.id !== 'tra_cuu' ){
								vm.stageFilterView = item.id;
								vm.hoso_title_table = item.title;
								if (item.level === 1) {
									vm.statusParamFilter = item.idSub;
									vm.substatusParamFilter = item.id;
								} else {
									vm.statusParamFilter = item.id;
									vm.substatusParamFilter = item.idSub;
								}

							} 

							if (item.id == 'tra_cuu_hoso') {
								vm._initraCuuHoSoTable(false);
							} else if (item.id == 'tra_cuu_phuong_tien') {
								// TODO vm._inidanhSachHoSoTable(false);
							} else if (item.id == 'tra_cuu_thong_tin_doanh_nghiep') {
								vm._inithongTinDoanhNghiepTable(false);
								vm.detailRegistPage = false;
							} else {
								vm._inidanhSachHoSoTable(false);
							}
							vm.onScrollTop();

						},
						parseDate2: function (date) {
							if (!date) return null

								const [month, day, year] = date.split('/')
							return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
						},
						reloadCounter : function(){
							var vm = this;

							vm.stageFilterView = 'danh_sach';

							/*var url = '/o/rest/v2/statistics/dossiers/todo';*/
							var url = '/o/rest/v2/statistics/dossiers/todo';
							console.log("Init load");
							$.ajax({
								url : url,
								dataType : "json",
								type : "GET",
								headers : {
									'groupId': themeDisplay.getScopeGroupId(),
								},
								data: {
									service: vm.serviceInfo,
									agency: agencies
								},								
								success : function(result){
									var serializable = result;

									var indexTree = -1;
									var index = 0;
									//console.log("listgroupHoSoFilterItems=======FISRT",vm.listgroupHoSoFilterItems);
									//console.log("serializable=======",serializable.data);
									for (var key in serializable.data) {
										for(var i in vm.listgroupHoSoFilterItems){
											if ( serializable.data[key].level === 0) {

												if (serializable.data[key].dossierStatus === 'cancelling' ||
													serializable.data[key].dossierStatus === 'cancelled' ||
													serializable.data[key].dossierStatus === 'processing' ||
													serializable.data[key].dossierStatus === 'paid') {
													serializable.data[key].items = [];

												if(serializable.data[key].dossierStatus === vm.listgroupHoSoFilterItems[i].id){
													vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
												}
											}

										} else {

											if(serializable.data[key].dossierSubStatus === vm.listgroupHoSoFilterItems[i].id){
												vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
											}
										}
									}

								}
								//console.log("listgroupHoSoFilterItems=======LAST",vm.listgroupHoSoFilterItems);

							},
							error : function(result){

							}
						});	
							
						},
						menuTabChange: function (item) {
							var vm = this;
							if (item.state === 'tat_ca_ho_so') {
								vm.stageFilterView = 'danh_sach'
								vm.detailPage = false;
								vm.detailRegistPage = false;
								vm.listgroupHoSoFilterselected = item.id;
								vm.danhSachHoSoTablepage = 1;

								vm.listgroupHoSoFilterselectedIndex = item.index - 1;

								vm.statusParamFilter = '';
								vm.substatusParamFilter = '';

								vm._inidanhSachHoSoTable(false);

								vm.onScrollTop();
							} else if (item.state === 'quan_ly_giay_phep') {
								vm.stageFilterView = 'tra_cuu_giay_phep'
								vm.detailRegistPage = false;
								vm.detailPage = false;
								vm._inigiayPhepVanTaiQuocTeTable(false);
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosQuocTe
								});
								vm._initGovAgencys({

								});
								vm._initTuyens();
							} else {
								vm.detailRegistPage = false;
								vm.detailPage = false;
								vm.stageFilterView = 'danh_sach'
							}
						},
						viewDialogLog: function (item) {
							var vm = this;
							vm.detailModel = item;
							vm.dialogViewLogs = true;
							vm.listHistoryProcessingItems = [];
							vm.selectDossierActionTab();
						},
						_initDossierStatus: function (argument) {
							var vm = this;
							var url = '/o/rest/v2/dictcollections/DOSSIER_STATUS/dictitems';
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								vm.dossierStatusItems = serializable.data;
							})
							.catch(function (error) {
								console.log(error);
							});
						},
						_initlistgroupHoSoFilter: function(select){
							var vm = this;

							// vm.newTabOrNewWindown();
							// vm.setStateOnlyFollow(emailAddress);
							vm.getLastedStateUserInfo(function(){
								vm._inidanhSachHoSoTable(false);
							});

							vm.stageFilterView = 'danh_sach';

							vm.listgroupHoSoFilterItems = [];

							/*var url = '/o/rest/v2/statistics/dossiers/todo';*/
							var url = '/o/rest/v2/statistics/dossiers/todo';
							console.log("Init load 2");
							axios.get(url, config).then(function (response) {
								var serializable = response.data;

								var indexTree = -1;
								var index = 0;
								for (var key in serializable.data) {
									
									if ( serializable.data[key].level === 0) {
										
										if (serializable.data[key].dossierStatus === 'cancelling' ||
											serializable.data[key].dossierStatus === 'cancelled' ||
											serializable.data[key].dossierStatus === 'processing' ||
											serializable.data[key].dossierStatus === 'paid') {
											serializable.data[key].items = [];

										vm.listgroupHoSoFilterItems.push({
											id: serializable.data[key].dossierStatus,
											idSub: serializable.data[key].dossierSubStatus,
											title: serializable.data[key].statusName,
											level: serializable.data[key].level,
											count: serializable.data[key].count,

											action: 'folder',
											action_active: 'folder_open',
											items: [],
											index: index
										});
									}

								} else {
									
									vm.listgroupHoSoFilterItems.push({
										id: serializable.data[key].dossierSubStatus,
										idSub: serializable.data[key].dossierStatus,
										title: serializable.data[key].statusName,
										level: serializable.data[key].level,
										count: serializable.data[key].count,
										action: 'folder',
										action_active: 'folder_open',
										items: [],
										index: index
									});
								}
								
							}

							if(select){
								console.log("selected========",select);
								vm.listgroupHoSoFilterselected = select;
							}


						})
							.catch(function (error) {
								console.log(error);
								
							});
							return false; 
						},
						initData: function () {
							var vm = this
							vm._initlistgroupHoSoFilter();
							vm._initServiceInfoFilterData();
							vm._initApplicantNameFilterData();
							vm._initDossierStatus();
							$("aside").remove();
						},
						deleteDossierFileVersion: function (item) {
							var vm = this;
							
							vm.$dialog.confirm('Bạn có muốn xóa file toàn bộ file của thành phần này!', {
								html: true,
								loader: true,
								okText: 'Xác nhận',
								cancelText: 'Quay lại',
								animation: 'fade'
							})
							.then((dialog) => {
								console.log("Run delete");
									// call API get file by dossierId

									var urlFiles = "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid+"/resetformdata";
									
									$.ajax({
										url : urlFiles,
										dataType : "json",
										type : "PUT",
										headers : {
											groupId : themeDisplay.getScopeGroupId()
										},
										success : function(result){
											item.counter = 0;
										},
										error : function(xhr){
											console.log(xhr);
										}
									});

									/*axios.put(urlFiles, config).then(function (response) {
										item.counter = 0;
										
									})
									.catch(function (error) {
										console.log(error);
										
									});*/
									
//									axios.put(urlFiles, "", config).then(function (response) {
//										item.counter = 0;
//										
//										
//									})
//									.catch(function (error) {
//										console.log(error);
//										
//									});
									
									dialog.close();
									return false; 
								})
							.catch((e) => {
								console.log(e)
							})
						},
						setStateOnlyFollow : function(emailAddress){
							var vm = this;
							var url = '/o/rest/v2/dictcollections/VR_AUDIT/dictitems';
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								console.log("emailAddress======",emailAddress);
								if(serializable){
									for (var i = 0; i < serializable.data.length; i++) {
										var model = serializable.data[i];
										if(emailAddress && emailAddress	=== model.itemName){

											vm.stateOnlyFollow = true;
											break;
										}
									}
								}
								
							})
							.catch(function (error) {
								console.log(error);

							});
						},
						viewDossierFileVersionDialog: function (item) {
							var vm = this;
							var vm = this;
							if (item['counter'] <= 0) {
								return
							}
							vm.dossierViewJXTitle = item.displayName;
							let arrFileViews = []
							vm.indexFile = 0;
							vm.blobFileSrc = '';
							if (vm.dossierFiles.length > 0) {
								console.log('dossierFiles--------', vm.dossierFiles);
								console.log('item--------', item);
								arrFileViews = vm.dossierFiles.filter(file => {
									return file.dossierPartNo === item.partNo
								})
								if (arrFileViews !== undefined && arrFileViews['length'] > 0) {
									vm.dossierFilesView = arrFileViews
								} else {
									vm.dossierFilesView = []
								}
							}
							
							if (vm.dossierFilesView.length > 0) {
								// let arrTemp = []
								// for (var i = 0; i < vm.dossierFilesView.length; i++) {
								// 	arrTemp.push(vm.getBlobFile(vm.dossierFilesView[i]));
								// }
								// Promise.all(arrTemp).then(resultsFiles => {
								// 	console.log('resultsFiles-----', resultsFiles)
								// 	vm.listBlobSrc = resultsFiles
								// }).catch(rejectErr => {
								// })
								var url = "/o/rest/v2/dossiers/" + vm.detailModel.dossierId + "/files/" + vm.dossierFilesView[0].referenceUid ;
								console.log("Preview dossier PDF");
								vm.getBlobFile(vm.dossierFilesView[0]).then(resolve => {
									console.log('resolve-----', resolve)
									vm.blobFileSrc = resolve
								})
								vm.dialogViewFile = !vm.dialogViewFile
							}
						},
						nextFile: function () {
							var vm = this
							if (vm.indexFile < vm.dossierFilesView.length -1) {
								vm.indexFile ++;
							}
							vm.blobFileSrc = ''
							vm.getBlobFile(vm.dossierFilesView[vm.indexFile]).then(resolve => {
								console.log('resolve-----', resolve)
								vm.blobFileSrc = resolve
							})
						},
						prevFile: function () {
							var vm = this
							if (vm.indexFile > 0) {
								vm.indexFile --;
							}
							vm.blobFileSrc = ''
							vm.getBlobFile(vm.dossierFilesView[vm.indexFile]).then(resolve => {
								console.log('resolve-----', resolve)
								vm.blobFileSrc = resolve
							})
						},
						getBlobFile: function (item) {
							var vm = this;
							return new Promise((resole, reject) => {
								const config_blob = {
									headers: {
										'groupId': themeDisplay.getScopeGroupId(),
										'Content-Type': 'application/json'
									},
									responseType: 'blob'
								};

								var url = '/o/rest/v2/dossiers/' + vm.detailModel.dossierId + '/files/' + item.referenceUid;

								axios.get(url, config_blob).then(function (response) {
									var url = window.URL.createObjectURL(response.data);
									if (response.status === 204) {
										resole(false);
									} else {
										resole(url);
									}
								})
								.catch(function (error) {
									console.log(error);
									reject(false)
								});
							})
						},
						printDeliverable: function() {						    
							$('#printTraCuu').printThis();
							console.log("Print deliverable");
						},
						searchAdvanceDossier() {
							var vm = this
							if (vm.stageFilterView === 'tat_ca_hoso' && !vm.detailPage && !vm.detailRegistPage ) {
								vm._inidanhSachHoSoTable(false);
							} else if (vm.stageFilterView === 'tra_cuu_phuong_tien' && !vm.detailPage && !vm.detailRegistPage) {
							} else if (vm.stageFilterView === 'tra_cuu_thong_tin_doanh_nghiep' && !vm.detailPage && !vm.detailRegistPage) {
								vm._inithongTinDoanhNghiepTable(false);
								vm.detailRegistPage = false;
							} else {
								vm._inidanhSachHoSoTable(false);
							}
							vm.dialogSearch = false;
						},
						searchKeyWord: function () {
							var vm = this
							vm.applicantIdNo = '';
							vm.dossierIdCTN = '';
							vm.dossierStatus = '';
							vm.startDate = '';
							vm.endDate = '';
							vm.dossierNo = '';
							vm.serviceInfo = '';
							vm._inidanhSachHoSoTable(false);
						},
						savePrintTemplate: function() {
							var formTemplate = {};
							
							$("#printTraCuu > div").each(function(){
								var id = $(this).attr('id');
								console.log("Div id: " + id);
								var xPos = $(this).position().left;
								var yPos = $(this).position().top;
								formTemplate[id] = {
									offsetX: xPos,
									offsetY: yPos										
								};
							});
							var vm = this;
							var url ="/o/il/v2/inland";
							
							vm.currentPrintTemplate.formTemplate = JSON.stringify(formTemplate);
							vm.currentPrintTemplate.employeeId = 0;
							
							console.log(formTemplate);
					        if (vm.currentPrintTemplate.createUserId == 0) {
					        	axios.post(url, $.param(vm.currentPrintTemplate), {
									headers: {
										'Content-Type': 'application/x-www-form-urlencoded',
										groupId: themeDisplay.getScopeGroupId()
									}												        		
					        	})
					        	.then(function (response) {
									vm.popUpPrintTraCuu = !vm.popUpPrintTraCuu;
					        	    console.log(response);
					        	})
					        	.catch(function (error) {
					        	    console.log(error);
					        	});	
					        }
					        else {
					        	axios.put(url + "/" + vm.currentPrintTemplate.printTemplateId, $.param(vm.currentPrintTemplate), {
									headers: {
										'Content-Type': 'application/x-www-form-urlencoded',
										groupId: themeDisplay.getScopeGroupId()
									}												        		
					        	})
					        	.then(function (response) {
					        	    console.log(response);
					        	})
					        	.catch(function (error) {
					        	    console.log(error);
					        	});	
					        }							
						},
						getInLand: function(item){
							var vm = this;
							console.log(item);
							var url ="/o/il/v2/inland";
							var configInland = {
								params: {
									serviceCode: item.serviceCode,
									dossierPartNo: item.dossierPartNo,
									fileTemplateNo: item.fileTemplateNo,
									templateNo: item.dossierTemplateNo,
									licenceType: item.licenceType
								},
								headers: {
									groupId: themeDisplay.getScopeGroupId()
								}
							}

							axios.get(url, configInland).then(function (response) {
								if (response.data.hasOwnProperty('formTemplate')) {
									if (response.data.formTemplate != '' && response.data.formTemplate.length != 0) {
							        	vm.currentPrintTemplate = {
											printTemplateId: response.data.printTemplateId,
							        		serviceCode: response.data.serviceCode,
								        	dossierPartNo: response.data.dossierPartNo,
								        	fileTemplateNo: response.data.fileTemplateNo,
								        	templateNo: response.data.templateNo,
								        	createUserId: response.data.createUserId,
								        	employeeId: response.data.employeeId,
								        	formTemplate: response.data.formTemplate,
								        	defaultCss: response.data.defaultCss,
								        	originalDocumentURL: response.data.originalDocumentURL,
								        	licenceType: response.data.licenceType
							        	};
							        	$('#printTraCuu').empty();
							        				        	
							        	var originalDocumentArr = JSON.parse(response.data.originalDocumentURL);
							        	var originalDocumentObj = originalDocumentArr[0];
							        	
							        	for (var i = 0; i < originalDocumentArr.length; i++) {
							        		var originalDocumentTemp = originalDocumentArr[i];
							        		if (originalDocumentTemp.hasOwnProperty('licenceType') && originalDocumentTemp['licenceType'] == item.licenceType) {
							        			originalDocumentObj = originalDocumentTemp;
							        			break;
							        		}
							        	}
							        	
	//						        	$('#printTraCuu').css("background-image",'url(' + originalDocumentObj.url + ')');
										$('#imgTraCuu').attr("src", originalDocumentObj.url);
										$('#imgTraCuu').css(originalDocumentObj.css);
										
										var imgTraCuu = $('#imgTraCuu');
										imgTraCuu.load(function(){
								        	var style = 'background-image: url(' + originalDocumentObj.url + ');background-size:' + imgTraCuu.width() + 'px ' + imgTraCuu.height() + 'px;';
								        	style += response.data.defaultCss;
								        	$('#printTraCuu').attr("style", style);
											$('#printTraCuu').height(imgTraCuu.height());
											$('#printTraCuu').width(imgTraCuu.width());
										});
										
										var formTemplate = response.data.formTemplate;
	
										var formData = JSON.parse(formTemplate);
																			
										for (var key in formData) {
											if (!document.getElementById(key)) {
												if (key.indexOf("array_") != -1) {
													var newKeyArr = key.split("_");
													var newKey = newKeyArr[2];
													if (item.hasOwnProperty(newKey)) {
														var itemDataArr = item[newKey].split("/");
														if (itemDataArr.length > newKeyArr[1]) {
															$("#printTraCuu").append('<div id='+key+' style="z-index: 99; font-size: 14px; position:absolute; left : '+formData[key].offsetX+'px; top : '+formData[key].offsetY+'px">' + itemDataArr[newKeyArr[1]] + '</div>');																																																				
														}
													}													
												}
												else if (key.indexOf("_") != -1) {
													console.log("Key contain _: " + key);
													var newKeyArr = key.split("_");
													var newKey = newKeyArr[1];
													if (item.hasOwnProperty(newKey)) {
														$("#printTraCuu").append('<div id='+key+' style="z-index: 99; font-size: 14px; position:absolute; left : '+formData[key].offsetX+'px; top : '+formData[key].offsetY+'px">' + item[newKey] + '</div>');																																					
													}
												}
												else {
													if (item.hasOwnProperty(key)) {
														$("#printTraCuu").append('<div id='+key+' style="z-index: 99; font-size: 14px; position:absolute; left : '+formData[key].offsetX+'px; top : '+formData[key].offsetY+'px">' + item[key] + '</div>');																																			
													}
												}
												$('#' + key).draggable({
													stop: function() {
														var position = $(this).position();
												        var xPos = position.left;
												        var yPos = position.top;
												        formData[this.id].offsetX = xPos;
												        formData[this.id].offsetY = yPos;
												        
												        var data = {
												        	serviceCode: response.data.serviceCode,
												        	dossierPartNo: response.data.dossierPartNo,
												        	fileTemplateNo: response.data.fileTemplateNo,
												        	templateNo: response.data.templateNo,
												        	createUserId: themeDisplay.getUserId(),
												        	employeeId: 0,
												        	formTemplate: JSON.stringify(formData),
												        	defaultCss: response.data.defaultCss,
												        	originalDocumentURL: response.data.originalDocumentURL,
												        	licenceType: response.data.licenceType
												        };
												        
												        if (response.data.createUserId != 0) {
												        	data.printTemplateId = response.data.printTemplateId;
												        	axios.put(url + "/" + response.data.printTemplateId, $.param(data), {
																headers: {
																	'Content-Type': 'application/x-www-form-urlencoded',
																	groupId: themeDisplay.getScopeGroupId()
																}												        		
												        	})
												        	.then(function (response) {
												        	    console.log(response);
												        	})
												        	.catch(function (error) {
												        	    console.log(error);
												        	});	
												        }
												        else {
												        	
												        }
												    }
												});
											}
										}
										
							        	/*
										$('#imgTraCuu').attr("src", response.data.originalDocumentURL);
										var formTemplate = response.data.formTemplate;
										var imgTraCuu = $('#imgTraCuu');
										imgTraCuu.load(function(){
											$('#printTraCuu').height(imgTraCuu.height());
											$('#printTraCuu').width(imgTraCuu.width());
											
											var formData = JSON.parse(formTemplate);
											
											for (var key in formData) {
												if (item.hasOwnProperty(key) && !document.getElementById(key)) {
													$("#printTraCuu").append('<div id='+key+' style="z-index: 99; font-size: 14px; position:absolute; left : '+formData[key].offsetX+'px; top : '+formData[key].offsetY+'px">' + item[key] + '</div>');												
													$('#' + key).draggable({
														stop: function() {
															var position = $(this).position();
													        var xPos = position.left;
													        var yPos = position.top;
													        formData[key].offsetX = xPos;
													        formData[key].offsetY = yPos;
													        
													        var data = {
													        	serviceCode: response.data.serviceCode,
													        	dossierPartNo: response.data.dossierPartNo,
													        	fileTemplateNo: response.data.fileTemplateNo,
													        	templateNo: response.data.templateNo,
													        	createUserId: themeDisplay.getUserId(),
													        	employeeId: 0,
													        	formTemplate: JSON.stringify(formData),
													        	originalDocumentURL: response.data.originalDocumentURL
													        };
													        if (response.data.createUserId != 0) {
													        	data.printTemplateId = response.data.printTemplateId;
													        	axios.put(url + "/" + response.data.printTemplateId, $.param(data), {
																	headers: {
																		'Content-Type': 'application/x-www-form-urlencoded',
																		groupId: themeDisplay.getScopeGroupId()
																	}												        		
													        	})
													        	.then(function (response) {
													        	    console.log(response);
													        	})
													        	.catch(function (error) {
													        	    console.log(error);
													        	});	
													        }
													    }
													});
												}
											}									
										});
										*/
							        	
									}
								}
								else {
									vm.popUpPrintTraCuu  = !vm.popUpPrintTraCuu;
									vm.toDetailGiayPhep(item);
								}
							})
							.catch(function (error) {
								console.log(error);
							});
						},
						toDetailGiayPhep: function(item){
							var vm = this;
							var url ="/o/rest/v2/dossiers/"+item.dossierId+"/files/"+item.referenceUid ;

							var config_blob =  {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								},
								responseType: 'blob'
							};

							axios.get(url, config_blob).then(function (response) {
								var urlblob = window.URL.createObjectURL(response.data);
								window.open(urlblob, '_blank')
							})
							.catch(function (error) {
								console.log(error);
							});
						},
						printGiayPhep: function(item) {
							var vm = this;
							vm.popUpPrintTraCuu  = !vm.popUpPrintTraCuu;
							
							setTimeout(function(){
								vm.getInLand(item)
							}, 500)
						},
						toDetailThongTinXe: function(item){
							var vm = this;
							vm.hinhThucSelect = '';
							vm.thongTinXeDate = '';
							vm.cuaKhauSelect = '';
							vm.registrationDate = '';
							vm.thong_tin_lai_xe = '';
							vm.giay_phep_lai_xe = '';
							vm._initCuaKhau();
							var paramsBuilder = {
					                start: vm.pageHistory * 10 - 10,
					                end: vm.pageHistory * 10
					        };
					        var config_dossiers = {
					                params: paramsBuilder,
					                headers: {
					                  'groupId': themeDisplay.getScopeGroupId(),
					                }
					        };							
							var urlThongTinXe = '/o/rest/vr-app/certDoc/borderGuard/'+item.registrationNumber;
							var urlVehicleDetail = '/o/rest/vr-app/ilvehicle/'+item.registrationNumber;
							axios.get(urlVehicleDetail, config_dossiers).then(function (response) {
								var serializable = response.data;
								vm.modelVehicle = serializable;
								vm.vehicleItems = [];
								for (var k in vm.modelVehicle) {
									if (vm.modelVehicle.hasOwnProperty(k)) {
										var name = '';
										if (k == 'registrationNumber') {
											name = 'Biển số';
										}
										else if (k == 'engineNumber') {
											name = 'Số khung';
										}
										else if (k == 'chassisNumber') {
											name = 'Số máy';
										}
										else if (k == 'manufaturedYear') {
											name = 'Năm sản xuất';
										}
										else if (k == 'capacity') {
											name = 'Số ghế';
										}
										else if (k == 'trademarkName') {
											name = 'Nhãn hiệu';
										}
										else if (k == 'vehicleColor') {
											name = 'Màu sơn';
										}
										if (name != '') {
											vm.vehicleItems.push({
												name: name,
												value: vm.modelVehicle[k]
											});																						
										}
									}
								}
								axios.get(urlThongTinXe, config_dossiers).then(function (response) {
									var serializable = response.data;
									if (serializable.data) {
										var page = Math.ceil(serializable.total / 10);
										vm.lengthPageHistory = page;
									} else {
										vm.lengthPageHistory = 1;
									}
									vm.modelLienVan = serializable;
									vm.popUpThongTinXe  = !vm.popUpThongTinXe;
								})
								.catch(function (error) {
									console.log(error);
								});
							})
							.catch(function (error) {
								console.log(error);
							});
						},
						addThongTinXe: function(append){
							var vm = this;
							console.log('addThongTinXe')
							var urlHistorys = '/o/rest/vr-app/certDoc/borderGuard/'+append.registrationNumber;
							var formData = new URLSearchParams();
							var registrationDate = vm.parseDate(vm.thongTinXeDate)
							formData.append('expImpGateType', vm.hinhThucSelect);
							formData.append('expImpGate', vm.cuaKhauSelect.itemName);
							formData.append('expImpGateCode', vm.cuaKhauSelect.itemCode);
							formData.append('registrationDate', registrationDate);
							formData.append('driverName', vm.thong_tin_lai_xe);
							formData.append('driverLicenceNo', vm.giay_phep_lai_xe);
							axios.post(urlHistorys, formData, config).then(function (response) {
								vm.snackbartextdossierViewJX = "Yêu cầu thực hiện thành công";
								vm.snackbardossierViewJX = true;
								var urlThongTinXe = '/o/rest/vr-app/certDoc/borderGuard/'+append.registrationNumber;
								axios.get(urlThongTinXe, config).then(function (response) {
									var serializable = response.data;
									vm.modelLienVan = serializable;
									vm.hinhThucSelect = '';
									vm.thongTinXeDate = '';
									vm.cuaKhauSelect = '';
									vm.registrationDate = '';
									vm.thong_tin_lai_xe = '';
									vm.giay_phep_lai_xe = '';
								})
								.catch(function (error) {
									console.log(error);
								});
							})
							.catch(function (error) {
								console.log(error);
							});
						},
						_inigiayPhepVanTaiQuocTeTable: function (append) {
							var vm = this;
							vm.traCuuFilter = false;
							vm.viewmore = true;
							var serviceCodeTemp = '';
							if(!vm.serviceInfoSelect) {
								serviceCodeTemp = configPage.serviceinfosQuocTe
							}
							else {
								serviceCodeTemp = vm.serviceInfoSelect;
							}
							var paramsBuilder = {
								keywords: vm.keywordTraCuuGiayPhep,
								serviceCode: serviceCodeTemp,
								govAgencyCode: vm.govAgencySelect,
								routeCode: vm.tuyenSelect,
								startDate: vm.parseDate(vm.searchTuNgay),
								fromDate: vm.parseDate(vm.searchDenNgay),
								start: vm.pageGiayPhepVanTaiQuocTeTable * 15 - 15,
								limit: 15
							};

							const config_dossiers = {
								params: paramsBuilder,
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								}
							};

							var url = '/o/rest/vr-app/certDoc';

							axios.get(url, config_dossiers).then(function (response) {
								var serializable = response.data;


								if (append) {
									vm.giayPhepVanTaiQuocTeTableItems.push.apply(vm.giayPhepVanTaiQuocTeTableItems, serializable.data);
								} else if(serializable.data){

									vm.giayPhepVanTaiQuocTeTableItems = serializable.data;

								}else {
									vm.giayPhepVanTaiQuocTeTableItems = [];
								}
								var page = Math.ceil(serializable.total / 15);
								vm.pageGiayPhepVanTaiQuocTeTableLength = page;
								console.log(vm.giayPhepVanTaiQuocTeTableItems);
								$("#tableGiayPhepVanTaiQuocTe").find('table > thead > tr > th:nth-child(2)').html("Tên giấy phép <br> Gíây phép - Số giấy phép")
							})
							.catch(function (error) {
								console.log(error);
								vm.giayPhepVanTaiQuocTeTableItems = [
								{
									giay_phep: 'abc123',
									so_giay_phep: 'abc123',
									ten_doanh_nghiep: 'abc123',
									co_quan: 'abc123',
									hieu_luc_tu_ngay: 'abc123',
									hieu_luc_den_ngay: 'abc123'
								},
								{
									giay_phep: 'abc123',
									so_giay_phep: 'abc123',
									ten_doanh_nghiep: 'abc123',
									co_quan: 'abc123',
									hieu_luc_tu_ngay: 'abc123',
									hieu_luc_den_ngay: 'abc123'
								},
								{
									giay_phep: 'abc123',
									so_giay_phep: 'abc123',
									ten_doanh_nghiep: 'abc123',
									co_quan: 'abc123',
									hieu_luc_tu_ngay: 'abc123',
									hieu_luc_den_ngay: 'abc123'
								}
								];
								var page = Math.ceil(serializable.total / 15);
								vm.pageGiayPhepVanTaiQuocTeTableLength = page;

							});
							return false; 
						},
						_inigiayPhepLienVanTable: function (append) {
							var vm = this;
							vm.traCuuFilter = false;
							vm.viewmore = true;
							var serviceCodeTemp = '';
							if(!vm.serviceInfoSelect) {
								serviceCodeTemp = configPage.serviceinfosLienVan
							}
							else {
								serviceCodeTemp = vm.serviceInfoSelect;
							}
							var paramsBuilder = {
								keywords: vm.keywordTraCuuGiayPhep,
								serviceCode: serviceCodeTemp,
								govAgencyCode: vm.govAgencySelect,
								routeCode: vm.tuyenSelect,
								startDate: vm.parseDate(vm.searchTuNgay),
								fromDate: vm.parseDate(vm.searchDenNgay),
								start: vm.pageGiayPhepLienVanTable * 15 - 15,
								limit: 15
							};

							const config_dossiers = {
								params: paramsBuilder,
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								}
							};

							var url = '/o/rest/vr-app/certDoc';

							axios.get(url, config_dossiers).then(function (response) {
								var serializable = response.data;
								if (append) {
									vm.giayPhepLienVanTableItems.push.apply(vm.giayPhepLienVanTableItems, serializable.data);
								} else if(serializable.data){
									vm.giayPhepLienVanTableItems = serializable.data;
								}else {
									vm.giayPhepLienVanTableItems = [];
								}
								var page = Math.ceil(serializable.total / 15);
								vm.pageGiayPhepLienVanTableLength = page;
								$("#tableGiayPhepLienVan").find('table > thead > tr > th:nth-child(2)').html("Tên giấy phép <br> Gíây phép - Số giấy phép")
								$("#tableGiayPhepLienVan").find('table > thead > tr > th:nth-child(3)').html("Số đăng ký phương tiện <br> Đơn vị khai thác")
								console.log(vm.giayPhepLienVanTableItems);
							})
							.catch(function (error) {
								console.log(error);
								vm.giayPhepLienVanTableItems = [
								{
									giay_phep: 'asdfgh',
									so_giay_phep: 'asdfgh',
									so_dang_ky_phuong_tien: 'asdfgh',
									ten_doanh_nghiep: 'asdfgh',
									co_quan: 'asdfgh',
									hieu_luc_tu_ngay: '1/1/2017',
									hieu_luc_den_ngay: '1/1/2018'
								},
								{
									giay_phep: 'asdfgh',
									so_giay_phep: 'asdfgh',
									so_dang_ky_phuong_tien: 'asdfgh',
									ten_doanh_nghiep: 'asdfgh',
									co_quan: 'asdfgh',
									hieu_luc_tu_ngay: '1/1/2017',
									hieu_luc_den_ngay: '1/1/2018'
								},
								{
									giay_phep: 'asdfgh',
									so_giay_phep: 'asdfgh',
									so_dang_ky_phuong_tien: 'asdfgh',
									ten_doanh_nghiep: 'asdfgh',
									co_quan: 'asdfgh',
									hieu_luc_tu_ngay: '1/1/2017',
									hieu_luc_den_ngay: '1/1/2018'
								},
								{
									giay_phep: 'asdfgh',
									so_giay_phep: 'asdfgh',
									so_dang_ky_phuong_tien: 'asdfgh',
									ten_doanh_nghiep: 'asdfgh',
									co_quan: 'asdfgh',
									hieu_luc_tu_ngay: '1/1/2017',
									hieu_luc_den_ngay: '1/1/2018'
								}
								];

							});
							return false; 
						},
						_inichapThuanKhaiThacTable: function (append) {
							var vm = this;
							vm.traCuuFilter = false;
							vm.viewmore = true;
							var serviceCodeTemp = '';
							if(!vm.serviceInfoSelect) {
								serviceCodeTemp = configPage.serviceinfosChapThuan
							}
							else {
								serviceCodeTemp = vm.serviceInfoSelect;
							}
							var paramsBuilder = {
								keywords: vm.keywordTraCuuGiayPhep,
								serviceCode: serviceCodeTemp,
								govAgencyCode: vm.govAgencySelect,
								routeCode: vm.tuyenSelect,
								startDate: vm.parseDate(vm.searchTuNgay),
								fromDate: vm.parseDate(vm.searchDenNgay),
								start: vm.pageChapThuanKhaiThacTable * 15 - 15,
								limit: 15
							};

							const config_dossiers = {
								params: paramsBuilder,
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								}
							};

							var url = '/o/rest/vr-app/certDoc';

							axios.get(url, config_dossiers).then(function (response) {
								var serializable = response.data;
								if (append) {
									vm.chapThuanKhaiThacTableItems.push.apply(vm.chapThuanKhaiThacTableItems, serializable.data);
								} else if(serializable.data){

									vm.chapThuanKhaiThacTableItems = serializable.data;

								}else {
									vm.chapThuanKhaiThacTableItems = [];
								}
								var page = Math.ceil(serializable.total / 15);
								vm.pageChapThuanKhaiThacTableLength = page;
								console.log(vm.chapThuanKhaiThacTableItems);
								$("#tableChapThuanKhaiThac").find('table > thead > tr > th:nth-child(2)').html("Tên giấy phép <br> Gíây phép - Số giấy phép")
								$("#tableChapThuanKhaiThac").find('table > thead > tr > th:nth-child(6)').html("Số xe tham gia <br> Hiệu lực khai thác")
							})
							.catch(function (error) {
								console.log(error);
								vm.chapThuanKhaiThacTableItems = [
									{
										giay_phep: 'ccccc',
										so_giay_phep: 'ccccc',
										ten_doanh_nghiep: 'ccccc',
										tuyen_khai_thac: 'ccccc',
										so_xe: 'ccccc',
										hieu_luc_tu_ngay: 'ccccc',
										hieu_luc_tu_ngay: '1/1/2017',
										hieu_luc_den_ngay: '11/1/2018'
									},
									{
										giay_phep: 'ccccc',
										so_giay_phep: 'ccccc',
										ten_doanh_nghiep: 'ccccc',
										tuyen_khai_thac: 'ccccc',
										so_xe: 'ccccc',
										hieu_luc_tu_ngay: 'ccccc',
										hieu_luc_tu_ngay: '1/1/2017',
										hieu_luc_den_ngay: '11/1/2018'
									},
									{
										giay_phep: 'ccccc',
										so_giay_phep: 'ccccc',
										ten_doanh_nghiep: 'ccccc',
										tuyen_khai_thac: 'ccccc',
										so_xe: 'ccccc',
										hieu_luc_tu_ngay: 'ccccc',
										hieu_luc_tu_ngay: '1/1/2017',
										hieu_luc_den_ngay: '11/1/2018'
									},
									{
										giay_phep: 'ccccc',
										so_giay_phep: 'ccccc',
										ten_doanh_nghiep: 'ccccc',
										tuyen_khai_thac: 'ccccc',
										so_xe: 'ccccc',
										hieu_luc_tu_ngay: 'ccccc',
										hieu_luc_tu_ngay: '1/1/2017',
										hieu_luc_den_ngay: '11/1/2018'
									}
								];

							});
							return false; 
						},
						_initServiceInfos: function(param){
							var vm = this;
							var url = '/o/rest/v2/serviceinfos';
							var config_serviceinfo = {
								headers: {
									groupId: themeDisplay.getScopeGroupId()
								},
								params: {
									serviceCode: param.serviceCode
								}
							}
							axios.get(url, config_serviceinfo).then(function (response) {
								var serializable = response.data;
								vm.serviceInfos = serializable.data;
								
							})
							.catch(function (error) {
								console.log(error);

							});
						},
						_initGovAgencys: function(param){
							var vm = this;
							var url = '/o/rest/v2/temp/dictcollections/GOVERNMENT_AGENCY/dictitems?sort=sibling';
							var config_gov = {
								headers: {
									groupId: themeDisplay.getScopeGroupId()
								},
								params: {
									govAgencyCode: param.govAgencyCode
								}
							}
							axios.get(url, config_gov).then(function (response) {
								var serializable = response.data;
								vm.govAgencys = serializable.data;
								
							})
							.catch(function (error) {
								console.log(error);

							});
						},
						_initTuyens: function(param){
							var vm = this;
							var url = '/o/rest/v2/temp/dictcollections/DB01/dictitems?sort=sibling';
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								vm.tuyens = serializable.data;
								
							})
							.catch(function (error) {
								console.log(error);

							});
						},
						_initCuaKhau: function (param) {
							var vm = this;
							var url = '/o/rest/v2/temp/dictcollections/DB05/dictitems?sort=sibling';
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								vm.cuaKhaus = serializable.data;
							})
							.catch(function (error) {
								console.log(error);

							});
						},
						changeStateTraCuuGiayPhep: function(state){
							var vm = this;
							vm.stateTraCuuGiayPhep = state;
							vm.serviceInfoSelect = '';
							$("#listTraCuuGiayPhep > li").removeClass("active");
							if(state == 'giay_phep_van_tai_quoc_te'){
								vm._inigiayPhepVanTaiQuocTeTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosQuocTe
								});
								$("#listTraCuuGiayPhep > li:nth-child(1)").addClass("active");
							}else if(state == 'giay_phep_lien_van'){
								vm._inigiayPhepLienVanTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosLienVan
								});
								$("#listTraCuuGiayPhep > li:nth-child(2)").addClass("active");
							}else {
								vm._inichapThuanKhaiThacTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosChapThuan
								});
								$("#listTraCuuGiayPhep > li:nth-child(3)").addClass("active");
							}
							console.log('vm.stateTraCuuGiayPhep=========',vm.stateTraCuuGiayPhep);
						},
						searchTraCuuGiayPhep: function(){
							var vm = this;
							var state = vm.stateTraCuuGiayPhep;
							vm.stateTraCuuGiayPhep = state;
							if(state == 'giay_phep_van_tai_quoc_te'){
								vm._inigiayPhepVanTaiQuocTeTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosQuocTe
								});
							}else if(state == 'giay_phep_lien_van'){
								vm._inigiayPhepLienVanTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosLienVan
								});
							}else {
								vm._inichapThuanKhaiThacTable();
								vm._initServiceInfos({
									serviceCode: configPage.serviceinfosChapThuan
								});
							}
						},
						getLastedStateUserInfo : function(callBack){
							var vm = this;

							var promise = new Promise(function(resolve, reject){
								var url = '/o/rest/v2/userInfologs';

								axios.get(url, config).then(function (response) {
									var serializable = response.data;

									console.log("serializable========",serializable);

									var serviceCode = serializable.serviceInfo;
									var applicantIdNo = serializable.applicant;
									var dossierNo = serializable.dossierNo;

									console.log("serviceCode========",serviceCode);
									console.log("applicantIdNo========",applicantIdNo);
									console.log("dossierNo========",dossierNo);
									if(serviceCode){
										vm.serviceInfoFilter = {
											serviceCode : serviceCode,
											serviceName : ""
										}
									}

									if(applicantIdNo){
										vm.applicantNameFilter = {
											applicantIdNo : applicantIdNo,
											applicantName : ""
										}
									}

									if(dossierNo){
										vm.dossierNoFilter = dossierNo
									}
									resolve(true);

								})
								.catch(function (error) {
									console.log(error);
									reject(false);
								});
							});


							promise.then(function(success){
								callBack();
							}, function(error){

							});

							
						},
						putStateUserInfo : function(){
							var vm = this;

							var stateModel = {
								"serviceInfo" : vm.serviceInfoFilter.serviceCode,
								"applicant" : vm.applicantNameFilter.applicantIdNo,
								"dossierNo" : vm.dossierNoFilter
							}

							$.ajax({
								url : "/o/rest/v2/userInfologs",
								dataType : "json",
								type : "POST",
								headers : {
									"groupId" : themeDisplay.getScopeGroupId()
								},
								data : stateModel,
								success :  function(result){

								},
								error : function(result){

								}
							});


						},
						deleteDossierPDF: function(item, index) {
							var vm = this;

							vm.$dialog.confirm('Bạn có muốn xóa tài liệu này!', {
								html: true,
								loader: true,
								okText: 'Xác nhận',
								cancelText: 'Quay lại',
								animation: 'fade'
							})
							.then((dialog) => {
								$.ajax({
									url : "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid,
									dataType : "json",
									type : "DELETE",
									headers: {
										"groupId": themeDisplay.getScopeGroupId()
									},
									success : function(result){
										vm.snackbartextdossierViewJX = "Xoá dữ liệu thành phần hồ sơ thành công!";
										vm.snackbardossierViewJX = true;
										vm.listDocumentOutStepItems.splice(index, 1);
										vm.stepModel.createFiles[item.index].counter = vm.stepModel.createFiles[item.index].counter - 1;
									},
									error : function(result){
										vm.snackbartextdossierViewJX = "Xoá dữ liệu thành phần hồ sơ thất bại!";
										vm.snackbarerordossierViewJX = true;
									}
								});
								dialog.close();
							})
							.catch((e) => {
								console.log(e)
							})
						},
						submitAlpacajsForm: function (item) {
							var vm = this;
							vm.loadingAlpacajsForm = true;
							try{
								console.log("$('#alpacajs_form_'+item.partNo + ' .formType').val()============",$("#alpacajs_form_"+item.partNo + " .formType").val());
								if( $("#alpacajs_form_"+item.partNo + " .formType").val() == null || 
									$("#alpacajs_form_"+item.partNo + " .formType").val() == 'undefined' ) {
									console.log("formType 1");
								vm.loadingAlpacajsForm = true;

								var control = $("#alpacajs_form_"+item.partNo).alpaca("get");
								var formData = control.getValue();
								console.log("Form data: " + JSON.stringify(formData));

								$.ajax({
									url : "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid+"/formdata",
									dataType : "json",
									type : "PUT",
									headers: {
										"groupId": themeDisplay.getScopeGroupId(),
										"Accept" : "application/json"
									},
									data : {
										formdata: JSON.stringify(formData)
									},
									success : function(result){
										vm.snackbartextdossierViewJX = "Lưu form thành công!";
										vm.snackbardossierViewJX = true;
										vm.loadingAlpacajsForm = false;

										try{
											
											if(item.hasSubmit){

											}else {
												if (item.counter == 0) {
													item.counter ++;
													item.hasSubmit = true;													
												}
											}


										}catch(e){

										}
									},
									error : function(result){
										vm.snackbartextdossierViewJX = "Lưu form thất bại!";
										vm.snackbarerordossierViewJX = true;
										vm.loadingAlpacajsForm = false;
									}
								});

							} else if( $("#alpacajs_form_"+item.partNo + " .formType").val() != null && 
								$("#alpacajs_form_"+item.partNo + " .formType").val() === 'assign' ) {
								console.log("formType 2");
								vm.loadingAlpacajsForm = true;

								var control = $("#alpacajs_form_"+item.partNo).alpaca("get");
								var formData = control.getValue();


								vm.subUsers = formData['subUsers'];
								vm.alpacaAssignUserId = formData['userAction'];

								$.ajax({
									url : "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid+"/formdata",
									dataType : "json",
									type : "PUT",
									headers: {
										"groupId": themeDisplay.getScopeGroupId(),
										"Accept" : "application/json"
									},
									data : {
										formdata: JSON.stringify(formData)
									},
									success : function(result){
										vm.snackbartextdossierViewJX = "Lưu form thành công!";
										vm.snackbardossierViewJX = true;
										vm.loadingAlpacajsForm = false;

										try{
											if(item.hasSubmit){

											}else {

												item.counter ++;
												item.hasSubmit = true;
											}


										}catch(e){

										}
									},
									error : function(result){
										vm.snackbartextdossierViewJX = "Lưu form thất bại!";
										vm.snackbarerordossierViewJX = true;
										vm.loadingAlpacajsForm = false;
									}
								});


							} else {
								vm.loadingAlpacajsForm = true;
								setTimeout(
									function(){ 
										try{
											vm.loadingAlpacajsForm = false;
											if(item.hasSubmit){

											}else {

												item.counter ++;
												item.hasSubmit = true;
											}


										}catch(e){

										}
										
									}, 
									3000);
							}
						}catch(e){
							vm.loadingAlpacajsForm = false;
						}
					},
					showAlpacaJSFORM: function (item) {
							//alapcajs Form
							var alpacajsForm = document.getElementById("alpacajs_form_"+item.partNo);
							if (alpacajsForm.innerHTML == '' && item.eform) {
								console.log(item);
								var alapcaJS = eval('('+item.formScript+')');
								alapcaJS['data'] = item.formData;
								
								$("#alpacajs_form_"+item.partNo).alpaca(alapcaJS);
							}
							
						},
						viewOnNewTab : function(item){
							var vm = this;
							if(item.counter > 0){
								console.log("vm.detailModel========",vm.detailModel);

								var url = "/group/cong-xu-ly/xu-ly-ho-so?stateWindow=stateWindow&dossierId="+vm.detailModel.dossierId+
								"&dossierPartNo="+item.partNo;

								console.log("url============",url);

								window.open(url, "_blank");

							}

						},
						parseDateUtc : function(date){
							return moment(String(date)).utc().format('DD/MM/YYYY HH:mm:ss');
						},
						showAlpacaJSFORMRegist: function (item) {
							var vm = this;
							//alapcajs Form
							var url = '/o/rest/v2/registrations/'+vm.detailRegistModel.registrationId+'/forms/' + item.referenceUid +"/formscript";
                            // var url = '/o/frontendwebdossier/json/steps.json';
                            vm.alapcaJSRei = {};
                            axios.get(url, config).then(function (response) {
                            	var serializable = response.data;

                            	vm.alapcaJSRei = eval('('+serializable+')');

                            	axios.get('/o/rest/v2/registrations/'+vm.detailRegistModel.registrationId+'/forms/' + item.referenceUid +"/formdata", 
                            		config).then(function (responseFormData) {

                            			vm.alapcaJSRei['data'] = responseFormData.data;
                            			$("#regist_form_"+item.referenceUid).alpaca(vm.alapcaJSRei);

                            		})
                            		.catch(function (error) {
                            			console.log(error);

                            		});

                            	})
                            .catch(function (error) {
                            	console.log(error);
                            	vm.stepLoading = false;

                            });
                            return false; 

                        },
                        checkHasEform (items) {
                        	var vm = this
                        	for (var i = 0; i < items.length; i++) {
                        		if (items[i]['eform']) {
                        			return true;
                        		}
                        	}
                        	return false;
                        },
                        changeProcessStep: function (item){
                        	var vm = this;
                        	console.log("Change process step");
                        	vm.dialogThuLyHoSo = true;
                        	console.log(item);
                        	var status = vm.statusParamFilter;
                        	var subStatus = vm.substatusParamFilter;
                        	vm.stepModel = null;
                        	$("#alpacajs_form_plugin").empty();
                        	
                        	/*if(status === "waiting" && subStatus === "watting_1"){
                        		$("#labelTPKQ").html("File đính kèm");
                        	}*/

                        	if(item.type === 1){
                        		vm.maxWidthDialog = '600px';
                        		var needIntervalRefresh = false;
                        		var fileArr = item.createFiles;
    							if (fileArr && fileArr.length) {
    								var length = fileArr.length;                        		
	                        		for (var i = 0; i < length; i++) {
	                        			var fileItem = fileArr[i];
	                        			if (fileItem.counter == 0 && (!fileItem.eform) && (!fileItem.returned) && (fileItem.dossierFileId != 0)) {
	                        				needIntervalRefresh = true;
	                        				break;
	                        			}
	                        		}
    							}
    							else if (fileArr) {
                        			var fileItem = fileArr;
                        			if (fileItem.counter == 0 && (!fileItem.eform) && (!fileItem.returned) && (fileItem.dossierFileId != 0)) {
                        				needIntervalRefresh = true;
                        			}    								
    							}
                        		$("textarea#processActionNote").val("");

                        		if (item.hasOwnProperty('createFiles') && !(item.createFiles instanceof Array)) {
                        			var createFilesTemp = item.createFiles;
                        			item.createFiles = [];
                        			item.createFiles.push(createFilesTemp);
                        		}

                        		if (item.autoEvent !== 'submit' && item.autoEvent !== 'timer') {
                        			vm.stepModel = item;
                        			setTimeout(function () {
                        				if (vm.stepModel.hasOwnProperty('createFiles')) {
                        					for (var i = 0; i < vm.stepModel.createFiles.length; i++) {
                        						if (vm.stepModel.createFiles[i]['eform']) {
                        							vm.maxWidthDialog = '800px';
                        							vm.showAlpacaJSFORM(vm.stepModel.createFiles[i])
                        						}
                        					}
                        				}
                        			}, 400);
                        		} else {
                        			vm.stepModel = null;
                        		}

                        		vm.processAssignUserIdItems = item.toUsers;
                        		console.log("Need interval refresh: " + needIntervalRefresh);
                        		if (needIntervalRefresh) {
                        		    setTimeout(function () {
                        		        vm.refreshProcess();
                        		        if(vm.processSteps.length === 1){
                        		        	vm.changeProcessStep(vm.processSteps[0]);
                        		        }
                        		        else {
                        		        	var length = vm.processSteps.length;
                        		        	for (var i = 0; i < length; i++) {
                        		        		var actionItem = vm.processSteps[i];
                        		        		if (item.actionCode === actionItem.actionCode) {
                        		        			vm.changeProcessStep(actionItem);
                        		        		}
                        		        	}
                        		        }
                        		      }.bind(this), 3000); 
                        		}
                        	}else {
                        		vm.maxWidthDialog = '800px';
                        		var urlPluginFormData = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/plugins/'+item.processActionId+'/formdata';
                        		var urlPluginFormScript = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/plugins/'+item.processActionId+'/formscript';
                        		var config_plugins = {
                        			headers: {
                        				'groupId': themeDisplay.getScopeGroupId()
                        			},
                        			dataType : "text"
                        		};

                        		axios.all([
                        			axios.get(urlPluginFormScript, config_plugins),
                        			axios.get(urlPluginFormData, config_plugins)
                        			]).then( axios.spread(function (urlResponesFormScript, urlResponesFormData) {
                        				var responseScript = urlResponesFormScript.data;
                        				var responseData = urlResponesFormData.data;

                        				console.log("responseScript==============",responseScript);
                        				item.plugin = true;

                        				if(responseScript.indexOf("#preview@pdf") !== -1){
                        					console.log("view pdf");
                        					var url ="/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/plugins/"+item.processActionId+"/preview" ;

                        					var config_blob =  {
                        						headers: {
                        							'groupId': themeDisplay.getScopeGroupId(),
                        						},
                        						responseType: 'blob'
                        					};

                        					axios.get(url, config_blob).then(function (response) {
                        						var urlblob = window.URL.createObjectURL(response.data);
                        						item.pdf = true;
                        						item.url = urlblob;
                        						item.no_pdf = '';
                        						vm.stepModel = item;
                        					})
                        					.catch(function (error) {
                        						console.log(error);
                        						item.pdf = true;
                        						item.url = '';
                        						item.no_pdf = 'Tài liệu đính kèm không tồn tại!';
                        						vm.stepModel = item;
                        					});

                        				}


                        				if(responseScript.indexOf("#preview@html") !== -1){
                        					console.log("view html");
                        					var config_view = {
                        						headers: {
                        							'groupId': themeDisplay.getScopeGroupId(),
                        						},
                        						dataType : "json"
                        					};
                        					item.html = true;
                        					item.no_html = "";
                        					vm.stepModel = item;

                        					var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/plugins/'+item.processActionId+'/previewhtml';
                        					axios.get(url, config_view).then(function (response) {
                        						item.no_html = "";
                        						vm.stepModel = item;

                        						var serializable = response.data ;

                        						var partNo = serializable.partNo;
                        						var dossierFileId = serializable.dossierFileId;

                        						$(".dossierFilePartNo").val("");
                        						$(".dossierFilePartNo").attr('id','dossierFileId'+partNo);
                        						$(".dossierFilePartNo").val(dossierFileId);

                        						var formReport = eval('('+serializable.formReport+')');
                        						var formData = eval('('+serializable.formData+')');

                        						console.log("formReport======",formReport);
                        						console.log("formData======",formData);
                        						/*var formData = serializable.formData;*/


                        						formReport.data = formData;
                        						console.log("formReport_____FINAL=======",formReport);
                        						$("#alpacajs_form_plugin").alpaca(formReport);
                        						

                        					})
                        					.catch(function (error) {
                        						console.log(error);
                        						item.html = true;
                        						item.no_html = "Tài liệu đính kèm không tồn tại!";
                        						vm.stepModel = item;
                        					});
                        				}

                        			}))
                        			.catch(function (error) {
                        				console.log(error);

                        			});
                        		}



                        	},
                        	refreshProcess : function(){
                        		var vm = this;
                        		vm._initchangeProcessStep();
                        		vm.stepModel = null;
                        		vm.dialogThuLyHoSo = false
                        	},
                        	postNextActions2 : function(item){
                        		console.log("itempostNextActions2============",item);
                        		var vm = this;
                        		var createFiles = item.createFiles;
                        		console.log("createFiles============",createFiles);
                        		var submitAlpacajsFns = new Array();

                        			/*for (var i = 0; i < createFiles.length; i++) {
                        				if(createFiles[i].eform){
                        					submitAlpacajsFns.push(vm.submitAlpacajsForm(createFiles[i]));
                        				}
                        				
                        			}*/
                        			var promise = new Promise(function(resolve, reject){
                        				try{
                        					vm.actionsSubmitLoading = true;
                        					$(".saveForm").click();
                        					resolve(true);
                        				}catch(e){
                        					resolve(false);
                        				}
                        			});


                        			promise.then(function(success){
                        				vm.postNextActions(item);
                        			}, function(error){

                        			});

                        			//console.log("submitAlpacajsFns============",submitAlpacajsFns);
                        			/*try{
                        				await Promise.all(submitAlpacajsFns);
                        			}catch(e){

                        			}*/

                        		
                        	},
                        	postNextActions: function (item){

                        		console.log(item);
                        		var vm = this;
                        		vm.actionsSubmitLoading = true;
                        		var fileArr = item.createFiles;
                        		var idArr = [];
                        		var waitingFiles = false;
							// var dossierFileId
							if (fileArr) {
								var length = fileArr.length;
								for (var i = 0; i < length; i++) {
									var fileDetail = fileArr[i];
									if (fileDetail.counter == 0) {
										waitingFiles = true;
										break;
									}
									var dossierFileId = fileDetail.dossierFileId;
									var dossierPartId = fileDetail.dossierPartId;
									if (dossierFileId && dossierPartId) {
										var strId = dossierFileId + ',' + dossierPartId;
										idArr.push(strId);
									}
								}
							}

							if(item.configNote){
								if(item.configNote.requiredNote && !$("textarea#processActionNote").val()){
									vm.snackbartextdossierViewJX = "Bạn phải nhập ý kiến trước khi gửi";
									vm.snackbarerordossierViewJX = true;
									vm.actionsSubmitLoading = false;
									return;
								}
							}

							console.log(idArr);

							var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/actions';

							var assignUserId = 0;
							var subUsers = '';
							
							if (vm.alpacaAssignUserId != 0) {
								
								assignUserId = vm.alpacaAssignUserId;
								subUsers = JSON.stringify(vm.subUsers);
								
							} else if (vm.processAssignUserId.userId > 0) {
								
								assignUserId = vm.processAssignUserId.userId;
								
							}

							
							var isKyOk = item.eSignature;
							//console.log("plugin().valid ==============>",plugin().valid);
							// TODO
							/*if (isKyOk && !plugin().valid) {
								alert("Plugin is not working :(");
				                vm.actionsSubmitLoading = false;
				                isKyOk = false;
				                return;
							} else if (!isKyOk) {
								isKyOk = true;
							} else if(){
								var x = plugin().Sign('tGp5xOELB59zprQeqh2ks89BgWE=');
								console.log(x);
								isKyOk = true;
							}*/
//							console.log("Plugin: " + plugin());
							if (isKyOk) {
								if (!plugin().valid) {
									alert("Plugin is not working :(");
									vm.actionsSubmitLoading = false;
									isKyOk = false;
									return;
								}
								if (waitingFiles) {
									alert("Tệp điện tử chưa sẵn sàng. Xin vui lòng chờ một lát!");
									vm.actionsSubmitLoading = false;
									return;
								}
							}
						/*	if(!plugin().valid === 0){
				                alert("Plugin is not working :(");
				                vm.actionsSubmitLoading = false;
				                isKyOk = false;
				                return;
				            } else {
				                var x = plugin().Sign('tGp5xOELB59zprQeqh2ks89BgWE=');
								console.log(x);
								isKyOk = true;
							}*/
														
							if (!isKyOk) {
								$.ajax({
									url: url,
									headers: {
										"groupId": themeDisplay.getScopeGroupId()
									},
									data: {
										"actionCode": item.actionCode,
										"actionUser": themeDisplay.getUserName(),
										"actionNote": $("textarea#processActionNote").val(),
										"assignUserId": assignUserId,
										"subUsers": subUsers
									},
									type: 'POST',
									dataType: 'json',
									contentType: 'application/x-www-form-urlencoded; charset=utf-8',
									success: function(data, textStatus, xhr) {

										vm.snackbartextdossierViewJX = item.actionName + " thành công!";
										vm.snackbardossierViewJX = true;
										
										
										setTimeout(function(){ 
											vm._inidanhSachHoSoTable();
											vm.reloadCounter();
										}, 1000);
										vm.detailPage = false;
										vm.actionsSubmitLoading = false;

									},
									error: function(xhr, textStatus, errorThrown) {

										vm.snackbartextdossierViewJX = item.actionName + " thất bại!";
										vm.snackbarerordossierViewJX = true;
										vm.actionsSubmitLoading = false;
									}
								});
							} else {
								if (idArr) {
									var paramObj = {};
									paramObj.actionCode = item.actionCode;
									paramObj.actionUser = themeDisplay.getUserName();
									paramObj.actionNote = $("textarea#processActionNote").val();
									paramObj.assignUserId = assignUserId;
									paramObj.subUsers = subUsers;
									paramObj.postStepCode = item.postStepCode;

									var strIdArr = idArr.join(";");
									var actionName = item.actionName;
									console.log(strIdArr);
									vm.kyDuyetYCGiamDinh(strIdArr,paramObj, actionName);
								}
							}

						},
						kyDuyetYCGiamDinh: function(strIdArr,paramObj, actionName) {

							var vm = this;
							var url = '/o/rest/v2/digitalSignature/'+vm.detailModel.dossierId+'/hashComputed';
							console.log(vm.detailModel.dossierId);
							
							$.ajax({
								type : 'POST',
								url : url,
								async: false,//bat dong bo = fale, dong bo voi client
								dataType: 'json',
								data : {
									// type: 'kyDuyetYCGiamDinh',
									strIdArr: strIdArr,
									actionCode: paramObj.actionCode,
									postStepCode: paramObj.postStepCode
									// strDossierPartId: strDossierPartId
									/*id : controlRequirementId*/
								},
								success : function(result) {
									console.log(result);
									/*var jsonData = JSON.parse(result);*/
									var msgs = result.msg;
									if (msgs !== 'fileEntryId') {
										var hashComputers = result.hashComputers;
										var signFieldNames = result.signFieldNames;
										var fileNames = result.fileNames;
										var fileEntryIds = result.fileEntryId;
										console.log("hashComputers: "+hashComputers);
										console.log("signFieldNames: "+signFieldNames);
										console.log("fileNames: "+fileNames);
										console.log("msgs: "+msgs);			
										vm.actionsSubmitLoading = false;
										var signs = [];
										
										if(plugin().valid) {
											
											for ( var i = 0; i < hashComputers.length; i++) {

												var hashComputer = hashComputers[i];
												var code = plugin().Sign(hashComputer);
												
												if(code === 0 || code === 7){			
													var sign = plugin().Signature;
													var signFieldName = signFieldNames[i];
													var fileName = fileNames[i];
													console.log("sign: "+sign);
													console.log("signFieldName: "+signFieldName);
													console.log("fileName: "+fileName);
													signs.push(sign);
//													var msg = msgs[i];
//													if(msg == 'success') {
//														try {
//															console.log('Complete sign');
//															vm.completeKyDuyetYCGiamDinh(sign, signFieldName, fileName, fileEntryId, paramObj, actionName);
//														}
//														catch(err) {
//															console.log(err);
//														}
//													}else{
//														alert(msg);
//													}	
												}else{
													alert(plugin().ErrorMessage);
												}
											}
																						
											vm.completeKyDuyetManyYCGiamDinh(signs.join(','), signFieldNames.join(','), fileNames.join(','), fileEntryIds.join(','), paramObj, actionName);
										} else {
											alert("Plugin is not working");
										}
									} else {
										alert("Cảnh báo: Không tìm thấy file bản thảo. Tạm dừng tiến trình ký số");
									}
								},
								error : function(result){
									vm.actionsSubmitLoading = false;
								}
							});
						},
						completeKyDuyetManyYCGiamDinh: function(signs, signFieldNames, fileNames, fileEntryIds,paramObj, actionName) {
							var vm = this;
							var url = '/o/rest/v2/digitalSignature/'+vm.detailModel.dossierId+'/dossierFiles';
							$.ajax({
								type : 'PUT',
								url : url,
								async: false,//bat dong bo = fale, dong bo voi client
								dataType : 'json',
								headers: {
									"groupId": themeDisplay.getScopeGroupId()
								},
								data: {
									actionCode: paramObj.actionCode,
									actionUser: paramObj.actionUser,
									actionNote: paramObj.actionNote,
									assignUserId: paramObj.assignUserId,
									postStepCode: paramObj.postStepCode,
									subUsers: paramObj.subUsers,
									sign: signs,
									signFieldName: signFieldNames,
									fileName: fileNames,
									fileEntryId: fileEntryIds
								},
								success : function(result) {
									console.log(result);

									var msg = result.msg;
									if(msg == 'success'){
										vm.snackbartextdossierViewJX = actionName + " thành công!";
										vm.snackbardossierViewJX = true;

										vm._inidanhSachHoSoTable();
										setTimeout(function(){ 
											vm._initlistgroupHoSoFilter();
										}, 1000);

										vm.detailPage = false;
										vm.actionsSubmitLoading = false;
									} else if (msg == 'fileEntryId') {
										alert("Cảnh báo: Không tìm thấy file bản thảo. Tạm dừng tiến trình đóng dấu số");
									} else {
										alert(msg);
									}									
								},
								error: function(){
									alert('ky so false');
								}
							})
						},						
						completeKyDuyetYCGiamDinh: function(sign, signFieldName, fileName, fileEntryId,paramObj, actionName) {
							var vm = this;
							var url = '/o/rest/v2/digitalSignature/'+vm.detailModel.dossierId+'/dossierFile';
							$.ajax({
								type : 'PUT',
								url : url,
								async: false,//bat dong bo = fale, dong bo voi client
								dataType : 'json',
								headers: {
									"groupId": themeDisplay.getScopeGroupId()
								},
								data: {
									actionCode: paramObj.actionCode,
									actionUser: paramObj.actionUser,
									actionNote: paramObj.actionNote,
									assignUserId: paramObj.assignUserId,
									postStepCode: paramObj.postStepCode,
									subUsers: paramObj.subUsers,
									sign: sign,
									signFieldName: signFieldName,
									fileName: fileName,
									fileEntryId: fileEntryId
								},
								success : function(result) {
									console.log(result);
									/*var jsonData = JSON.parse(result);*/
									var msg = result.msg;
									if(msg == 'success'){
//										alert('ký số thành công!');
										vm.snackbartextdossierViewJX = actionName + " thành công!";
										vm.snackbardossierViewJX = true;

										vm._inidanhSachHoSoTable();
										setTimeout(function(){ 
//											alert('Next action signature');
											vm._initlistgroupHoSoFilter();
										}, 1000);

										vm.detailPage = false;
										vm.actionsSubmitLoading = false;
									} else if (msg == 'fileEntryId') {
										alert("Cảnh báo: Không tìm thấy file bản thảo. Tạm dừng tiến trình đóng dấu số");
									} else {
										alert(msg);
									}
								},
								error: function(){
									alert('ky so false');
								}
							})
						},
						_initchangeProcessStep: function (){
							var vm = this;
							vm.stepLoading = true;
							var timestamp = new Date().getTime();
							var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/nextactions?timestamp=' + timestamp;
							var urlPlugin = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/plugins?timestamp=' + timestamp;
                            // var url = '/o/frontendwebdossier/json/steps.json';
                            
                            axios.all([
                            	axios.get(url, config),
                            	axios.get(urlPlugin, config)
                            	]).then( axios.spread(function (urlRespones, urlPluginsRespones) {

                            		var serializable = urlRespones.data.data;
                            		var serializablePlugins = urlPluginsRespones.data.data;
                            		var serializablePluginsConvert = [];
                            		var serializableNextActionConvert = [];

                            		if(serializable){
                            			for (var i = 0; i < serializable.length; i++) {
                            				serializable[i].type = 1;
                            				if(!serializable[i].autoEvent){
                            					if(serializable[i].configNote){
                            						var configNote = JSON.parse(serializable[i].configNote);
                            						serializable[i].configNote = configNote;

                            					}
                            					serializableNextActionConvert.push(serializable[i]);
                            				}
                            				
                            			}
                            		}else {
                            			serializable = [];
                            			serializableNextActionConvert = [];
                            		}


                            		if(serializablePlugins){
                            			for (var i = 0; i < serializablePlugins.length; i++) {
                            				var plugin = {
                            					type : 2,
                            					processActionId : serializablePlugins[i].processPluginId,
                            					actionName : serializablePlugins[i].pluginName
                            				};
                            				serializablePluginsConvert.push(plugin);
                            			}
                            		}


                            		var nextactions = serializableNextActionConvert;
                            		var plugins = serializablePluginsConvert;


                            		vm.processSteps = $.merge( nextactions, plugins );
                            		
                            		var processStepsLength = vm.processSteps.length;
                            		
                            		var needIntervalRefresh = false;
                            		for (var rc = 0; rc < processStepsLength; rc++) {
                            			var item = vm.processSteps[rc];
                            			
                                		var fileArr = item.createFiles;
            							if (fileArr && fileArr.length) {
            								var length = fileArr.length;                        		
        	                        		for (var i = 0; i < length; i++) {
        	                        			var fileItem = fileArr[i];
        	                        			if (fileItem.counter == 0 && (!fileItem.eform) && (!fileItem.returned) && (fileItem.dossierFileId != 0)) {
        	                        				needIntervalRefresh = true;
        	                        				break;
        	                        			}
        	                        		}
            							}
            							else if (fileArr) {
                                			var fileItem = fileArr;
                                			if (fileItem.counter == 0 && (!fileItem.eform) && (!fileItem.returned) && (fileItem.dossierFileId != 0)) {
                                				needIntervalRefresh = true;
                                			}    								
            							}     
            							if (needIntervalRefresh) break;
                            		}
                            		
                            		if (needIntervalRefresh) {
                            		    setTimeout(function () {
                            		        vm.refreshProcess();
                            		        nOfRetry++;
                            		      }.bind(this), 3000);                             			
                            		}
                            		else {
                                		vm.stepLoading = false;
                                		console.log(vm.processSteps);

                                		//neu processSteps chi co 1, trigger chanProcessStep 
                                		if(vm.processSteps.length === 1){
                                			vm.changeProcessStep(vm.processSteps[0]);
                                		}                            			
                            		}

                            	}))
                            	.catch(function (error) {
                            		console.log(error);
                            		vm.stepLoading = false;

                            	});
                            	return false; 
                            },
                            filterAllDossierWithOutStatus: function () {
                            	this.stageFilterView = 'danh_sach';
                            	this.listgroupHoSoFilterselectedIndex = -1;
                            	this.traCuuFilter = false;
                            	this.detailRegistPage = false;
                            	this._inidanhSachHoSoTable(false);
                            },
                            filterTraCuu: function () {
                            	this.traCuuFilter = true;
                            	this.detailRegistPage = false;
                            },
                            singleFileUpload: function (item) {
                            	var vm = this;
                            	vm.actionsSubmitLoading = true;

                            	document.getElementById('inputfile_'+item.dossierPartId).click();
                            	setTimeout(function(){ 
                            		vm.actionsSubmitLoading = false;
                            	}, 4000);
                            },
                            singleFileUploadLeave: function () {
                            	var vm = this;
                            	vm.actionsSubmitLoading = false;
                            },
                            singleFileUploadInput: function (e, item, index) {

                            	var files = e.target.files || e.dataTransfer.files;

                            	var file = files[0];

                            	var vm = this;
                            	vm.actionsSubmitLoading = true;

                            	var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/files';

                            	var data = new FormData();
                            	data.append( 'displayName', file.name );
                            	data.append( 'file', file );
                            	data.append( 'dossierPartNo', item.partNo );
                            	data.append( 'dossierTemplateNo', vm.detailModel.dossierTemplateNo );
                            	data.append( 'fileTemplateNo', item.templateFileNo );
                            	data.append( 'fileType', '' );
                            	data.append( 'isSync', item.returned );
                            	data.append( 'referenceUid', '' );
                            	data.append( 'formData', '' );

                            	$.ajax({
                            		type : 'POST', 
                            		url  : url, 
                            		data : data,
                            		dataType: 'json',
                            		headers: {"groupId": themeDisplay.getScopeGroupId()},
                            		processData: false,
                            		contentType: false,
                            		cache: false,
                            		async : false,
                            		success :  function(result){ 
                            			vm.actionsSubmitLoading = false;
                            			vm.snackbartextdossierViewJX = " Tải file thành công!";
                            			vm.snackbardossierViewJX = true;
                            			
									//vm._initchangeProcessStep();
									vm.stepModel.createFiles[index].counter = vm.stepModel.createFiles[index].counter + 1;
									vm.dossierFiles.push(result);
									
								},
								error:function(result){
									vm.actionsSubmitLoading = false;
									vm.snackbartextdossierViewJX = "Tải file thất bại!";
									vm.snackbarerordossierViewJX = true;
								}
							});
							/**
                            axios.post(url, postData, config).then(function (response) {
                                var serializable = response.data;

								vm._initchangeProcessStep();

                            })
                                .catch(function (error) {
                                    console.log(error);
									vm.snackbartextdossierViewJX = item.actionName + " thất bại!";
                      				vm.snackbarerordossierViewJX = true;
                                });
                                */
                            },
                            undoDetailPage: function () {

                            	this.detailPage = !this.detailPage;
                            	setTimeout(function(){ 
								// temp fix header
								$('.danhSachHoSoTable__class th[role="columnheader"]').each(function( index ) {
									if ($( this ).attr('aria-label').indexOf("Activate") > 0) {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>") + ' <i aria-hidden="true" class="material-icons icon">arrow_upward</i>');
									} else {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>"));
									}
								});
							}, 300);
                            },
                            undoDetailRegistPage: function () {
                            	this.detailRegistPage = !this.detailRegistPage;
                            	setTimeout(function(){ 
								// temp fix header
								$('.thongTinDoanhNghiepTable__class th[role="columnheader"]').each(function( index ) {
									if ($( this ).attr('aria-label').indexOf("Activate") > 0) {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>") + ' <i aria-hidden="true" class="material-icons icon">arrow_upward</i>');
									} else {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>"));
									}
								});
							}, 300);
                            },
                            onScroll(e) {
                            	this.offsetTop = window.pageYOffset || document.documentElement.scrollTop
                            },
                            onScrollTop (e) {
                            	window.scrollBy(0, -99999)
                            },
                            newTabOrNewWindown : function(){
                            	var vm = this;

                            	console.log("===============run newTabOrNewWindown============");

                            	if(stateWindowParam === "stateWindow"){

                            		if(dossierIdParam){
                            			var urlDetailDossier = '/o/rest/v2/dossiers/'+dossierIdParam;

                            			axios.get(urlDetailDossier, config).then(function (response) {
                            				var detailDossier = response.data;
                            				vm.detailModel = detailDossier;

                            				var url = "/o/rest/v2/dossiertemplates/"+detailDossier.dossierTemplateNo;
                            				var urlFiles = "/o/rest/v2/dossiers/"+dossierIdParam+"/files";

                            				axios.all([
                            					axios.get(url, config),
                            					axios.get(urlFiles, config)
                            					])
                            				.then(axios.spread(function (urlRespones, urlFilesRespones) {

                            					var serializableTemplate = urlRespones.data;
                            					var serializableFiles = urlFilesRespones.data.data;

                            					for (var i = 0; i < serializableTemplate.dossierParts.length; i++) {
                            						var itemPart = serializableTemplate.dossierParts[i];
                            						if(itemPart.partNo === dossierPartNo){
                            							vm.dossierViewJXTitle = itemPart.partName;
                            						}

                            					}

                            					var listFilesUpload = [];
                            					for (var key in serializableFiles){
                            						if (serializableFiles[key].dossierPartNo === dossierPartNo) {
                            							if(serializableFiles[key].fileSize > 0){
                            								listFilesUpload.push(serializableFiles[key]);
                            							}
                            							
                            						}
                            					}
                            					vm.listDocumentInPartNoItems = listFilesUpload;
                            					
                            					vm.popUpViewDossierFile = !vm.popUpViewDossierFile;

                            					vm._initCbxDocumentNewTab(listFilesUpload);

                            					/*return Promise.reject();*/

                            				})).catch(function (error) {
                            					console.log(error);

                            				});

                            			})
                            			.catch(function (error) {
                            				console.log(error);

                            			});

                            		}
                            	}
                            }
                        }
                    },
    //                 'listgroupHoSoFilter': {
    //                 	'id': 'listgroupHoSoFilter',
    //                 	'name': 'listgroupHoSoFilter',
    //                 	"type": "listgroup",
    //                 	'show_counter': true,
    //                 	'counter': 'count',
    //                 	"child_items": "items",
    //                 	"data_title": "title",
    //                 	"data_value": "id",
    //                 	"onLoad": "_initlistgroupHoSoFilter",
    //                 	"onClick": "groupHoSoFilter",
    //                 	"events": {
      //               		groupHoSoFilter: function(item){
      //               			var vm = this;

      //               			console.log("item status",item);

      //               			vm.reloadCounter();
                    			
                    			
      //               			vm.detailPage = false;
      //               			vm.detailRegistPage = false;
      //               			vm.listgroupHoSoFilterselected = item.id;
      //               			vm.danhSachHoSoTablepage = 1;

      //               			vm.listgroupHoSoFilterselectedIndex = item.index - 1;

      //               			if ( item.id !== 'tra_cuu' ){
      //               				vm.stageFilterView = item.id;
      //               				vm.hoso_title_table = item.title;
      //               				if (item.level === 1) {
      //               					vm.statusParamFilter = item.idSub;
      //               					vm.substatusParamFilter = item.id;
      //               				} else {
      //               					vm.statusParamFilter = item.id;
      //               					vm.substatusParamFilter = item.idSub;
      //               				}

      //               			} 



      //               			if (item.id == 'tra_cuu_hoso') {
      //               				vm._initraCuuHoSoTable(false);
      //               			} else if (item.id == 'tra_cuu_phuong_tien') {
						// 		// TODO vm._inidanhSachHoSoTable(false);
						// 	} else if (item.id == 'tra_cuu_thong_tin_doanh_nghiep') {
						// 		vm._inithongTinDoanhNghiepTable(false);
						// 		vm.detailRegistPage = false;
						// 	} else {
						// 		vm._inidanhSachHoSoTable(false);
						// 	}
						// 	vm.onScrollTop();

						// },
						// reloadCounter : function(){
						// 	var vm = this;

						// 	vm.stageFilterView = 'danh_sach';

						// 	/*var url = '/o/rest/v2/statistics/dossiers/todo';*/
						// 	var url = '/o/rest/v2/statistics/dossiers/todo';
						// 	console.log("Init load");
						// 	$.ajax({
						// 		url : url,
						// 		dataType : "json",
						// 		type : "GET",
						// 		headers : {
						// 			'groupId': themeDisplay.getScopeGroupId(),
						// 		},
						// 		data: {
						// 			service: vm.serviceInfoFilter.serviceCode,
						// 			agency: agencies
						// 		},								
						// 		success : function(result){
						// 			var serializable = result;

						// 			var indexTree = -1;
						// 			var index = 0;
						// 			//console.log("listgroupHoSoFilterItems=======FISRT",vm.listgroupHoSoFilterItems);
						// 			//console.log("serializable=======",serializable.data);
						// 			for (var key in serializable.data) {
						// 				for(var i in vm.listgroupHoSoFilterItems){
						// 					if ( serializable.data[key].level === 0) {

						// 						if (serializable.data[key].dossierStatus === 'cancelling' ||
						// 							serializable.data[key].dossierStatus === 'cancelled' ||
						// 							serializable.data[key].dossierStatus === 'processing' ||
						// 							serializable.data[key].dossierStatus === 'paid') {
						// 							serializable.data[key].items = [];

						// 						if(serializable.data[key].dossierStatus === vm.listgroupHoSoFilterItems[i].id){
						// 							vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
						// 						}
						// 					}

						// 				} else {

						// 					if(serializable.data[key].dossierSubStatus === vm.listgroupHoSoFilterItems[i].id){
						// 						vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
						// 					}
						// 				}
						// 			}

						// 		}
						// 		//console.log("listgroupHoSoFilterItems=======LAST",vm.listgroupHoSoFilterItems);

						// 	},
						// 	error : function(result){

						// 	}
						// });	
							
						// },
						// _initlistgroupHoSoFilter: function(select){
						// 	var vm = this;

						// 	vm.newTabOrNewWindown();
						// 	vm.setStateOnlyFollow(emailAddress);
						// 	vm.getLastedStateUserInfo(function(){
						// 		vm._inidanhSachHoSoTable(false);
						// 	});

						// 	vm.stageFilterView = 'danh_sach';

						// 	vm.listgroupHoSoFilterItems = [];

						// 	/*var url = '/o/rest/v2/statistics/dossiers/todo';*/
						// 	var url = '/o/rest/v2/statistics/dossiers/todo';
						// 	console.log("Init load 2");
						// 	axios.get(url, config).then(function (response) {
						// 		var serializable = response.data;

						// 		var indexTree = -1;
						// 		var index = 0;
						// 		for (var key in serializable.data) {
									
						// 			if ( serializable.data[key].level === 0) {
										
						// 				if (serializable.data[key].dossierStatus === 'cancelling' ||
						// 					serializable.data[key].dossierStatus === 'cancelled' ||
						// 					serializable.data[key].dossierStatus === 'processing' ||
						// 					serializable.data[key].dossierStatus === 'paid') {
						// 					serializable.data[key].items = [];

						// 				vm.listgroupHoSoFilterItems.push({
						// 					id: serializable.data[key].dossierStatus,
						// 					idSub: serializable.data[key].dossierSubStatus,
						// 					title: serializable.data[key].statusName,
						// 					level: serializable.data[key].level,
						// 					count: serializable.data[key].count,

						// 					action: 'folder',
						// 					action_active: 'folder_open',
						// 					items: [],
						// 					index: index
						// 				});
						// 			}

						// 		} else {
									
						// 			vm.listgroupHoSoFilterItems.push({
						// 				id: serializable.data[key].dossierSubStatus,
						// 				idSub: serializable.data[key].dossierStatus,
						// 				title: serializable.data[key].statusName,
						// 				level: serializable.data[key].level,
						// 				count: serializable.data[key].count,
						// 				action: 'folder',
						// 				action_active: 'folder_open',
						// 				items: [],
						// 				index: index
						// 			});
						// 		}
								
						// 	}

						// 	if(select){
						// 		console.log("selected========",select);
						// 		vm.listgroupHoSoFilterselected = select;
						// 	}


						// })
						// 	.catch(function (error) {
						// 		console.log(error);
								
						// 	});
						// 	return false; 
						// }
				// 	}
				// },
				// 'listgroupTraCuuFilter': {
				// 	'id': 'listgroupTraCuuFilter',
				// 	'name': 'listgroupTraCuuFilter',
				// 	"type": "listgroup",
				// 	'cssClass': 'pb-5',
				// 	"child_items": "items",
				// 	"data_title": "title",
				// 	"data_value": "id",
				// 	"onLoad": "_initlistgroupTraCuuFilter",
				// 	"onClick": "filterChange",
				// 	"events": {
				// 		filterChange: function(item){
				// 			var vm = this;
				// 			vm.detailPage = false;
				// 			vm.detailRegistPage = false;
				// 			vm.listgroupTraCuuFilterselected = item.id;
				// 			if ( item.id !== 'tra_cuu' ){
				// 				vm.stageFilterView = item.id;
				// 				vm.hoso_title_table = item.title;
				// 			}
							
				// 			if (item.id == 'tra_cuu_hoso') {
				// 				vm._initraCuuHoSoTable(false);
				// 				vm._initAdvanced_filter_serviceInfo();
				// 				vm._initAdvanced_filter_loaiSanPham();
				// 				vm._initAdvanced_filter_nhanHieu();
				// 				vm._initAdvanced_filter_dossierStatus();
								
				// 			} else if (item.id == 'tra_cuu_giay_phep') {
				// 				console.log("GO------------")
				// 				vm._inigiayPhepVanTaiQuocTeTable(false);
				// 				vm._initServiceInfos({
				// 					serviceCode: configPage.serviceinfosQuocTe
				// 				});
				// 				vm._initGovAgencys({

				// 				});
				// 				vm._initTuyens();

				// 			}else if(item.id === 'tat_ca_hoso'){

								// vm.detailPage = false;
								// vm.detailRegistPage = false;
								// vm.listgroupHoSoFilterselected = item.id;
								// vm.danhSachHoSoTablepage = 1;

								// vm.listgroupHoSoFilterselectedIndex = item.index - 1;

								// vm.statusParamFilter = '';
								// vm.substatusParamFilter = '';

								// vm._inidanhSachHoSoTable(false);

								// vm.onScrollTop();
				// 			}
							
				// 		},
				// 		_initlistgroupTraCuuFilter: function(){
				// 			var vm = this;
							
				// 			vm.listgroupTraCuuFilterItems = [
				// 			{
				// 				id: 'tat_ca_hoso',
				// 				title: 'Tất cả hồ sơ'
				// 			},
				// 			// {
				// 			// 	id: 'tra_cuu_phuong_tien',
				// 			// 	title: 'Phương tiện sản xuất lắp ráp'
				// 			// },
				// 			// {
				// 			// 	id: 'tra_cuu_thong_tin_doanh_nghiep',
				// 			// 	title: 'Thông tin doanh nghiệp'
				// 			// },
				// 			{
				// 				id: 'tra_cuu_giay_phep',
				// 				title: 'Tra cứu giấy phép'
				// 			}
				// 			];
							
				// 		}
				// 	}
				// },
// 				'listgroupBaoCaoFilter': {
// 					'id': 'listgroupBaoCaoFilter',
// 					'name': 'listgroupBaoCaoFilter',
// 					"type": "listgroup",
// 					"child_items": "items",
// 					"data_title": "title",
// 					"data_value": "id",
// 					"onLoad": "_initlistgroupBaoCaoFilter",
// 					"onClick": "filterChangeBaoCao",
// 					"events": {
// 						filterChangeBaoCao: function(item){
// 							var vm = this;
// 							vm.detailPage = false;
// 							vm.detailRegistPage = false;
// 							vm.listgroupTraCuuFilterselected = item.id;
// 							if ( item.id !== 'tra_cuu' ){
// 								vm.stageFilterView = item.id;
// 								vm.hoso_title_table = item.title;
// 							}
							
// 							if (item.id == 'tra_cuu_hoso') {
// 								vm._initraCuuHoSoTable(false);
// 								vm._initAdvanced_filter_serviceInfo();
// 								vm._initAdvanced_filter_loaiSanPham();
// 								vm._initAdvanced_filter_nhanHieu();
								
// 							} else if (item.id == 'tra_cuu_phuong_tien') {
// 								// TODO vm._inidanhSachHoSoTable(false);
// 							} else if (item.id == 'tra_cuu_thong_tin_doanh_nghiep') {
// 								vm._inithongTinDoanhNghiepTable(false);
// 							}
							
// 						},
// 						_initlistgroupBaoCaoFilter: function(){
// 							var vm = this;

// //							vm.listgroupBaoCaoFilterItems = [
// //							{
// //								id: 'tham_dinh_bao_cao',
// //								title: 'Thẩm định thiết kế'
// //							},
// //							{
// //								id: 'kiem_tra_san_pham_mau',
// //								title: 'Kiểm tra sản phẩm mẫu'
// //							},
// //							{
// //								id: 'danh_gia_cop',
// //								title: 'Đánh giá COP'
// //							}
// //							];
							
// 						}
// 					}
// 				},
				'keywordsSearchTraCuuHoSo': {
					'id': 'keywordsSearchTraCuuHoSo',
					'name': 'keywordsSearchTraCuuHoSo',
					'type': 'text',
					'cssClass': 'pt-1',
					'placeholder': 'Tìm kiếm theo từ khoá...',
					'solo': true,
					'append_icon': 'search',
					'onKeyup': 'filterTraCuuHoSoKeywords',
					'events': {
						filterTraCuuHoSoKeywords: function (event) {
							var vm = this;
							if (event.keyCode === 13 || vm.keywordsSearchTraCuuHoSo.length > 3 || vm.keywordsSearchTraCuuHoSo.length === 0) {
								vm._initraCuuHoSoTable(false);
							}

						}
					}
				},
				'keywordsSearchTraCuuPhuongTien': {
					'id': 'keywordsSearchTraCuuPhuongTien',
					'name': 'keywordsSearchTraCuuPhuongTien',
					'type': 'text',
					'cssClass': 'pt-1',
					'placeholder': 'Tìm kiếm theo từ khoá...',
					'solo': true,
					'append_icon': 'search',
					'onKeyup': 'filterTraCuuPhuongTienKeywords',
					'events': {
						filterTraCuuPhuongTienKeywords: function (event) {
							var vm = this;
							if (event.keyCode === 13 || vm.keywordsSearchTraCuuPhuongTien.length > 3 || vm.keywordsSearchTraCuuPhuongTien.length === 0) {
								
							}

						}
					}
				},
				'keywordsSearchTraCuuDoanhNghiep': {
					'id': 'keywordsSearchTraCuuDoanhNghiep',
					'name': 'keywordsSearchTraCuuDoanhNghiep',
					'type': 'text',
					'cssClass': 'pt-1',
					'placeholder': 'Tìm kiếm theo từ khoá...',
					'solo': true,
					'append_icon': 'search',
					'onKeyup': 'filterTraCuuDoanhNghiepKeywords',
					'events': {
						filterTraCuuDoanhNghiepKeywords: function (event) {
							var vm = this;
							if (event.keyCode === 13 || vm.keywordsSearchTraCuuDoanhNghiep.length > 3 || vm.keywordsSearchTraCuuDoanhNghiep.length === 0) {
								vm._inithongTinDoanhNghiepTable(false);
							}

						}
					}
				},
				// 'keywordsSearchDanhSachHoSo': {
				// 	'id': 'keywordsSearchDanhSachHoSo',
				// 	'name': 'keywordsSearchDanhSachHoSo',
				// 	'type': 'text',
				// 	'cssClass': 'pt-1',
				// 	'placeholder': 'Tìm kiếm theo từ khoá...',
				// 	'solo': true,
				// 	'append_icon': 'search',
				// 	'onKeyup': '_filterDanhSachHoSoKeyup($event)',
				// 	'events': {
				// 		_filterDanhSachHoSoKeyup: function (event) {
				// 			var vm = this;
				// 			if (event.keyCode === 13 || vm.keywordsSearchDanhSachHoSo.length > 3 || vm.keywordsSearchDanhSachHoSo.length === 0) {
				// 				vm._inidanhSachHoSoTable(false);
				// 			}

				// 		}
				// 	}
				// },
				// "serviceInfoFilter": {
				// 	'id': 'serviceInfoFilter',
				// 	'name': 'serviceInfoFilter',
				// 	"type": "hidden",
				// 	'label': 'Lựa chọn thủ tục hành chính ',
				// 	"item_text": "serviceName",
				// 	"item_value": "serviceCode",
				// 	"single_line": true,
				// 	"hide_selected": true,
				// 	"multiple": false,
				// 	"tags": false, //custom input not avaliable in select
				// 	"chips": true,
				// 	"deletable_chips": true,
				// 	"loading": false,
				// 	"no_data_text": "Lua chon selected",
				// 	"items": [],
				// 	"onLoad": "_initServiceInfoFilterData",
				// 	"onChange": "_filterDanhSachHoSoOnchange($event)",
				// 	"onRemove": "_filterDanhSachHoSoOnRemove",
				// 	'onClick': '_filterDanhSachHoSoOnClear',
				// 	"events": {
						// _initServiceInfoFilterData: function(){
						// 	var vm = this;
							
						// 	//TODO: API
						// 	var url = '/o/rest/v2/serviceinfos';
							
						// 	axios.get(url, config).then(function (response) {
						// 		var serializable = response.data;

						// 		vm.serviceInfoFilterItems = serializable.data;

						// 	})
						// 	.catch(function (error) {
						// 		console.log(error);

						// 	});
						// 	return false; 
							
						// },
				// 		_filterDanhSachHoSoOnchange: function(data){
				// 			var vm = this;
				// 			vm.serviceInfoFilter = data;
				// 			vm._inidanhSachHoSoTable(false);
				// 			vm.putStateUserInfo();
				// 		},
				// 		_filterDanhSachHoSoOnRemove: function(){
				// 			var vm = this;
				// 			vm.serviceInfoFilter = [];
				// 			vm.putStateUserInfo();
				// 			vm._inidanhSachHoSoTable(false);
				// 		},
				// 		_filterDanhSachHoSoOnClear: function(){
				// 			this.serviceInfoFilter = [];
				// 		}

				// 	}
				// },
				// "applicantNameFilter": {
				// 	'id': 'applicantNameFilter',
				// 	'name': 'applicantNameFilter',
				// 	"type": "select",
				// 	'label': 'Chủ hồ sơ ',
				// 	"item_text": "applicantName",
				// 	"item_value": "applicantIdNo",
				// 	"single_line": true,
				// 	"hide_selected": true,
				// 	"multiple": false,
				// 	"tags": false, //custom input not avaliable in select
				// 	"chips": true,
				// 	"deletable_chips": true,
				// 	"loading": false,
				// 	"no_data_text": "Lua chon selected",
				// 	"items": [],
				// 	"onLoad": "_initApplicantNameFilterData",
				// 	"onChange": "_applicantFilterDanhSachHoSoOnchange($event)",
				// 	"onRemove": "_applicantFilterDanhSachHoSoOnRemove",
				// 	'onClick': '_applicantFilterDanhSachHoSoOnClear',
				// 	"events": {
						// _initApplicantNameFilterData: function(){
						// 	var vm = this;
							
						// 	//TODO: API
						// 	var url = '/o/rest/v2/applicants';
							
						// 	axios.get(url, config).then(function (response) {
						// 		var serializable = response.data;

						// 		vm.applicantNameFilterItems = serializable.data;
								
						// 	})
						// 	.catch(function (error) {
						// 		console.log(error);

						// 	});
						// 	return false; 
						// },
				// 		_applicantFilterDanhSachHoSoOnchange: function(data){
				// 			var vm = this;
				// 			vm.applicantNameFilter = data;
				// 			vm._inidanhSachHoSoTable(false);
				// 			vm.putStateUserInfo();
				// 		},
				// 		_applicantFilterDanhSachHoSoOnRemove: function(){
				// 			var vm = this;
				// 			vm.applicantNameFilter = [];
				// 			vm._inidanhSachHoSoTable(false);
				// 			vm.putStateUserInfo();
				// 		},
				// 		_applicantFilterDanhSachHoSoOnClear: function(){
				// 			this.applicantNameFilter = [];
				// 		}
				// 	}
				// },
				// "dossierNoFilter": {
				// 	'id': 'dossierNoFilter',
				// 	'name': 'dossierNoFilter',
				// 	"type": "text",
				// 	'label': 'Nhập mã tiếp nhận (Số hồ sơ)',
				// 	"onChange": "_filterDanhSachHoSoOnchange3",
				// 	"events": {
				// 		_filterDanhSachHoSoOnchange3: function(){
				// 			var vm = this;
				// 			vm._inidanhSachHoSoTable(false);
				// 			vm.putStateUserInfo();
				// 		}
				// 	}
				// },
				'thongTinDoanhNghiepTable': {
					'id': 'thongTinDoanhNghiepTable',
					'name': 'thongTinDoanhNghiepTable',
					'type': 'table',
					'no_data_text': 'Không tìm thấy hồ sơ thương nhân nào!',
					'cssClass': 'thongTinDoanhNghiepTable__class',
					'item_key': 'registrationId',
					'headers': 'headers',
					'template': 'thong_tin_doanh_nghiep_table_template',
					'pagging': '_paggingThongTinDoanhNghiepTable',
					'next': '_nextThongTinDoanhNghiepTable',
					'previous': '_previousThongTinDoanhNghiepTable',
					'events': {
						_inithongTinDoanhNghiepTable: function (append) {
							
							var vm = this;
							vm.traCuuFilter = true;
							vm.viewmore = true;
							this.thongTinDoanhNghiepTableheaders = [
							{
								text: 'STT',
								align: 'left',
								sortable: false,
								value: 'stt'
							},
							{
								text: 'Tên, địa chỉ cơ sở sản xuất. Cơ sở nhập khẩu',
								align: 'left',
								sortable: true,
								value: 'address'
							},
							{
								text: 'Mã số thuế. điện thoại. fax. email',
								align: 'left',
								sortable: true,
								value: 'contactEmail'
							},
							{
								text: 'Người đại diện. chức danh',
								align: 'left',
								sortable: true,
								value: 'applicantName'
							},
							{
								text: 'Thông tin nhà xưởng. (SL, địa chỉ, diện tích, nhân lực, công suất theo tháng)',
								align: 'left',
								sortable: false,
								value: 'action'
							},
							{
								text: 'Tên sản phẩm',
								align: 'left',
								sortable: true,
								value: 'action'
							},
							{
								text: 'Tình trạng đăng ký',
								align: 'left',
								sortable: true,
								value: 'registrationState'
							}
							];

							var paramsBuilder = {
								keyword: vm.keywordsSearchTraCuuDoanhNghiep,
								start: vm.thongTinDoanhNghiepTablepage * 15 - 15,
								end: vm.thongTinDoanhNghiepTablepage * 15,
							};
							
							//TODO
							const config_registrations = {
								params: paramsBuilder,
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								}
							};

							var url = '/o/rest/v2/registrations';
							
							axios.get(url, config_registrations).then(function (response) {
								var serializable = response.data;
								var arrResult = [];

								if(serializable.data){
									for (var i = 0; i < serializable.data.length; i++) {
										if(!serializable.data[i].removed){
											arrResult.push(serializable.data[i]);
										}
									}
								}

								vm.thongTinDoanhNghiepTableItems = arrResult;
								vm.thongTinDoanhNghiepTableTotal = Math.ceil(serializable.total / 15);
								
								// temp fix header
								$('.thongTinDoanhNghiepTable__class th[role="columnheader"]').each(function( index ) {
									if ($( this ).attr('aria-label').indexOf("Activate") > 0) {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>") + ' <i aria-hidden="true" class="material-icons icon">arrow_upward</i>');
									} else {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>"));
									}
								});
								
							})
							.catch(function (error) {
								console.log(error);
								vm.thongTinDoanhNghiepTableItems = [];

							});
							return false; 
						},
						_paggingThongTinDoanhNghiepTable: function() {
							
							this._inithongTinDoanhNghiepTable(false);
						},
						_nextThongTinDoanhNghiepTable: function() {
							
						},
						_previousThongTinDoanhNghiepTable: function() {
							
						},
						toDetailHoSoDoanhNghiep: function (item) {
							
							var vm = this;
							// call DetailAPI.

							var url = '/o/rest/v2/registrations/'+item.registrationId;
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;

								vm.detailRegistModel = serializable;
								vm.detailRegistPage = true;
								window.scrollBy(0, -99999);
								vm._getListForms();
								vm.stateButtonregistration = true;

							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						},
						_getListForms: function () {
							var vm = this;
							// call DetailAPI.

							var url = '/o/rest/v2/registrations/'+vm.detailRegistModel.registrationId+'/forms';
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								var arrFormnotRemmove = [];
								if(serializable.data){
									for (var i = 0; i < serializable.data.length; i++) {
										if(!serializable.data[i].removed){
											arrFormnotRemmove.push(serializable.data[i]);
										}
									}
								}
								vm.registForms = arrFormnotRemmove;
								vm.detailRegistPage = true;
								
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						},
						formatDate (date) {
							if (!date) {
								return null
							}
							const [year, month, day] = date.split('-');
							return '${month}/${day}/${year}';
						},
						parseDate (date) {
							if (!date) return null;

							const [year, month, day] = date.split('-');
							return `${day}/${month}/${year}`;
						},
						addRegistrations: function() {
							var vm = this;
							vm.$dialog.confirm('Bạn có muốn thêm mới hồ sơ Doanh Nghiệp!', {
								html: true,
								loader: true,
								okText: 'Xác nhận',
								cancelText: 'Quay lại',
								animation: 'fade'
							})
							.then((dialog) => {

								// call API get file by dossierId
								var url = "/o/rest/v2/registrations";
								
								$.ajax({
									url: url,
									headers: {
										"groupId": themeDisplay.getScopeGroupId()
									},
									data: {
										"applicantName": "",
										"applicantIdType": "",
										"applicantIdNo": "",
										"applicantIdDate": "",
										"address": "",
										"cityCode": "",
										"districtCode": "",
										"wardCode": "",
										"contactName": "",
										"contactTelNo": "",
										"contactEmail": "",
										"govAgencyCode": "",
										"registrationState": 2,
										"registrationClass": ""
									},
									type: 'POST',
									dataType: 'json',
									contentType: 'application/x-www-form-urlencoded; charset=utf-8',
									success: function(data, textStatus, xhr) {
										vm.detailRegistModel = data.data;
										console.log(vm.detailRegistModel);
									},
									error: function(xhr, textStatus, errorThrown) {
										vm.detailRegistModel = {};
									}
								});
								
								dialog.close();
								return false; 
							})
							.catch((e) => {
								console.log(e)
							})
						},
						registrationPheDuyet: function(registrationState) {

							
							var vm = this;
							vm.stateButtonregistration = false;
							var defaultMessage = 'Đồng ý phê duyệt hồ sơ doanh nghiệp này?';
							
							if (registrationState === 3) {
								defaultMessage = 'Gửi thông báo yêu cầu bổ sung thông tin doanh nghiệp?';
							}
							
							vm.$dialog.confirm(defaultMessage, {
								html: true,
								loader: true,
								okText: 'Xác nhận',
								cancelText: 'Quay lại',
								animation: 'fade'
							})
							.then((dialog) => {

								// call API get file by dossierId
								var url = "/o/rest/v2/registrations/"+vm.detailRegistModel.registrationId;
								
								$.ajax({
									url: url,
									headers: {
										"groupId": themeDisplay.getScopeGroupId()
									},
									data: {
										"registrationState": registrationState,
									},
									type: 'PUT',
									dataType: 'json',
									contentType: 'application/x-www-form-urlencoded; charset=utf-8',
									success: function(data, textStatus, xhr) {
										vm.detailRegistModel = data;
										console.log(vm.detailRegistModel);
										vm.snackbartextdossierViewJX = "Yêu cầu xử lý thành công thành công!";
										vm.snackbardossierViewJX = true;
										vm.stateButtonregistration = false;
										
									},
									error: function(xhr, textStatus, errorThrown) {
										vm.detailRegistModel = {};
										vm.snackbartextdossierViewJX = "Yêu cầu xử lý thành công thất bại!";
										vm.snackbarerordossierViewJX = true;
										vm.stateButtonregistration = true;
									}
								});
								
								dialog.close();
								return false; 
							})
							.catch((e) => {
								console.log(e);
								vm.stateButtonregistration = true;
							})
						}
					}
				},

				'traCuuHoSoTable': {
					'id': 'traCuuHoSoTable',
					'name': 'traCuuHoSoTable',
					'type': 'table',
					'no_data_text': 'Không tìm thấy hồ sơ nào!',
					'cssClass': 'danhSachHoSoTable__class',
					'select_all': true,
					'item_key': 'dossierId',
					'headers': 'headers',
					'template': 'tra_cuu_hoso_table_template',
					'pagging': '_paggingTraCuuHoSoTable',
					'next': '_nextTraCuuHoSoTable',
					'previous': '_previousTraCuuHoSoTable',
					'events': {
						_initraCuuHoSoTable: function (append) {
							var vm = this;
							vm.traCuuFilter = true;
							vm.viewmore = true;
							this.traCuuHoSoTableheaders = [
							{
								text: 'STT',
								align: 'center',
								sortable: false,
								value: 'stt'
							},
							{
								text: 'Tên chứng chỉ',
								align: 'left',
								sortable: true,
								value: 'applicantName'
							},
							{
								text: 'Mã hồ sơ. Số hồ sơ',
								align: 'left',
								sortable: true,
								value: 'dossierId'
							},
							{
								text: 'Ngày gửi. Ngày tiếp nhận. Hạn xử lý',
								align: 'left',
								sortable: true,
								value: 'submitDate'
							},
							{
								text: 'Số chứng chỉ. Ngày cấp',
								align: 'left',
								sortable: false,
								value: 'action'
							},
							{
								text: 'Tên doanh nghiệp',
								align: 'left',
								sortable: false,
								value: 'action'
							},
							{
								text: 'Hành động',
								align: 'center',
								sortable: true,
								value: 'lastActionNote'
							}
							];

							var url = '/o/rest/v2/deliverables';
							var method = "GET";

							var paramsBuilder = {
								
								start: vm.traCuuHoSoTablepage * 15 - 15,
								end: vm.traCuuHoSoTablepage * 15,
								sort: 'modified',
								order: 'false'
							};
							var loai_chung_chi = vm.advancedFilterServiceInfo.deliverableType;
							try{
								if(vm.advanced_filter){

									
									var ten_doanh_nghiep = vm.advanced_filter_applicantName;
									var so_ho_so = vm.advanced_filter_dossierIdCTN;
									var so_chung_chi = vm.advanced_filter_dossierNo;
									var loai_san_pham = vm.advancedFilterLoaiSanPham.itemName;
									var nhan_hieu = vm.advancedFilterNhanHieu.itemName;
									var ten_thuong_mai = vm.advanced_filter_tenThuongMai;
									var ma_kieu_loai = vm.advanced_filter_maKieuLoai;

									if(!loai_chung_chi && !ten_doanh_nghiep && !so_ho_so && !so_chung_chi && !loai_san_pham && !nhan_hieu && 
										!ten_thuong_mai && !ma_kieu_loai){
										paramsBuilder.keyword = vm.keywordsSearchTraCuuHoSo;
								}else {
									var queryKey = '"query": "';
									var queryValue = '"values": "';
									var queryType = '"type": "';

									var queryKeyArr = new Array();
									var queryValueArr = new Array();
									var queryTypeArr = new Array();

									if(ten_doanh_nghiep){

										queryKeyArr.push('(ten_doanh_nghiep like ?)');
										queryValueArr.push('*'+ten_doanh_nghiep+'*');
										queryTypeArr.push('String');

									}

									if(so_ho_so){

										queryKeyArr.push('(so_ho_so = ?)');
										queryValueArr.push(so_ho_so);
										queryTypeArr.push('String');
									}

									if(so_chung_chi){

										queryKeyArr.push('(so_chung_chi = ?)');
										queryValueArr.push(so_chung_chi);
										queryTypeArr.push('String');
									}

									if(loai_san_pham){

										queryKeyArr.push('(loai_san_pham = ?)');
										queryValueArr.push(loai_san_pham);
										queryTypeArr.push('String');
									}

									if(nhan_hieu){

										queryKeyArr.push('(nhan_hieu = ?)');
										queryValueArr.push(nhan_hieu);
										queryTypeArr.push('String');
									}

									if(ten_thuong_mai){

										queryKeyArr.push('(ten_thuong_mai like ?)');
										queryValueArr.push('*'+ten_thuong_mai + '*');
										queryTypeArr.push('String');
									}

									if(ma_kieu_loai){

										queryKeyArr.push('(ma_kieu_loai like ?)');
										queryValueArr.push('*'+ma_kieu_loai + '*');
										queryTypeArr.push('String');
									}

									var query = '{ "query": "' + queryKeyArr.join(" [and] ") + '", "values": " ' + queryValueArr.join("#") + '", "type": " ' + queryTypeArr.join(",") + '"}';
									paramsBuilder.keyword = query;
									url = "/o/rest/v2/deliverables/agency/BGTVTCDKVN/type/"+loai_chung_chi;
									method = "POST";


								}

							}else {
								paramsBuilder.keyword = vm.keywordsSearchTraCuuHoSo;
							}
						}catch(e){
							url = "/o/rest/v2/deliverables";
							method = "GET";
						}

						if(method === "POST"){
							if(!loai_chung_chi){
								vm.snackbartextdossierViewJX = "Bạn phải chọn loại chứng chỉ trước khi tìm kiếm";
								vm.snackbarerordossierViewJX = true;
							}
						}

						$.ajax({
							url : url,
							type : method,
							dataType : "json",
							headers: {
								'groupId': themeDisplay.getScopeGroupId(),
							},
							data : paramsBuilder,
							success : function(result){
								var serializable = result;

								if (append) {
									vm.traCuuHoSoTableItems.push.apply(vm.traCuuHoSoTableItems, serializable.data);
								} else {
									vm.traCuuHoSoTableItems = serializable.data;

									vm.traCuuHoSoTableTotal = Math.ceil(serializable.total / 15);
								}

								vm.xem_them = 'Xem thêm 8+ bản ghi';
								if (!serializable.data || serializable.data.length === 0) {
									vm.xem_them = 'Tổng số ( ' + serializable.total + ' ) bản ghi'
								}
								vm.viewmore = false;

									// temp fix header
									$('.danhSachHoSoTable__class th[role="columnheader"]').each(function( index ) {
										if ($( this ).attr('aria-label').indexOf("Activate") > 0) {
											$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>") + ' <i aria-hidden="true" class="material-icons icon">arrow_upward</i>');
										} else {
											$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>"));
										}
									});

									/*var resData = serializable.data;
									if(resData){
										for (var i = 0; i < resData.length; i++) {
											var deliverableId = resData[i].deliverableId;
											console.log("deliverableId======",deliverableId);
											if(deliverableId){
												$.ajax({
													url : "/o/rest/v2/deliverables/"+deliverableId+"/formdata",
													dataType : "json",
													type : "GET",
													headers: {"groupId": themeDisplay.getScopeGroupId()},
													async : false,
													success : function(result){
														$("#ma_ho_so"+deliverableId).html(result.ma_ho_so);
														$("#so_ho_so"+deliverableId).html(result.so_ho_so);
														$("#ngay_gui"+deliverableId).html(result.ngay_gui);
														$("#ngay_tiep_nhan"+deliverableId).html(result.ngay_tiep_nhan);
														$("#so_chung_chi"+deliverableId).html(result.so_chung_chi);
														$("#ngay_ky_cc"+deliverableId).html(result.ngay_ky_cc);
													},
													error : function(result){

													}
												});
											}

										}
									}*/
								},
								error : function(result){
									console.log(result);
									vm.traCuuHoSoTableItems = [];
								}
							});


						return false; 
					},
					toViewDelivableFile : function(item){
						var deliverableCode = item.deliverableCode;
						if(deliverableCode){
							var url = "/o/rest/v2/dossiers/file/"+deliverableCode;

							const config_blob = {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								},
								responseType: 'blob'
							};
							axios.get(url, config).then(function (response) {

								var result = response.data;
								var urlGetFile = "/o/rest/v2/dossiers/"+result.dossierId+"/files/"+result.referenceUid;

								axios.get(urlGetFile, config_blob).then(function (response) {
									var url = window.URL.createObjectURL(response.data);
									//console.log("url===========>",url);
									window.open(url , '_blank');
								})
								.catch(function (error) {
									console.log(error);

								});

							})
							.catch(function (error) {
								console.log(error);

							});

						}
						return false;
					},
					toDetailHoSoDelivable: function (item) {

						var vm = this;
						vm.stepModel = null;
							// call DetailAPI.
						
						var ma_ho_so = item.ma_ho_so;
						var urlDossierId = '/o/rest/v2/dossiers/number/'+ma_ho_so;

						axios.get(urlDossierId, config).then(function (response) {
							var serializable = response.data;

							var url = '/o/rest/v2/dossiers/'+serializable.dossierId;

							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								var dossierStatus = serializable.dossierStatus;
								var dossierSubStatus = serializable.dossierSubStatus;

								if (dossierStatus === 'cancelling' || 
									dossierStatus === 'cancelled' || 
									dossierStatus === 'processing' || 
									dossierStatus === 'paid') {
									vm.listgroupHoSoFilterselected = dossierStatus;
									vm.statusParamFilter = dossierStatus;
									vm.substatusParamFilter = dossierSubStatus;
								}else {
									
									vm.listgroupHoSoFilterselected = dossierSubStatus;
									vm.statusParamFilter = dossierSubStatus;
									vm.substatusParamFilter = dossierStatus;
								}

								vm.detailModel = serializable;
								vm.detailPage = true;
								window.scrollBy(0, -99999);

								vm._inilistDocumentIn(vm.detailModel);
								vm._inilistDocumentOut(vm.detailModel);

							})
							.catch(function (error) {
								console.log(error);

							});

						})
						.catch(function (error) {
							console.log(error);

						});

						return false; 
					},
					advanced_filter_btn_click : function(){
						var vm = this;
						vm._initraCuuHoSoTable(false);
					},
					_paggingTraCuuHoSoTable: function() {

						this._initraCuuHoSoTable(false);
					},
					_nextTraCuuHoSoTable: function() {

					},
					_previousTraCuuHoSoTable: function() {

					}
				}
			},
			'danhSachHoSoTable': {
				'id': 'danhSachHoSoTable',
				'name': 'danhSachHoSoTable',
				'type': 'table',
				'no_data_text': 'Không tìm thấy hồ sơ nào!',
				'cssClass': 'danhSachHoSoTable__class',
				'select_all': false,
				'item_key': 'dossierId',
				'headers': 'headers',
				'template': 'danh_sach_hoso_table_template',
				'loading': "loadingDanhSachHoSoTable",
				/*'onLoad': '_inidanhSachHoSoTable',*/
				'pagging': '_paggingDanhSachHoSo',
				'next': '_nextDanhSachHoSo',
				'previous': '_previousDanhSachHoSo',
				'events': {
					_inidanhSachHoSoTable: function (append) {
						var vm = this;
						vm.traCuuFilter = false;
						vm.viewmore = true;

						this.danhSachHoSoTableheaders = [
						{
							text: 'STT',
							align: 'center',
							sortable: false,
							value: 'stt'
						},
						{
							text: 'Tên thủ tục. Tên doanh nghiệp',
							align: 'left',
							sortable: true,
							value: 'applicantName'
						},
						{
							text: 'Mã tiếp nhận',
							align: 'left',
							sortable: true,
							value: 'referenceUid'
						},
						{
							text: 'Mã số hồ sơ',
							align: 'left',
							sortable: true,
							value: 'dossierId'
						},
						{
							text: 'Trạng Thái',
							align: 'left',
							sortable: true,
							value: 'submitDate'
						},
						{
							text: 'Ghi chú',
							align: 'left',
							sortable: true,
							value: 'submitDate'
						}

						];
						vm.loadingDanhSachHoSoTable = true;
						var statusParam = vm.statusParamFilter;
						var substatusParam = vm.substatusParamFilter;

							/*if (vm.stageFilterView !== 'danh_sach') {
								statusParam = vm.statusParamFilter;
								substatusParam = vm.substatusParamFilter;
							}*/

							var paramsBuilder = {
								keyword: vm.keywordsSearch,
								owner: vm.applicantIdNo,
								service: vm.serviceInfo,
								follow: true,
								dossierNo: vm.dossierNo,
								dossierIdCTN: vm.dossierIdCTN,
								dossierStatus: vm.dossierStatus,
								start: vm.danhSachHoSoTablepage * 15 - 15,
								end: vm.danhSachHoSoTablepage * 15,
								status: statusParam,
								substatus: substatusParam,
								startDate: vm.parseDate(vm.startDate),
								endDate: vm.parseDate(vm.endDate),
								sort: 'modified',
								order: 'false'
							};

							
							
							// if ( vm.keywordFilter != null ) {
							// 	paramsBuilder['keyword'] = vm.keywordFilter;
							// } else {
							// 	paramsBuilder['keyword'] = vm.keywordsSearchDanhSachHoSo;
							// }
							const config_dossiers = {
								params: paramsBuilder,
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
								}
							};

							var url = '/o/rest/v2/dossiers/dossiersTest';
							//var url = "http://hanoi.fds.vn:2281/api/dossiers";
							
							axios.get(url, config_dossiers).then(function (response) {
								var serializable = response.data;
								

								if (append) {
									vm.danhSachHoSoTableItems.push.apply(vm.danhSachHoSoTableItems, serializable.data);
								} else if(serializable.data){
									/*for (var i = 0; i < serializable.data.length; i++) {
										
										if(serializable.data[i].certNumber){
											if(serializable.data[i].certNumber.length){

												var certNumber = $.grep(serializable.data[i].certNumber, function( value, index ) {
													return ( value.certNo && value.certDate );
												});
												serializable.data[i].certNumber = certNumber;

											}else {

												var certNumbers = new Array();
												var certNumber = serializable.data[i].certNumber;

												if(certNumber.certNo && certNumber.certDate){
													certNumbers.push(certNumber);
													serializable.data[i].certNumber = certNumbers;
												}
											}
										}else {
											serializable.data[i].certNumber = '';
										}

									}*/

									vm.danhSachHoSoTableItems = serializable.data;
									vm.loadingDanhSachHoSoTable = false;
									for (var i = 0; i < vm.danhSachHoSoTableItems.length; i++) {
										var createDate = vm.danhSachHoSoTableItems[i].createDate.toString();
										var year = createDate.substr(0,4);
										var month = createDate.substr(4,2);
										var day = createDate.substr(6,2);

										var hh = createDate.substr(8,2);
										var mm = createDate.substr(10,2);
										var ss = createDate.substr(12,2);

										vm.danhSachHoSoTableItems[i].createDate = day + "/" + month +"/" + year + " " + hh + ":" + mm + ":" + ss;

									}

									vm.danhSachHoSoTableTotal = Math.ceil(serializable.total / 15);
									
								}else {
									vm.danhSachHoSoTableItems = [];

									vm.danhSachHoSoTableTotal = 0;
								}
								
								if (vm.listgroupHoSoFilterselectedIndex >= 0) {
									console.log('reindex counting ...');
									vm.listgroupHoSoFilterItems[vm.listgroupHoSoFilterselectedIndex]['count'] = serializable.total;
								}
								
								vm.xem_them = 'Xem thêm 8+ bản ghi';
								if (!serializable.data) {
									vm.xem_them = 'Tổng số ( ' + serializable.total + ' ) bản ghi'
								}
								vm.viewmore = false;
								
								// temp fix header
								$('.danhSachHoSoTable__class th[role="columnheader"]').each(function( index ) {
									if ($( this ).attr('aria-label').indexOf("Activate") > 0) {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>") + ' <i aria-hidden="true" class="material-icons icon">arrow_upward</i>');
									} else {
										$( this ).html($( this ).attr('aria-label').substring(0, $( this ).attr('aria-label').indexOf(":")).replace(/\./g,"<br/>"));
									}
								});
								//console.log(vm.danhSachHoSoTableItems);
							})
							.catch(function (error) {
								console.log(error);
								vm.danhSachHoSoTableItems = [];
								vm.loadingDanhSachHoSoTable = false;

							});
							
							var todoUrl = '/o/rest/v2/statistics/dossiers/todo';
							
							$.ajax({
								url : todoUrl,
								dataType : "json",
								type : "GET",
								headers : {
									'groupId': themeDisplay.getScopeGroupId(),
								},
								data: {
									service: vm.serviceInfo,
									agency: agencies
								},
								success : function(result){
									var serializable = result;

									var indexTree = -1;
									var index = 0;
									//console.log("listgroupHoSoFilterItems=======FISRT",vm.listgroupHoSoFilterItems);
									//console.log("serializable=======",serializable.data);
									for (var key in serializable.data) {
										for(var i in vm.listgroupHoSoFilterItems){
											if ( serializable.data[key].level === 0) {

												if (serializable.data[key].dossierStatus === 'cancelling' ||
													serializable.data[key].dossierStatus === 'cancelled' ||
													serializable.data[key].dossierStatus === 'processing' ||
													serializable.data[key].dossierStatus === 'paid') {
													serializable.data[key].items = [];

												if(serializable.data[key].dossierStatus === vm.listgroupHoSoFilterItems[i].id){
													vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
												}
											}

										} else {

											if(serializable.data[key].dossierSubStatus === vm.listgroupHoSoFilterItems[i].id){
												vm.listgroupHoSoFilterItems[i].count = serializable.data[key].count;
											}
										}
									}

								}
								//console.log("listgroupHoSoFilterItems=======LAST",vm.listgroupHoSoFilterItems);

							},
							error : function(result){

							}
						});	
							
							return false; 
						},
						toDetailHoSo: function (item) {
							
							var vm = this;
							vm.stepModel = null;
							// call DetailAPI.

							var url = '/o/rest/v2/dossiers/'+item.dossierId;
							
							axios.get(url, config).then(function (response) {
								var serializable = response.data;

								vm.detailModel = serializable;
								vm.detailModel.dossierIdCTN = item.dossierIdCTN;
								vm.detailPage = true;
								window.scrollBy(0, -99999);
								
								vm._inilistDocumentIn(vm.detailModel);
								vm._inilistDocumentOut(vm.detailModel);
								vm._initchangeProcessStep();

							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						},
						_paggingDanhSachHoSo: function() {
							this._inidanhSachHoSoTable(false);
						},
						_nextDanhSachHoSo: function() {
							
						},
						_previousDanhSachHoSo: function() {
							
						}
					}
				},
				// TODO detail
				'listDocumentIn': {
					'id': 'listDocumentIn',
					'name': 'listDocumentIn',
					'type': 'listview',
					'template': 'list_document_in_template',
					'events': {
						_inilistDocumentIn: function (item) {
							var vm = this;
//							vm.listDocumentInItems = [];
//							vm.listDocumentOutItems = [];

								var url = "/o/rest/v2/dossiertemplates/"+item.dossierTemplateNo;
								var urlFiles = "/o/rest/v2/dossiers/"+item.dossierId+"/files";

								axios.all([
									axios.get(url, config),
									axios.get(urlFiles, config)
									])
								.then(axios.spread(function (urlRespones, urlFilesRespones) {
							    // Both requests are now complete
							    vm.dossierFiles = urlFilesRespones.data.data;

							    var serializable = urlRespones.data;

							    var listIn = [], listOut = [], listAll = [];

							    for(var key in serializable.dossierParts){

							    	var countData = 0;
							    	for(var keyFile in vm.dossierFiles){

							    		if ( vm.dossierFiles[keyFile].dossierPartNo === serializable.dossierParts[key].partNo ) {
							    			countData = countData + 1;
							    			serializable.dossierParts[key].referenceUid = vm.dossierFiles[keyFile].referenceUid;
							    			serializable.dossierParts[key].fileEntryId = vm.dossierFiles[keyFile].fileEntryId;
							    			serializable.dossierParts[key].displayName = vm.dossierFiles[keyFile].displayName;

							    			listAll.push(serializable.dossierParts[key]);
							    		}

							    	}

							    	serializable.dossierParts[key].counter = countData;

							    	if ( serializable.dossierParts[key].partType === 2 ) {
							    		listOut.push(serializable.dossierParts[key]);
							    	} else {
							    		listIn.push(serializable.dossierParts[key]);
							    	}

							    }

							    vm.listDocumentInItems = listIn;
							    vm.listDocumentOutItems = listOut;
								// TEMP
								vm._initCbxDocumentNewTab(listAll);
								
								return Promise.reject();
								
							})).catch(function (error) {
								console.log(error);

							});
							return false;
						},
						viewDossierFileVersion: function (item) {
							var vm = this;

							vm.dossierViewJXTitle = item.partName;
							var listFilesUpload = [];
							for (var key in vm.dossierFiles){
								if (vm.dossierFiles[key].dossierPartNo === item.partNo) {
									if(vm.dossierFiles[key].fileSize > 0){
										listFilesUpload.push(vm.dossierFiles[key]);
									}
									
								}
							}
							vm.listDocumentInPartNoItems = listFilesUpload;
							if (item.counter > 0){
								vm.popUpViewDossierFile = !vm.popUpViewDossierFile;
							}
							
						},
						viewDossierFileVersionNewTabOrWindow: function (item) {
							var vm = this;
							if(item.counter > 0){
								console.log("vm.detailModel========",vm.detailModel);

								var url = "/group/cong-xu-ly/xu-ly-ho-so?stateWindow=stateWindow&dossierId="+vm.detailModel.dossierId+
								"&dossierPartNo="+item.partNo;

								vm.$dialog.confirm('Bạn muốn mở trong tab mới hay cửa sổ mới!', {
									html: true,
									loader: true,
									okText: 'Cửa sổ mới',
									cancelText: 'Tab mới',
									animation: 'fade'
								})
								.then((dialog) => {
									//console.log("dialog============",dialog);

									window.open(url, "_blank", "ccc");

									dialog.close();
									return false; 
								})
								.catch((e) => {
									console.log(e);
									window.open(url, "_blank");
								})
							}
							
						},
						viewDossierFileResult: function (item, index) {
							var vm = this;
							if (item.counter > 0){
								vm.popupResultFile = true;
								// call API get file by dossierId
								var urlFiles = "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files";
								
								axios.get(urlFiles, config).then(function (response) {
									var serializable = response.data;
									var resultsFiles = [];
									for (var key in serializable.data) {
										if (serializable.data[key].dossierPartType === 2 && serializable.data[key].dossierPartNo === item.partNo) {
											serializable.data[key].index = index;
											resultsFiles.push(serializable.data[key]);
										}
									}
									vm.listDocumentOutStepItems = resultsFiles;
									vm.popUpViewDossierFile = !vm.popUpViewDossierFile;
									
								})
								.catch(function (error) {
									console.log(error);
									
								});
							}
							return false; 
						}
					}
				},
				'listDocumentInPartNo': {
					'id': 'listDocumentInPartNo',
					'name': 'listDocumentInPartNo',
					'type': 'listview',
					'template': 'list_document_in_part_no_template'
				},
				'listDocumentOut': {
					'id': 'listDocumentOut',
					'name': 'listDocumentOut',
					'type': 'listview',
					'template': 'list_document_out_template',
					'events': {
						downloadReferenceFile: function (item) {
							// call DownloadFile.
							var vm = this;
							const config_blob = {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
									'Content-Type': 'application/json'
								},
								responseType: 'blob'
							};

							var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/files/'+item.referenceUid;
							
							axios.get(url, config_blob).then(function (response) {
								var url = window.URL.createObjectURL(response.data);
								//console.log(url);
								window.open(url);
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						},
						_inilistDocumentOut: function (item) {
							var vm = this;
//							vm.listDocumentInItems = [];
//							vm.listDocumentOutItems = [];

							var url = "/o/rest/v2/dossiertemplates/"+item.dossierTemplateNo;
							var urlFiles = "/o/rest/v2/dossiers/"+item.dossierId+"/files";

							axios.all([
								axios.get(url, config),
								axios.get(urlFiles, config)
								])
							.then(axios.spread(function (urlRespones, urlFilesRespones) {
							    // Both requests are now complete
							    vm.dossierFiles = urlFilesRespones.data.data;

							    var serializable = urlRespones.data;

							    var listIn = [], listOut = [], listAll = [];

							    for(var key in serializable.dossierParts){

							    	var countData = 0;
							    	for(var keyFile in vm.dossierFiles){

							    		if ( vm.dossierFiles[keyFile].dossierPartNo === serializable.dossierParts[key].partNo ) {
							    			countData = countData + 1;
							    			serializable.dossierParts[key].referenceUid = vm.dossierFiles[keyFile].referenceUid;
							    			serializable.dossierParts[key].fileEntryId = vm.dossierFiles[keyFile].fileEntryId;
							    			serializable.dossierParts[key].displayName = vm.dossierFiles[keyFile].displayName;

							    			if(vm.dossierFiles[keyFile].fileSize <= 0){
							    				
							    				countData = countData -1;
							    				
							    			}

							    		}

							    	}

							    	serializable.dossierParts[key].counter = countData;					

							    	if ( serializable.dossierParts[key].partType === 2 ) {
							    		if(serializable.dossierParts[key].partNo.indexOf("KQ") !== -1){
							    			
							    			listOut.push(serializable.dossierParts[key]);
							    		}

							    	} 

							    }

							    vm.listDocumentOutItems = listOut;

							    return Promise.reject();

							})).catch(function (error) {
								console.log(error);

							});
							return false;
						}
					}
				},
				'listDocumentOutStep': {
					'id': 'listDocumentOutStep',
					'name': 'listDocumentOutStep',
					'type': 'listview',
					'template': 'list_document_out_part_no_template'
				},
				'listHistoryProcessing': {
					'id': 'listHistoryProcessing',
					'name': 'listHistoryProcessing',
					'type': 'table',
					'cssClass': 'table_history_style',
					'hide_headers': true,
					'template': 'list_history_processing_template',
					'onLoad': '_inilistHistoryProcessing',
					'events': {
						_inilistHistoryProcessing: function () {

							var vm = this;
							vm.listHistoryProcessingItems = [];

						},
						selectDossierActionTab(){
							
							var vm = this;
							
							//var url = "/o/frontendwebdossier/json/dossier_logs.json?t=1";
							var url = "/o/rest/v2/dossierlogs/"+vm.detailModel.dossierId+"/logs";
							vm.listHistoryProcessingItems = [];
							axios.get(url, config).then(function (response) {
								var serializable = response.data;
								for (var key in serializable.data) {
									if (serializable.data[key].notificationType === 'PROCESS_TYPE') {
										serializable.data[key].createDate = vm.parseDateUtc(serializable.data[key].createDate);
										var stepNameTmp = serializable.data[key].payload.stepName;
										if(stepNameTmp){
											if(stepNameTmp === "type_cancelling"){
												stepNameTmp = "Yêu cầu hủy";
											}else if(stepNameTmp === "type_submit"){
												stepNameTmp = "Yêu cầu sửa thành phần hồ sơ";
											}else if(stepNameTmp === "type_submitting"){
												stepNameTmp = "Yêu cầu sửa đổi bổ sung";
											}else if(stepNameTmp === "type_correcting"){
												stepNameTmp = "Yêu cầu chỉnh sửa kết quả";
											}else if(stepNameTmp === "type_reject_cancelling"){
												stepNameTmp = "Từ chối yêu cầu hủy";
											}else if(stepNameTmp === "type_reject_submit"){
												stepNameTmp = "Hủy yêu cầu sửa thành phần hồ sơ";
											}else if(stepNameTmp === "type_reject_submitting"){
												stepNameTmp = "Từ chối yêu cầu bổ sung";
											}else if(stepNameTmp === "type_reject_correcting"){
												stepNameTmp = "Hủy yêu cầu chỉnh sửa kết quả";
											}
										}
										serializable.data[key].payload.stepName = stepNameTmp;

										vm.listHistoryProcessingItems.push(serializable.data[key]);
									}
								}
							})
							.catch(function (error) {
								console.log(error);
								
							});
							return false; 
						},
						downloadFile( fileAttachId){
							var vm = this;
							var url = "/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+fileAttachId;
							const config_blob = {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
									'Content-Type': 'application/json'
								},
								responseType: 'blob'
							};
							axios.get(url, config_blob).then(function (response) {
								var url = window.URL.createObjectURL(response.data);
								window.open(url);
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						}
					}
				},
                // TODO PROCESS ACTION
                /*"processActionNote": {
                	'id': 'processActionNote',
                	'name': 'processActionNote',
                	"type": "text",
                	'placeholder': 'ý kiến cán bộ ... ',
                	'multi_line': true,
                	'textarea': true
                },*/
                "processAssignUserId": {
                	'id': 'processAssignUserId',
                	'name': 'processAssignUserId',
                	"type": "select",
                	'required': true,
                	'label': 'Lựa chọn cán bộ phân công xử lý ',
                	"item_text": "userName",
                	"item_value": "userId",
                	"hide_selected": true,
                	"chips": true,
                	"deletable_chips": true,
                	"loading": false,
                	"no_data_text": "Lua chon selected",
                	"items": [],
                	'onLoad': '_initprocessAssignUserId',
                	'events': {
                		_initprocessAssignUserId: function () {

                			this.processAssignUserIdItems = [
                			{
                				"userId": 1,
                				"userName": "userName1",
                				"moderator": false
                			},
                			{
                				"userId": 2,
                				"userName": "userName2",
                				"moderator": false
                			},
                			{
                				"userId": 3,
                				"userName": "userName3",
                				"moderator": false
                			},
                			{
                				"userId": 4,
                				"userName": "userName4",
                				"moderator": false
                			}
                			];
                		}
                	}
                },
				// TODO POPUP
				'popUpViewDossierFile' : {
					'id': 'popUpViewDossierFile',
					'name': 'popUpViewDossierFile',
					"type": "dialog",
					"type_dialog": "fullScreen",
					'icon_save': 'undo',
					'label_save': 'Quay lại',
					"color": "primary",
					"template": "popUpViewDossierFileTemplate",
					"events": {
						popUpViewDossierFileClose: function () {
							console.log("close popup");
							var iFrame = document.getElementById("dossierPDFView");
							var dossierPDFViewNotFound = document.getElementById("dossierPDFViewNotFound");
							iFrame.innerHTML = '';
							this.popUpViewDossierFile = false;
							this.popupResultFile = false;
						},
						popUpViewDossierFileSave: function () {
							console.log("save popup");
							var vm = this;
							var iFrame = document.getElementById("dossierPDFView");
							iFrame.innerHTML = '';
							dossierPDFViewNotFound.innerHTML = '';
							vm.popUpViewDossierFile = false;
							vm.popupResultFile = false;
						},
						previewDossierPDF: function (item) {
							var vm = this;
							vm.dossierViewJXTitle = item.displayName;
							// TODO: call API lay file
							var url ="/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid ;
							// var url = "/o/rest/v2/dossiers/14203/files/a148ee9b-b1a7-b2e7-ca0e-e6503a65b8eb";
							console.log("Preview dossier PDF");
							vm._showFile({
								config : {
									headers: {
										'groupId': themeDisplay.getScopeGroupId(),
										'Content-Type': 'application/json'
									},
									responseType: 'blob'
								},
								url : url
							});

						},
						_showFile: function (options) {
							var iFrame = document.getElementById("dossierPDFView");
							var dossierPDFViewNotFound = document.getElementById("dossierPDFViewNotFound1");
							axios.get(options.url, options.config).then(function (response) {
								console.log(response)
								var url = window.URL.createObjectURL(response.data);
								iFrame.className = "";
								iFrame.innerHTML = '<iframe src="'+url+'" width="100%" height="100%"> </iframe>';
								dossierPDFViewNotFound.innerHTML = '';
								if (response.status === 204) {
									dossierPDFViewNotFound.innerHTML = 'Tài liệu không tồn tại!';
								}
								
							})
							.catch(function (error) {
								console.log(error);
								dossierPDFViewNotFound.innerHTML = 'Tài liệu đính kèm không tồn tại!';

							});
							return false; 
						},
						previewDossierPDFNewTab: function (item) {
							var vm = this;
							var url ="/o/rest/v2/dossiers/"+vm.detailModel.dossierId+"/files/"+item.referenceUid ;
							
							const config_blob = {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
									'Content-Type': 'application/json'
								},
								responseType: 'blob'
							};
							
							axios.get(url, config_blob).then(function (response) {
								var url = window.URL.createObjectURL(response.data);
								window.open(url);
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						}
					}
				},
				// 'popUpThongTinXe' : {
				// 	'id': 'popUpThongTinXe',
				// 	'name': 'popUpThongTinXe',
				// 	"type": "dialog",
				// 	"type_dialog": "fullScreen",
				// 	'icon_save': 'undo',
				// 	'label_save': 'Quay lại',
				// 	"color": "primary",
				// 	"template": "popUpThongTinXeTemplate",
				// 	"events": {
				// 		popUpThongTinXeClose: function () {
				// 			var vm = this;
				// 			vm.popUpThongTinXe = !vm.popUpThongTinXe;
				// 		},
				// 		popUpThongTinXeSave: function () {
				// 			var vm = this;
				// 			vm.popUpThongTinXe = !vm.popUpThongTinXe;
				// 		}
				// 	}
				// },
				/*
				'popUpPrintTraCuu' : {
					'id': 'popUpPrintTraCuu',
					'name': 'popUpPrintTraCuu',
					"type": "dialog",
					"type_dialog": "fullScreen",
					'icon_save': 'save',
					'label_save': 'Lưu lại',
					"color": "primary",
					"template": "popUpPrintTraCuuTemplate",
					"events": {
						popUpPrintTraCuuClose: function () {
							var vm = this;
							vm.popUpPrintTraCuu = !vm.popUpPrintTraCuu;
						},
						popUpPrintTraCuuSave: function () {
							var vm = this;
							
						}
					}
				},
				*/
				"cbxDocumentNewTab": {
					'id': 'cbxDocumentNewTab',
					'name': 'cbxDocumentNewTab',
					"type": "select",
					'label': 'Xem tài liệu trong tabs mới',
					"item_text": "displayName",
					"item_value": "referenceUid",
					"single_line": true,
					"hide_selected": true,
					"loading": false,
					"no_data_text": "Lua chon selected",
					"items": [],
					"onChange": "_newTabCbxDocumentNewTab($event)",
					"events": {
						_initCbxDocumentNewTab: function(listAll){
							var vm = this;
							
							vm.cbxDocumentNewTabItems = listAll;
						},
						_newTabCbxDocumentNewTab: function(data) {
							var vm = this;
							const config_blob = {
								headers: {
									'groupId': themeDisplay.getScopeGroupId(),
									'Content-Type': 'application/json'
								},
								responseType: 'blob'
							};

							var url = '/o/rest/v2/dossiers/'+vm.detailModel.dossierId+'/files/'+data.referenceUid;
							
							axios.get(url, config_blob).then(function (response) {
								var url = window.URL.createObjectURL(response.data);
								//console.log(url);
								window.open(url);
							})
							.catch(function (error) {
								console.log(error);

							});
							return false; 
						}

					}
				},
                // TODO ADVANCED SEARCH
                "advanced_filter_serviceInfo": {
                	'id': 'advanced_filter_serviceInfo',
                	'name': 'advanced_filter_serviceInfo',
                	"type": "select",
                	'cssClass': 'no-wrap',
                	"item_text": "deliverableName",
                	"item_value": "deliverableType",
                	'label': 'Lựa chọn loại chứng chỉ',
                	"hide_selected": true,
                	"combobox": true,
                	"loading": false,
                	"no_data_text": "Lua chon selected",
                	"items": [],
                	"onChange": "_filterAdvancedServiceInfoOnchange($event)",
                	'events': {
                		_initAdvanced_filter_serviceInfo: function () {
                			var vm = this;
                			var url = "/o/rest/v2/deliverabletypes";
                			axios.get(url, config).then(function (response) {
                				var serializable = response.data;
                				vm.advanced_filter_serviceInfoItems = serializable.data;
                				console.log(vm.advanced_filter_serviceInfoItems);
                			})
                			.catch(function (error) {
                				console.log(error);

                			});
                		},
                		_filterAdvancedServiceInfoOnchange : function(data){
                			var vm = this;
                			vm.advancedFilterServiceInfo = data;
                		}
                	}
                },
                "advanced_filter_applicantName": {
                	'id': 'advanced_filter_applicantName',
                	'name': 'advanced_filter_applicantName',
                	"type": "text",
                	'label': 'Tên doanh nghiệp'
                },
                "advanced_filter_dossierIdCTN": {
                	'id': 'advanced_filter_dossierIdCTN',
                	'name': 'advanced_filter_dossierIdCTN',
                	"type": "text",
                	'label': 'Số hồ sơ'
                },
/*                "advanced_filter_dossierNo": {
                	'id': 'advanced_filter_dossierNo',
                	'name': 'advanced_filter_dossierNo',
                	"type": "text",
                	'label': 'Số chứng chỉ '
                },*/
                "advanced_filter_startDate" : {
                	'id' : 'advanced_filter_startDate',
                	'name' : 'advanced_filter_startDate',
                	'type' : 'date',
                	'label' : 'Từ ngày'
                },
                "advanced_filter_endDate" : {
                	'id' : 'advanced_filter_endDate',
                	'name' : 'advanced_filter_endDate',
                	'type' : 'date',
                	'label' : 'Đến ngày'
                },
                "advanced_filter_dossierStatus" : {
                	'id' : 'advanced_filter_dossierStatus',
                	'name' : 'advanced_filter_dossierStatus',
                	'type' : 'select',
                	'cssClass': 'no-wrap',
                	"item_text": "statusName",
                	"item_value": "dossierStatus",
                	'label': 'Trạng thái hồ sơ',
                	"hide_selected": true,
                	"combobox": true,
                	"loading": false,
                	"no_data_text": "Lua chon selected",
                	"onChange": "_filterAdvancedDossierStatusOnchange($event)",
                	'events': {
                		_initAdvanced_filter_dossierStatus: function () {
                			var vm = this;
                			if (!vm.advanced_filter_dossierStatusItems) {

                				var url = '/o/rest/v2/statistics/dossiers/todo';
                				console.log("Init load 3");
                				axios.get(url, config).then(function (response) {
                					var serializable = response.data;

                					var serializable = response.data;
                					vm.advanced_filter_dossierStatusItems = serializable.data;
                					console.log(vm.advanced_filter_dossierStatusItems);

                				})
                				.catch(function (error) {
                					console.log(error);

                				});
                			}
                			return false; 
                		},
                		_filterAdvancedDossierStatusOnchange : function(data){
                			var vm = this;
                			vm.advancedFilterDossierStatus = data;
                		}
                	}
                },
                "advanced_filter_loaiSanPham": {
                	'id': 'advanced_filter_loaiSanPham',
                	'name': 'advanced_filter_loaiSanPham',
                	"type": "select",
                	'cssClass': 'no-wrap',
                	"item_text": "itemName",
                	"item_value": "itemCode",
                	'label': 'Loại sản phẩm',
                	"hide_selected": true,
                	"combobox": true,
                	"loading": false,
                	"no_data_text": "Lua chon selected",
                	"items": [],
                	"onChange": "_filterAdvancedLoaiSanPhamOnchange($event)",
                	'events': {
                		_initAdvanced_filter_loaiSanPham: function () {
                			var vm = this;
                			if (vm.advanced_filter_loaiSanPhamItems.length <= 0) {

                				var url = '/o/rest/v2/dictcollections/VR02/dictitems?sort=sibling';

                				axios.get(url, config).then(function (response) {
                					var serializable = response.data;
                					vm.advanced_filter_loaiSanPhamItems = serializable.data;
                					console.log(vm.advanced_filter_loaiSanPhamItems);
                				})
                				.catch(function (error) {
                					console.log(error);

                				});
                			}
                			return false; 
                		},
                		_filterAdvancedLoaiSanPhamOnchange : function(data){
                			var vm = this;
                			vm.advancedFilterLoaiSanPham = data;
                		}
                	}
                },
                "advanced_filter_nhanHieu": {
                	'id': 'advanced_filter_nhanHieu',
                	'name': 'advanced_filter_nhanHieu',
                	"type": "select",
                	'cssClass': 'no-wrap',
                	"item_text": "itemName",
                	"item_value": "itemCode",
                	'label': 'Nhãn hiệu',
                	"hide_selected": true,
                	"combobox": true,
                	"loading": false,
                	"no_data_text": "Lua chon selected",
                	"items": [],
                	"onChange": "_filterAdvancedNhanHieuOnchange($event)",
                	'events': {
                		_initAdvanced_filter_nhanHieu: function () {
                			var vm = this;
                			if (vm.advanced_filter_nhanHieuItems.length <= 0) {

                				var url = '/o/rest/v2/dictcollections/VR03/dictitems?sort=sibling';

                				axios.get(url, config).then(function (response) {
                					var serializable = response.data;
                					vm.advanced_filter_nhanHieuItems = serializable.data;
                				})
                				.catch(function (error) {
                					console.log(error);

                				});
                			}
                			return false; 
                		},
                		_filterAdvancedNhanHieuOnchange : function(data){
                			var vm = this;
                			vm.advancedFilterNhanHieu = data;
                		}
                	}
                },
                "advanced_filter_tenThuongMai": {
                	'id': 'advanced_filter_tenThuongMai',
                	'name': 'advanced_filter_tenThuongMai',
                	"type": "text",
                	'label': 'Tên thương mại'
                },
                "advanced_filter_maKieuLoai": {
                	'id': 'advanced_filter_maKieuLoai',
                	'name': 'advanced_filter_maKieuLoai',
                	"type": "text",
                	'label': 'Mã kiểu loại'
                }
            }
        });

dossierViewJX._builder('dossierViewJX');
}



		/*window.onload = function(event){
			var vm = dossierViewJX;
			setInterval(function(){

				vm._initlistgroupHoSoFilter();
				vm._inidanhSachHoSoTable();


			}, 10000);
		}*/
