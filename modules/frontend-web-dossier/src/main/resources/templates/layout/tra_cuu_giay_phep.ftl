<template>
	<v-container fluid grid-list-md>
		<v-layout row justify-center>
			<v-dialog v-model="popUpThongTinXe" persistent max-width="800px">
				<v-card>
					<v-toolbar dark color="primary" height="50">
						<div class="text-bold">Thông tin phương tiện</div>
						<v-spacer></v-spacer>
						<v-toolbar-items>
							<v-btn icon dark @click.native="popUpThongTinXe = false">
								<v-icon>close</v-icon>
							</v-btn>
						</v-toolbar-items>
					</v-toolbar>
					<v-card-text>
						<v-container grid-list-md>
							<v-layout wrap>
								<v-flex xs12 sm12>
									<v-data-table :headers="vehicleHeaders"
									               :items="vehicleItems"
									               item-key="name"
									               hide-actions
									               expand
									               class="elevation-1">
									  <template slot="items" scope="props">
									    <tr @click="props.expanded = !props.expanded">
									      <td class="text-xs-left">{{ props.item.name }}</td>
									      <td class="text-xs-left">{{ props.item.value }}</td>
									    </tr>
									  </template>	
									</v-data-table>  							
								</v-flex>
								<v-flex xs12 sm12>
									<v-layout row wrap>
									  <v-flex xs12 sm12>
									    <span class="text-bold pt-2" style="color: #1E88E5; text-transform: uppercase;">Lịch sử xuất nhập cảnh</span>  <v-btn class="pull-right btn--small" color="primary" @click.native="addHistoryCar = !addHistoryCar">Thêm lịch sử xuất nhập cảnh</v-btn>
									  </v-flex>
									</v-layout>
									<v-divider class="mt-0"></v-divider>
									<v-slide-y-transition>
										<v-card color="blue-grey lighten-5" v-if="addHistoryCar">
											<v-layout row wrap>
												<v-flex xs12 sm3 style="padding-left: 11px">
													<v-subheader>Chọn hình thức</v-subheader>
												</v-flex>
												<v-flex xs12 sm5>
													<v-select
													:items="hinhThucs"
													item-text="itemName"
													item-value="itemCode"
													v-model="hinhThucSelect"
													clearable
													></v-select>
												</v-flex>
											</v-layout>
											<v-layout row wrap class="text-xs-right">
												<v-flex xs12 sm3 style="padding-left: 11px">
													<v-subheader>Cửa khẩu</v-subheader>
												</v-flex>
												<v-flex xs12 sm4>
													<v-select
													:items="cuaKhaus"
													item-text="itemName"
													v-model="cuaKhauSelect"
													clearable
													></v-select>
												</v-flex>
												<v-flex xs12 sm4>
													<v-menu
													ref="menuThongTinXeDate"
													:close-on-content-click="false"
													v-model="menuThongTinXeDate"
													:nudge-right="40"
													lazy
													transition="scale-transition"
													offset-y
													full-width
													max-width="290px"
													min-width="290px"
													>
													<v-text-field
													slot="activator"
													v-model="thongTinXeDate"
													persistent-hint
													prepend-icon="event"
													></v-text-field>
													<v-date-picker v-model="thongTinXeDate" no-title @input="menuThongTinXeDate = false"></v-date-picker>
												</v-menu>
											</v-layout>
											<v-layout row wrap class="text-xs-right">
												<v-flex xs12 sm3 style="padding-left: 11px">
													<v-subheader>Thông tin lái xe</v-subheader>
												</v-flex>
												<v-flex xs12 sm8>
													<v-text-field
													v-model="thong_tin_lai_xe"
													></v-text-field>
												</v-flex>
											</v-layout>
											<v-layout row wrap class="text-xs-right">
												<v-flex xs12 sm3 style="padding-left: 11px">
													<v-subheader>Giấy phép lái xe:</v-subheader>
												</v-flex>
												<v-flex xs12 sm8>
													<v-text-field
													v-model="giay_phep_lai_xe"
													></v-text-field>
												</v-flex>
											</v-layout>
											<v-layout row wrap>
												<v-flex xs12 sm12 class="pb-3" style="padding-left: 11px">
													<v-btn color="success" @click="addThongTinXe(modelLienVan)">Thêm</v-btn>
												</v-flex>
											</v-layout>
										</v-card>
									</v-slide-y-transition>
								</v-flex>
								<v-flex xs12 sm12 class="ml-1 pr-0">
									<div v-for="item in modelLienVan.data" key="thongTinXeHistorys">
										<v-layout row wrap>
											<v-flex xs12 sm12>
												<v-chip color="blue lighten-1" text-color="white" v-if="item.expImpGateType == 'NC'" small>Nhập Cảnh</v-chip>
												<v-chip color="light-green darken-2" text-color="white" v-else small>Xuất Cảnh</v-chip>
											</v-flex>
											<v-flex xs12 sm12>
												<span>Cửa khẩu: </span>&nbsp;&nbsp; <span>{{item.expImpGate}}</span>
											</v-flex>
											<v-flex xs12 sm12>
												<span>Thông tin lái xe :</span>&nbsp;&nbsp; <span>{{item.driverName}}</span>
											</v-flex>
											<v-flex xs12 sm12>
												<span>Giấy phép lái xe :</span>&nbsp;&nbsp; <span>{{item.driverLicenceNo}}</span>
											</v-flex>
											<v-flex xs12 sm12>
												<span>Ngày xuất/nhập :</span>&nbsp;&nbsp; <span>{{item.registrationDate}}</span>
											</v-flex>
										</v-layout>
										<v-divider></v-divider>
									</div>
									<#-- <v-list two-line subheader v-for="item in modelLienVan.data" key="thongTinXeHistorys">
										<v-list-tile>
											<v-list-tile-content>
												
												<div>
													 &nbsp;&nbsp;&nbsp; <span>Cửa khẩu : </span> {{item.expImpGate}} &nbsp;&nbsp;&nbsp; {{item.registrationDate}}
													<p>Thông tin lái xe : {{item.driverName}}</p>
													<span>Giấy phép lái xe : {{item.driverLicenceNo}}</span>
												</div>
											</v-list-tile-content>
										</v-list-tile>
										<v-divider></v-divider>
									</v-list> -->
									<div class="text-xs-center">
										<v-pagination :length="lengthPageHistory" v-model="pageHistory"></v-pagination>
									</div>
								</v-flex>
							</v-layout>
						</v-container>
					</v-card-text>
				</v-card>
			</v-dialog>
		</v-layout>
		<v-layout row wrap>
			<v-flex sm3 class="pr-0" id="tracuugiayphep">
				<v-card>
					<v-list id="listTraCuuGiayPhep">
						<v-list-tile key="giay_phep_van_tai_quoc_te" avatar @click="changeStateTraCuuGiayPhep('giay_phep_van_tai_quoc_te')">
							<v-list-tile-action>
								<v-icon>local_car_wash</v-icon>
							</v-list-tile-action>
							<v-list-tile-content>
								<span>Giấy phép vận tải quốc tế</span>
							</v-list-tile-content>
						</v-list-tile>

						<v-list-tile key="giay_phep_lien_van" avatar @click="changeStateTraCuuGiayPhep('giay_phep_lien_van')">
							<v-list-tile-action>
								<v-icon>group_work</v-icon>
							</v-list-tile-action>
							<v-list-tile-content>
								<span>Giấy phép cho phương tiện</span>
							</v-list-tile-content>
						</v-list-tile>

						<v-list-tile key="van_ban_chap_thuan" avatar @click="changeStateTraCuuGiayPhep('van_ban_chap_thuan')">
							<v-list-tile-action>
								<v-icon>description</v-icon>
							</v-list-tile-action>
							<v-list-tile-content>
								<span>Chấp thuận khai thác tuyến hành khách cố định</span>
							</v-list-tile-content>
						</v-list-tile>
					</v-list>
				</v-card>

				<!-- <v-card>
					<v-btn block color="secondary"@click="changeStateTraCuuGiayPhep('giay_phep_van_tai_quoc_te')">
						<v-icon right dark>local_car_wash</v-icon>Giấy phép vận tải quốc tế
					</v-btn>
					<v-btn block color="secondary" @click="changeStateTraCuuGiayPhep('giay_phep_lien_van')">
						<v-icon right dark>group_work</v-icon>Giấy phép liên vận
					</v-btn>
					<v-btn block color="secondary"@click="changeStateTraCuuGiayPhep('van_ban_chap_thuan')">
						<v-icon right dark>description</v-icon>Chấp thuận khai thác tuyến hành khách cố định
					</v-btn>
				</v-card> -->
			</v-flex>
			<v-flex sm9 class="pl-0">
				<v-card style="padding-bottom: 10px;">
					<v-layout row wrap style="margin-top:8px;">
						<!-- <v-flex sm2 xs12 class="pt-2">
							<v-subheader>Tìm theo thủ tục</v-subheader>
						</v-flex> -->
						<v-flex sm5 xs12 class="pl-4">
							<v-select
							:items="serviceInfos"
							v-model="serviceInfoSelect"
							item-text="serviceName"
							item-value="serviceCode"
							label="Lựa chọn thủ tục"
							clearable
							></v-select>
						</v-flex>
						<!-- <v-flex sm2 xs12 >
						  
						</v-flex> -->
						<v-flex sm5 xs12 class="pl-3">
							<v-text-field
							label="Nhập từ khóa"
							v-model="keywordTraCuuGiayPhep"
							></v-text-field>
						</v-flex>
						<v-flex sm2 xs12>
							<v-btn small color="primary" @click="searchTraCuuGiayPhep()">Tìm kiếm</v-btn>
						</v-flex>
					</v-layout>
					<v-layout row wrap>
						<!-- <v-flex xs12 sm2 class="pt-2">
							<v-subheader>Cơ quan cấp phép</v-subheader>
						</v-flex> -->
						<v-flex xs12 sm5 class="pl-4">
							<v-select
							:items="govAgencys"
							v-model="govAgencySelect"
							item-text="itemName"
							item-value="itemCode"
							label="Cơ quan cấp phép"
							clearable
							></v-select>
						</v-flex>
						<!-- <v-flex xs12 sm2 class="pt-2" v-if="stateTraCuuGiayPhep == 'van_ban_chap_thuan'">
							<v-subheader>Tuyến</v-subheader>
						</v-flex> -->
						<v-flex xs12 sm5 class="pl-3" v-if="stateTraCuuGiayPhep == 'van_ban_chap_thuan' || stateTraCuuGiayPhep == 'giay_phep_lien_van'">
							<v-select
							:items="tuyens"
							v-model="tuyenSelect"
							item-text="itemName"
							item-value="itemCode"
							label="Tuyến"
							clearable
							></v-select>
						</v-flex>
					</v-layout>
					<v-layout row wrap>
						<!-- <v-flex xs12 sm2 class="pt-2">
							<v-subheader>Từ ngày</v-subheader>
						</v-flex> -->
						<v-flex xs12 sm5 class="pl-4">
							<v-menu
							ref="menuTuNgay"
							:close-on-content-click="false"
							v-model="menuTuNgay"
							:nudge-right="40"
							lazy
							transition="scale-transition"
							offset-y
							full-width
							max-width="290px"
							min-width="290px"
							>
							<v-text-field
							slot="activator"
							v-model="searchTuNgay"
							persistent-hint
							prepend-icon="event"
							label="Từ ngày"
							clearable
							></v-text-field>
							<v-date-picker v-model="searchTuNgay" no-title @input="menuTuNgay = false"></v-date-picker>
						</v-menu>
					</v-flex>
					<!-- <v-flex xs12 sm2 text-right class="pt-2">
						<v-subheader>Đến ngày</v-subheader>
					</v-flex> -->
					<v-flex xs3 sm5 class="pl-3">
						<v-menu
						ref="menuDenNgay"
						:close-on-content-click="false"
						v-model="menuDenNgay"
						:nudge-right="40"
						lazy
						transition="scale-transition"
						offset-y
						full-width
						max-width="290px"
						min-width="290px"
						>
						<v-text-field
						slot="activator"
						v-model="searchDenNgay"
						persistent-hint
						prepend-icon="event"
						label="Đến ngày"
						clearable
						></v-text-field>
						<v-date-picker v-model="searchDenNgay" no-title @input="menuDenNgay = false"></v-date-picker>
					</v-menu>
				</v-flex>
			</v-layout>
		</v-card>
	  		</v-flex>
		</v-layout>		
		<v-layout row wrap>
			<v-flex xs12 sm12 class="text-xs-right pt-3">
				<div v-if="stateTraCuuGiayPhep == 'giay_phep_van_tai_quoc_te'">
					<v-data-table
						:headers="giayPhepVanTaiQuocTeTableheaders"
						:items="giayPhepVanTaiQuocTeTableItems"
						hide-actions
						id="tableGiayPhepVanTaiQuocTe"
						class="elevation-1">
						<template slot="items" slot-scope="props">
							<td style="padding: 8px; padding-left: 0px;width: 3%; " class="text-xs-center">{{ pageGiayPhepVanTaiQuocTeTable * 15 - 15 + props.index + 1 }}</td>
							<td style="padding: 8px;" class="text-xs-left">
								{{ props.item.licenceName }} 
								<br v-if="props.item.licenceNo">
								{{ props.item.licenceNo }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.applicantName }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.govAgencyName }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.validFrom }} 
								<br v-if="props.item.validUntil">
								{{ props.item.validUntil }} 
							</td>
							<td style="padding: 8px; width: 10%;" class="text-xs-center">
								<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="printGiayPhep(props.item)">
									<v-icon style="font-size: 16px;">print</v-icon>
								</v-btn> 
								
								<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="toDetailGiayPhep(props.item)">
									<v-icon style="font-size: 16px;">visibility</v-icon>
								</v-btn>
							</td>
						</template>
					</v-data-table>
					<v-pagination :length="pageGiayPhepVanTaiQuocTeTableLength" v-model="pageGiayPhepVanTaiQuocTeTable" class="pt-3"></v-pagination>
				</div>
				
				<div v-if="stateTraCuuGiayPhep == 'giay_phep_lien_van'">
					<v-data-table
						:headers="giayPhepLienVanTableheaders"
						:items="giayPhepLienVanTableItems"
						hide-actions
						id="tableGiayPhepLienVan"
						class="elevation-1">
						<template slot="items" slot-scope="props">
							<td style="padding: 8px; padding-left: 0px;width: 4%; " class="text-xs-center">{{ pageGiayPhepLienVanTable * 15 - 15 + props.index + 1 }}</td>
							<td style="padding: 8px;" class="text-xs-left">
								{{ props.item.licenceName }} 
								<br v-if="props.item.licenceNo">
								{{ props.item.licenceNo }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.registrationNumber }} 
								<br v-if="props.item.applicantName">
								{{ props.item.applicantName }} 
							</td>
							<td style="padding: 8px; width: 17%;" class="text-xs-left">
								{{ props.item.govAgencyName }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.validFrom }} 
								<br v-if="props.item.validUntil">
								{{ props.item.validUntil }} 
							</td>
							<td style="padding: 8px; width: 14%;" class="text-xs-center">
								<div>
									<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="printGiayPhep(props.item)">
										<v-icon style="font-size: 16px;">print</v-icon>
									</v-btn>
									<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="toDetailGiayPhep(props.item)">
										<v-icon style="font-size: 16px;" >visibility</v-icon>
									</v-btn>
									<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="toDetailThongTinXe(props.item)">
										<v-icon style="font-size: 16px;">fas fa-truck</v-icon>
									</v-btn> 
								</div>
							</td>
						</template>
					</v-data-table>
					<v-pagination :length="pageGiayPhepLienVanTableLength" v-model="pageGiayPhepLienVanTable" class="pt-3"></v-pagination>
				</div>

				<div v-if="stateTraCuuGiayPhep == 'van_ban_chap_thuan'">
					<v-data-table
						:headers="chapThuanKhaiThacTableheaders"
						:items="chapThuanKhaiThacTableItems"
						hide-actions
						id="tableChapThuanKhaiThac"
						class="elevation-1">
						<template slot="items" slot-scope="props">
							<td style="padding: 8px; padding-left: 0px;width: 4%; " class="text-xs-center">{{ pageChapThuanKhaiThacTable * 15 - 15 + props.index + 1 }}</td>
							<td style="padding: 8px;" class="text-xs-left">
								{{ props.item.licenceName }} 
								<br v-if="props.item.so_giay_phep">
								{{ props.item.licenceNo }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.applicantName }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.routes }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.numberOfLot }} 
							</td>
							<td style="padding: 8px; width: 15%;" class="text-xs-left">
								{{ props.item.registrationNumber }} 
								<br v-if="props.item.validFrom">
								{{ props.item.validFrom }} 
								<br v-if="props.item.validUntil">
								{{ props.item.validUntil }} 
							</td>
							<td style="width: 14%;" class="text-xs-center">
								<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="printGiayPhep(props.item)">
									<v-icon style="font-size: 16px;">print</v-icon>
								</v-btn>
								<v-btn class="ml-0 mr-0 btn__info" flat icon style="color: #14BEF0;" @click="toDetailGiayPhep(props.item)">
									<v-icon style="font-size: 16px;">visibility</v-icon>
								</v-btn> 
							</td>
						</template>
					</v-data-table>
					<v-pagination :length="pageChapThuanKhaiThacTableLength" v-model="pageChapThuanKhaiThacTable" class="pt-3"></v-pagination>
				</div>
			</v-flex>
		</v-layout>
	</v-container>
</template>

<script>
	$(function () {
		document.addEventListener('DOMContentLoaded', function (event) {
			$("#listTraCuuGiayPhep > li").click(function (event) {
				event.preventDefault();
				console.log('item list click');
				$(this).addClass('active');
			})
		});
	})
</script>
<style>
	.toolbar__content {
		height: 50px
	}
</style>