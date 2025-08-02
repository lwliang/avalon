<script setup lang="ts">
import {AddressType} from "./address.ts";
import {getModelPageApi} from "../../api/modelApi.ts";
import {watch} from "vue";
import {borderStyleType} from "../icon/types.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/11 18:09
 */
const props = defineProps({
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
  formModel: {
    type: Object,
    default: () => ({})
  }
})

const serviceName = "base.area.2023"
// 省 必须
const province = defineModel<{id: number, name: string} | null>({
  type: Object,
  default: null
})
// 市 非必须
const city = defineModel<{id: number, name: string} | null>('city', {
  type: Object,
  default: null
})
// 区 非必须
const district = defineModel<{id: number, name: string} | null>('district', {
  type: Object,
  default: null
})

watch(() => province.value, () => {
  if(props.formModel.state === 'init'){ // 初始化时，不触发
    return
  }
  if (city.value && province.value) {
    city.value = null
  }
})
watch(() => city.value, () => {
  if(props.formModel.state === 'init'){ // 初始化时，不触发
    return
  }
  if (district.value && city.value) {
    district.value = null
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
  if (province.value) {
    const pageResult = await getModelPageApi(`id,name`,
        `('pCode',=,'${province.value.id}')&('level',=,2)&('name',like,${name ? "'" + name + "'" : "''"})`,
        `${serviceName}`,
        1, 20)

    return pageResult.data;
  } else {
    return []
  }
}

const loadDistrictOption = async (name: string) => {
  if (city.value) {
    const pageResult = await getModelPageApi(`id,name`,
        `('pCode',=,'${city.value.id}')&('level',=,3)&('name',like,${name ? "'" + name + "'" : "''"})`,
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
    <my-many-2-one-select :serviceName="serviceName" :readonly="readonly" :required="required" :border="border"
                          v-model="province"
                          :load="loadProvinceOption"/>
    <my-many-2-one-select v-if="type == 'city' || type == 'district'" :serviceName="serviceName"
                          :border="border"
                          :readonly="readonly"
                          :required="required"
                          v-model="city"
                          :load="loadCityOption"/>
    <my-many-2-one-select v-if="type == 'district'" :serviceName="serviceName" :readonly="readonly"
                          :border="border"
                          :required="required"
                          v-model="district"
                          :load="loadDistrictOption"/>
  </div>
</template>

<style scoped>

</style>