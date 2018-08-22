<template>
	<div>
		<v-tabs>
			<v-tabs-bar v-model="tabActive" class="cyan" dark>
				<v-tabs-item v-for="tab in tabs" :key="tab.value"	:href="'#' + tab.value" ripple @click="menuTabChange(tab)">
					<v-menu offset-y v-if="tab.value === 'tab2'">
						<span slot="activator">{{tab.text}}</span>
						<v-list style="width: 250px;">
							<v-list-tile v-for="(item, index) in listgroupHoSoFilterItems"	:key="index" @click="groupHoSoFilter(item, index)" :class="{active: index === indexListStatus}">
								<#-- <v-list-tile-action>
									<i aria-hidden="true" class="material-icons icon">folder</i>
								</v-list-tile-action> -->

								<v-list-tile-content>
									<v-list-tile-title>{{item.title}}</v-list-tile-title>
								</v-list-tile-content>

								<v-list-tile-action>
									<span class="px-3" style="background-color: #ccc;">{{item.count}}</span>
								</v-list-tile-action>
							</v-list-tile>
						</v-list>
					</v-menu>
					<span v-else>{{tab.text}}</span>
				</v-tabs-item>
				<v-tabs-slider color="blue"></v-tabs-slider>
			</v-tabs-bar>
			<v-tabs-items>
				<v-tabs-content v-for="tab in tabs"	:key="tab.value" :id="tab.value">
					<div></div>
				</v-tabs-content>
			</v-tabs-items>
		</v-tabs>
	</div>
</template>