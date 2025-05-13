<script setup lang="ts">
import {AddressType} from "./address.ts";
import MyIdSelect from "../select/id-select/my-id-select.vue";
import FormField from "../../model/FormField.ts";
import {getModelPageApi} from "../../api/modelApi.ts";
import {watch} from "vue";
import {borderStyleType} from "../icon/my-icon.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/11 18:09
 */
const props = defineProps({
  htmlId: String,
  htmlName: String,
  required: Boolean,
  readonly: Boolean,
  field: String,
  type: {
    type: AddressType,
    default: "province",
  },
  border: {
    type: borderStyleType,
    default: 'round'
  },
})

const serviceName = "base.area.2023"
// 省 必须
const province = defineModel({
  type: FormField,
  required: true,
})
// 市 非必须
const city = defineModel('city', {
  type: FormField,
  required: false,
})
// 区 非必须
const district = defineModel('district', {
  type: FormField,
  required: false,
})

watch(() => province.value.value, () => {
  if (city.value && province.value.isChanged() ) {
    city.value.value = null
  }
})
watch(() => city.value?.value, () => {
  if (district.value && city.value?.isChanged()) {
    district.value.value = null
  }
})

const loadProvinceOption = async (name: string) => {
  const pageResult = await getModelPageApi(`id,name`,
      `('level',=,1)&('name',like,${name ? "'" + name + "'" : "''"})`,
      `${serviceName}`,
      1, 20)

  return pageResult.data;
}

const loadCityOption = async (name: string) => {
  if (province.value.value) {
    const pageResult = await getModelPageApi(`id,name`,
        `('pCode',=,'${province.value.value.id}')&('level',=,2)&('name',like,${name ? "'" + name + "'" : "''"})`,
        `${serviceName}`,
        1, 20)

    return pageResult.data;
  } else {
    return []
  }
}

const loadDistrictOption = async (name: string) => {
  if (city.value && city.value.value) {
    const pageResult = await getModelPageApi(`id,name`,
        `('pCode',=,'${city.value.value.id}')&('level',=,3)&('name',like,${name ? "'" + name + "'" : "''"})`,
        `${serviceName}`,
        1, 20)

    return pageResult.data;
  } else {
    return []
  }
}

</script>

<template>
  <div class="flex items-center gap-2">
    <my-id-select :service="serviceName" :html-id="htmlId" :readonly="readonly" :required="required" :border="border"
                  v-model="province"
                  :load-option="loadProvinceOption"/>
    <my-id-select v-if="type == 'city' || type == 'district'" :service="serviceName" :html-id="htmlId+'_city'"
                  :border="border"
                  :readonly="readonly"
                  :required="required"
                  v-model="city"
                  :load-option="loadCityOption"/>
    <my-id-select v-if="type == 'district'" :service="serviceName" :html-id="htmlId+'_district'" :readonly="readonly"
                  :border="border"
                  :required="required"
                  v-model="district"
                  :load-option="loadDistrictOption"/>
  </div>
</template>

<style scoped>

</style>