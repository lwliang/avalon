<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref} from "vue";
import {getModelPageApi} from "../../api/modelApi.ts";
import MyImage from "../image/my-image.vue";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {useRoute} from "vue-router";
import Service from "../../model/Service.ts";
import MyIcon from "../icon/my-icon.vue";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";
import Field from "../../model/Field.ts";
import {getDateTime} from "../../util/dateUtils.ts";

const serviceFieldStore = useGlobalFieldDataStore()

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22 22:43
 */
const props = defineProps({
    service: String,
    serviceId: Number
})
const route = useRoute();
const serviceName = ref<string>(route.params.service as string)
const service = ref<Service>()
const serviceStore = useGlobalServiceDataStore()
const pageNum = ref<number>(1)
const pageSize = ref<number>(10)

const logs = ref<any[]>([])

const serviceFields = ref<Field[]>([])
serviceFieldStore.getFieldByServiceNameAsync(serviceName.value).then(data => {
    serviceFields.value.push(...data)
})


const loadLogs = () => {
    getModelPageApi("id,op,creator,handleName,createTime,serviceName,serviceId,content",
        `(&,(=,serviceName,${props.service}),(=,serviceId,${props.serviceId}))`,
        'base.service.log',
        pageNum.value,
        pageSize.value).then(pageInfo => {
        const data = pageInfo.data;
        for (const datum of data) {
            datum.contentJSON = JSON.parse(datum.content)
        }
        logs.value.push(...data);
    })
}

loadLogs();

serviceStore.getServiceByNameAsync(serviceName.value).then(data => {
    service.value = data
})

const getFieldLabel = (field: string) => {
    const find = serviceFields.value.find(x => x.name == field);
    if (find) {
        return find.label;
    }
    return field
}

</script>

<template>
    <div>
        <div v-for="(log) in logs" :key="log.id" class="flex">
            <div>
                <MyImage src="/avatar.png" width="32px" height="32px"></MyImage>
            </div>
            <div class="flex-1 pl-2">
                <div class="flex gap-2">
                    <div>{{  log.handleName }}</div>
                    <div>{{ getDateTime(log.createTime) }}</div>
                </div>
                <div class="flex gap-2">
                    <div>{{ service?.label }}</div>
                    <div>{{ log.op == 'insert' ? '创建' : '更新' }}</div>
                </div>
                <div class="pl-8 pb-8">
                    <ul class="list-disc">
                        <li v-for="(value,key,index) in log.contentJSON" :key="index">
                            <div class="w-full grid" style="grid-template-columns: 100px 25px  minmax(0, 1fr) ">
                                <div class="w-[100px]">{{ getFieldLabel(String(key)) }}</div>
                                <div class="">
                                    <MyIcon type="fas" icon="arrow-right"/>
                                </div>
                                <div>{{ value }}</div>
                            </div>

                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</template>

<style scoped>

</style>