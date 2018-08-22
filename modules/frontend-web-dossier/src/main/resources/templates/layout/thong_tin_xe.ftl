<#-- <div id="popUpThongTinXeTemplate">
  <v-flex xs12 sm12 class="pl-5">
    <p>Số đăng ký phương tiện : {{modelLienVan.registrationNumber}}</p>
  </v-flex>
  <v-flex xs12 sm12>
  	<v-expansion-panel>
  		<v-expansion-panel-content>
  			<div slot="header"><v-icon>add</v-icon> Lịch sử xuất nhập cảnh</div>
  			<v-card>
  				<v-layout row wrap>
  					<v-flex xs12 sm3 class='pl-5'>
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
  					<v-flex xs12 sm3 class="pl-5">
						<v-subheader>Cửa khẩu</v-subheader>
  					</v-flex>
  					<v-flex xs12 sm4>
						<v-select
						  :items="cuaKhaus"
              item-text="itemName"
              item-value="itemCode"
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
  					<v-flex xs12 sm3 class="pl-5">
						<v-subheader>Thông tin lái xe</v-subheader>
  					</v-flex>
  					<v-flex xs12 sm8>
						<v-text-field
						  v-model="thong_tin_lai_xe"
						></v-text-field>
  					</v-flex>
  				</v-layout>
  				<v-layout row wrap class="text-xs-right">
  					<v-flex xs12 sm3 class="pl-5">
						<v-subheader>Giấy phép lái xe:</v-subheader>
  					</v-flex>
  					<v-flex xs12 sm8>
						<v-text-field
              v-model="giay_phep_lai_xe"
						></v-text-field>
  					</v-flex>
  				</v-layout>
  				<v-layout row wrap>
  					<v-flex xs12 sm12 class="pl-5">
						<v-btn color="success" @click="addThongTinXe(modelLienVan)">Thêm</v-btn>
  					</v-flex>
  				</v-layout>
  			</v-card>
  		</v-expansion-panel-content>
  	</v-expansion-panel>
  </v-flex>
  <v-flex xs12 sm12>
  	<v-list two-line subheader v-for="item in modelLienVan.data" key="thongTinXeHistorys">
  		<v-list-tile avatar>
  			<v-list-tile-content>
  				<div>
  					<span class="bold">{{ (item.expImpGateType == 'NC') ? 'Nhập cảnh' : 'Xuất Cảnh'}}</span> &nbsp;&nbsp;&nbsp; <span>Cửa khẩu : </span> {{item.expImpGate}} &nbsp;&nbsp;&nbsp; {{item.registrationDate}}
  					<p>Thông tin lái xe : {{item.driverName}}</p>
  					<span>Giấy phép lái xe : {{item.driverLicenceNo}}</span>
  				</div>
  			</v-list-tile-content>
  		</v-list-tile>
  		<v-divider></v-divider>
  	</v-list>
  	<div class="text-xs-center">
  		<v-pagination :length="lengthPageHistory" v-model="pageHistory"></v-pagination>
  	</div>
  </v-flex>
</div> -->