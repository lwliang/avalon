<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {ref, watch} from "vue";
import {getActionSearchView} from "../../api/commonApi.ts";
import {parserEx} from "../../xml/XMLParser.ts";
import {XMLParserResult} from "../../xml/XMLParserResult.ts";
import {arrow, flip, offset, shift, useFloating} from '@floating-ui/vue';
import Field from "../../model/Field.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/21 20:21
 */
const props = defineProps<{
    service: string,
    fullWidth?: Boolean,
    arrowShow?: Boolean,
}>()

const fieldDataStore = useGlobalFieldDataStore();
const serviceDataStore = useGlobalServiceDataStore();
const emit = defineEmits(['conditionChange'])

const reference = ref<any>(null);
const floating = ref(null);
const floatingArrow = ref(null);
const show = ref(false)
const {floatingStyles, middlewareData, placement} = useFloating(reference, floating, {
    placement: 'bottom',
    middleware: [offset(8), flip(), shift(), arrow({element: floatingArrow})],
});

const searchValue = ref<String>('')

const arch = ref<string>('')
let parserResult = ref<XMLParserResult | null>();
const loadSearchView = (service: string) => {
    getActionSearchView(service).then(data => {
        if (data.length) {
            parserEx(data[0].arch, service).then(result => {
                parserResult.value = result
                loadField(parserResult.value.fields)
            })
        } else {
            loadField([])
        }
    })
}
if (props.service) {
    loadSearchView(props.service)
}
const showPopper = () => {
    if (!show.value) {
        show.value = true;
    }
}
const hidePopper = () => {
    if (show.value) {
        show.value = false;
    }
}

watch(() => searchValue.value, (newSearchValue) => {
    if (newSearchValue) {
        showPopper();
    } else {
        hidePopper();
        emit('conditionChange', '');
    }
})

const fields = ref<Field[]>([])
const loadField = async (fieldArr: any[]) => {
    const fieldTemp = await fieldDataStore.getFieldByServiceNameAsync(props.service)
    const service = await serviceDataStore.getServiceByNameAsync(props.service)
    fields.value.splice(0, fields.value.length);
    const nameField = fieldArr.find((x) => x.name == service.nameField)
    if (!nameField) {
        fieldArr.push({name: service.nameField})
    }
    for (let field of fieldArr) {
        const x = fieldTemp.find(x => x.name == field.name);
        if (x) {
            fields.value.push(x)
        }
    }
}
const searchChange = (field: Field, value: String) => {
    let searchCondition = ''
    if (field.type == FieldTypeEnum.StringField ||
        field.type == FieldTypeEnum.TextField ||
        field.type == FieldTypeEnum.HtmlField) {
        searchCondition = `(like,${field.name},"${value}")`
    }
    if (searchCondition) {
        emit('conditionChange', searchCondition);
    }
    hidePopper();
}

</script>

<template>
    <div class="relative">
        <div ref="reference">
            <div class="flex border items-center gap-2 px-2 py-1 rounded-xl overflow-hidden">
                <MyIcon type="fas" icon="search" size="sm"></MyIcon>
                <input placeholder="搜索..." class="text-sm" type="text" v-model="searchValue">
                <MyIcon type="fas" icon="caret-down" size="sm"></MyIcon>
            </div>
        </div>
        <div v-if="show"
             :class="{popover:true,'w-full':fullWidth,'popover-p':!fullWidth,'popover-full-p':fullWidth}"
             ref="floating"
             :style="[floatingStyles]">
            <div class="text-sm">
                <div v-for="(field, index) in fields" :key="index"
                     class="flex items-center px-6 gap-2 py-0.5 cursor-pointer hover:bg-gray-100"
                     @click="searchChange(field,searchValue)">
                    <div style="color: #111827">搜索</div>
                    <div class="font-bold" style="color: #374151">{{ field.label }}</div>
                    <div style="color: #111827">包含</div>
                    <div style="color: #714B67">{{ searchValue }}</div>
                </div>
            </div>
            <div v-if="arrowShow" :class="{'arrow':true,'border-t':!placement.startsWith('top'),
            'border-l':!placement.startsWith('top'),
            'border-b':placement.startsWith('top'),
            'border-r':placement.startsWith('top')}"
                 ref="floatingArrow"
                 :style="{ position: 'absolute',
                left:  middlewareData.arrow?.x != null ? `${middlewareData.arrow.x}px` : '',
                top:  middlewareData.arrow?.y != null ? `${middlewareData.arrow.y}px`: '',
                [placement.startsWith('top') ? 'bottom' : 'top']:'-4px'}">
            </div>
        </div>
    </div>

</template>

<style scoped>

</style>