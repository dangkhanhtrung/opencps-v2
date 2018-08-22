<!-- TODO paymentViewJX template one page view List detail template -->
<div id="danh_sach_hoso_table_template" class="hidden">
	<template slot="items" slot-scope="props">
		<td style="padding: 8px; padding-left: 0px;width: 3%;" class="text-xs-center">{{ danhSachHoSoTablepage * 15 - 15 + props.index + 1 }}</td>
		<td style="padding: 8px; width: 36%;" class="text-xs-left">
			<a href="javascript:;" @click.prevent.stop="toDetailHoSo(props.item)">
				{{ props.item.serviceName }} 
				<br>
			</a>
			{{ props.item.applicantName }} 
		</td>
		<td style="padding: 8px; width: 12%;" class="text-xs-left">
			{{ props.item.dossierNo }} 
		</td>
		<td style="padding: 8px; width: 12%;" class="text-xs-left">
			{{ props.item.dossierIdCTN }}
		</td>
		<td style="padding: 8px; width: 12%;" class="text-xs-left">
			<span v-if="props.item.dossierSubStatusText">{{ props.item.dossierSubStatusText }}</span>
			<span v-else>{{ props.item.dossierStatusText }}</span>
		</td>
		<td style="padding: 8px; width: 22%; position: relative;" class="text-xs-left">
			{{ props.item.receiveDate }} : Nộp hồ sơ <br>
			<span style="color: #da8f05;">{{ props.item.lastActionDate }} : <i>{{ props.item.lastActionName }}</i></span> <br>
			<div style="text-align: right; right: 5px; bottom: 0; position: absolute;">
				<span class="text-hover-blue" @click="viewDialogLog(props.item)" style="color: rgb(134, 131, 131); cursor: pointer;"><i class="fa fa-comments" aria-hidden="true"></i> Tin nhắn ({{ props.item['countLogs'] ? props.item['countLogs'] : 0 }})</span>
			</div>
		</td>
	</template>
</div>


