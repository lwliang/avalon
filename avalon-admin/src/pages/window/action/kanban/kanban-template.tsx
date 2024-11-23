/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import {compile, createVNode, defineComponent} from "vue";
import {getModuleIcon} from "../../../../api/moduleApi.ts";

interface Props {
    template: string,
    fields: any
}

export default defineComponent({
    emits: ['btnClick'],
    props: {
        template: {
            type: String,
            required: true
        },
        fields: {
            type: Object,
            required: true
        }
    },
    setup(props: Props, {emit}) {
        const vNode = compile(props.template)
        const btnClickHandler = (actionType: string, action: string) => {
            emit('btnClick', actionType, action, props.fields)
        }
        return () => {
            return createVNode(vNode, {...props.fields, getModuleIcon, btnClickHandler})
        }
    }
})