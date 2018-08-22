<!-- TODO paymentViewJX template one page view List detail template -->
<div id="danh_sach_giay_phep_van_tai_quoc_te_template" class="hidden">
	<template slot="items" slot-scope="props">
		<td style="padding-top: 3px;"> <v-checkbox primary hide-details v-model="props.selected" ></v-checkbox> </td>
		<td style="padding: 8px; padding-left: 0px;width: 5%; ">{{ giayPhepVanTaiQuocTeTablepage * 15 - 15 + props.index + 1 }}</td>
		<td style="padding: 8px;" class="text-xs-left">
			{{ props.item.giay_phep }} 
			<br v-if="props.item.so_giay_phep">
			{{ props.item.so_giay_phep }} 
		</td>
		<td style="padding: 8px; width: 15%;" class="text-xs-left">
			{{ props.item.ten_doanh_nghiep }} 
		</td>
		<td style="padding: 8px; width: 15%;" class="text-xs-left">
			{{ props.item.co_quan }} 
		</td>
		<td style="padding: 8px; width: 15%;" class="text-xs-left">
			{{ props.item.hieu_luc_tu_ngay }} 
			<br v-if="props.item.hieu_luc_den_ngay">
			{{ props.item.hieu_luc_den_ngay }} 
		</td>
		<td style="padding: 8px; width: 10%;" class="text-xs-left">
			<v-btn color="" @click="toDetailGiayPhep(props.item)">Xem</v-btn> 
		</td>

	</template>
</div>


