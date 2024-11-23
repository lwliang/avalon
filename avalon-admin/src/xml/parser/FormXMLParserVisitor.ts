/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import XMLParserVisitor from "./XMLParserVisitor.ts";
import {AttributeContext, ChardataContext, ContentContext, DocumentContext, ElementContext} from "./XMLParser.ts";
import Field from "../../model/Field.ts";
import Service from "../../model/Service.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";

const useService = useGlobalServiceDataStore();
const useFieldDataStore = useGlobalFieldDataStore()


export class FormXMLParserVisitor extends XMLParserVisitor<any> {
    fields: any[]
    fullFields: any[]
    service: string
    viewMode: string
    kanban: any
    tree: any
    form: any
    search: any
    one2ManyFields: string[];

    private _stack: any[] = [] // 记录识别标签的深度
    private _push_stack = (el: any) => {
        this._stack.push(el)
    }
    private _pop_stack = () => {
        this._stack.pop()
    }

    private _contain_stack = (el: any): boolean => {
        return this._stack.includes(el)
    }

    constructor(service: string) {
        super();
        this.fields = [];
        this.fullFields = [];
        this.one2ManyFields = [];
        this.service = service
        this.viewMode = ""
        this.kanban = {}
        this.form = {}
        this.tree = {}
        this.search = {}
    }

    visitDocument = async (ctx: DocumentContext) => {
        const elementContext = ctx.element();
        if (elementContext) {
            await this.visitElement(elementContext);
        }
        return {
            fields: this.fields,
            fullFields: this.fullFields,
            service: this.service,
            viewMode: this.viewMode,
            kanban: this.kanban,
            tree: this.tree,
            form: this.form,
            search: this.search,
            one2ManyFields: this.one2ManyFields,
        }
    };
    appendServiceKeyField = async (): Promise<Field | undefined> => {
        const serviceFields = await useFieldDataStore.getFieldByServiceNameAsync(this.service)
        return serviceFields.find(field => field.isPrimaryKey)
    }
    loadServiceField = async (service: string, field: string): Promise<Field | undefined> => {
        const serviceFields = await useFieldDataStore.getFieldByServiceNameAsync(service)
        return serviceFields.find(x => x.name == field)
    }

    addFields = (field: any) => {
        this.fields.push(field)
        this.fullFields.push(field)
    }

    getTemplate = () => {
        if (this.viewMode == "kanban") return this.kanban
        if (this.viewMode == "tree") return this.tree
        if (this.viewMode == "form") return this.form
        if (this.viewMode == "search") return this.search
    }

    visitElement = async (ctx: ElementContext) => {
        const tagName = ctx.Name_list()[0].getText();
        this._push_stack(tagName)
        if (ctx.Name_list().length == 2) { //  <div ></div>
            if (tagName.trim().toLowerCase() == 'kanban') { // kanban
                this.viewMode = 'kanban'
                this.getTemplate().template = ""
                await this.visitKanbanElement(ctx)
                const key = await this.appendServiceKeyField()
                if (key) {
                    this.addFields({name: key.name})
                }
            } else if (tagName.trim().toLowerCase() == 'search') { // tree
                this.viewMode = 'search'
                this.getTemplate().template = ""
                await this.visitTreeElement(ctx)
            } else if (tagName.trim().toLowerCase() == 'tree') { // tree
                this.viewMode = 'tree'
                this.getTemplate().template = ""
                await this.visitTreeElement(ctx)
                const key = await this.appendServiceKeyField()
                if (key) {
                    this.addFields({name: key.name})
                }
            } else if (tagName.trim().toLowerCase() == 'form') { // form
                this.viewMode = 'form'
                this.getTemplate().template = ""
                await this.visitFormElement(ctx)
                const key = await this.appendServiceKeyField()
                if (key) {
                    this.addFields({name: key.name})
                }
            } else if (tagName.trim().toLowerCase() == 'template') {
                this.getTemplate().template = "";
                await this.visitContent(ctx.content())
            } else if (tagName.trim().toLowerCase() == 'col') {
                this.getTemplate().template += `<MyCol`
                this.visitorTagAttribute(ctx);
                this.getTemplate().template += ">"
                await this.visitContent(ctx.content())
                this.getTemplate().template += `</MyCol>`
            } else if (tagName.trim().toLowerCase() == 'notebook') {
                this.getTemplate().template += `<MyTabs`
                this.visitorTagAttribute(ctx);
                this.getTemplate().template += ">"
                await this.visitContent(ctx.content())
                this.getTemplate().template += `</MyTabs>`
            } else if (tagName.trim().toLowerCase() == 'page') {
                this.getTemplate().template += `<MyTabPanel`
                this.visitorTagAttribute(ctx);
                this.getTemplate().template += ">"
                await this.visitContent(ctx.content())
                this.getTemplate().template += `</MyTabPanel>`
            } else {
                this.getTemplate().template += `<${tagName}`
                this.visitorTagAttribute(ctx);
                if (tagName.toLowerCase() == 'mybutton' || tagName.toLowerCase() == 'my-button') {
                    this.getTemplate().template += ` @click="btnClickHandler"`
                }
                this.getTemplate().template += ">"
                await this.visitContent(ctx.content())
                this.getTemplate().template += `</${tagName}>`
            }
        } else { // 格式 <div />
            if (tagName.trim().toLowerCase() == 'field') { // field
                const field = await this.visitFieldElement(ctx)
                const serviceField = await this.loadServiceField(this.service, field.name) as Field
                if (!this.fields.find(x => x.name == field.name)) {
                    this.addFields(field)
                    if (serviceField && serviceField.type == FieldTypeEnum.Many2oneField) {
                        const relativeServiceName = serviceField.relativeServiceName;
                        const service = await useService.getServiceByNameAsync(relativeServiceName)
                        this.fullFields.push({name: `${field.name}.${service.keyField}`})
                        this.fullFields.push({name: `${field.name}.${service.nameField}`})
                    } else if (serviceField && serviceField.type == FieldTypeEnum.Many2manyField) {
                        const relativeServiceName = serviceField.relativeServiceName;
                        const service = await useService.getServiceByNameAsync(relativeServiceName)
                        this.fullFields.push({name: `${field.name}.id`})
                        this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${service.keyField}`})
                        this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${service.nameField}`})
                    }

                }
                if (this.viewMode == 'form') {
                    if (this._contain_stack('col')) { // 在col中的字段 增加label组件
                        this.getTemplate().template += `<div class="contents">`
                        this.getTemplate().template += `<my-label htmlFor="${field.name}">${serviceField?.label}</my-label>`
                    }

                    if (serviceField.type == FieldTypeEnum.One2manyField) { // 多对多
                        const relativeServiceName = serviceField.relativeServiceName;
                        const service = await useService.getServiceByNameAsync(relativeServiceName)
                        this.getTemplate().template += `<MySubTree ref="${field.name}_input" title="${serviceField.label}" service="${service.name}" field="${serviceField.name}"
                                :record="${field.name}"></MySubTree>`
                        this.one2ManyFields.push(field.name)
                    } else if (serviceField.type == FieldTypeEnum.SelectionField) {
                        this.getTemplate().template += `<MySelectionSelect ref="${field.name}_input" field="${field.name}" service="${this.service}" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MySelectionSelect>`
                    } else if (serviceField.type == FieldTypeEnum.Many2oneField) {
                        this.getTemplate().template += `<MyIdSelect ref="${field.name}_input" service="${serviceField.relativeServiceName}" field="${field.name}" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyIdSelect>`
                    } else if (serviceField.type == FieldTypeEnum.ImageField) {
                        this.getTemplate().template += `<MyUpload ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyUpload>`
                    } else if (serviceField.type == FieldTypeEnum.PasswordField) {
                        this.getTemplate().template += `<MyPassword ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyPassword>`
                    } else if (serviceField.type == FieldTypeEnum.DateField) {
                        this.getTemplate().template += `<my-date ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></my-date>`
                    } else if (serviceField.type == FieldTypeEnum.BooleanField) {
                        this.getTemplate().template += `<MyCheck ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyCheck>`
                    } else if (serviceField.type == FieldTypeEnum.HtmlField) {
                        this.getTemplate().template += `<MyTextarea ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyTextarea>`
                    } else if (serviceField.type == FieldTypeEnum.TextField) {
                        this.getTemplate().template += `<MyTextarea ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyTextarea>`
                    } else if (serviceField.type == FieldTypeEnum.Many2manyField) {
                        this.getTemplate().template += `<MyMany2manySelect ref="${field.name}_input" service="${serviceField.relativeServiceName}" field="${field.name}" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyMany2manySelect>`
                    } else if (serviceField.type == FieldTypeEnum.TimeField) {
                        this.getTemplate().template += `<MyTime ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyTime>`
                    } else if (serviceField.type == FieldTypeEnum.DateTimeField) {
                        this.getTemplate().template += `<MyDatetime ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyDatetime>`
                    } else {
                        this.getTemplate().template += `<MyInput ref="${field.name}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${field.name}"></MyInput>`
                    }

                    if (this._contain_stack('col')) {
                        this.getTemplate().template += `</div>`
                    }
                }
            } else {
                this.getTemplate().template += `<${tagName}`
                this.visitorTagAttribute(ctx)
                this.getTemplate().template += `/>`
            }
        }
        this._pop_stack()
    }

    private visitorTagAttribute(ctx: ElementContext) {
        const attributeContexts = ctx.attribute_list();
        for (let attributeContext of attributeContexts) {
            const attrName = attributeContext.Name().getText()
            let attrValue = attributeContext.STRING().getText()
            this.getTemplate().template += ` ${attrName}=${attrValue}`
        }
    }

    visitKanbanElement = async (ctx: ElementContext) => {
        for (let attributeContext of ctx.attribute_list()) { // 获取看板的所有属性
            await this.visitObjectAttribute(attributeContext, this.kanban)
        }
        await this.visitContent(ctx.content())
    }

    visitTreeElement = async (ctx: ElementContext) => {
        for (let attributeContext of ctx.attribute_list()) { // 获取看板的所有属性
            await this.visitObjectAttribute(attributeContext, this.tree)
        }
        await this.visitContent(ctx.content())
    }
    visitFormElement = async (ctx: ElementContext) => {
        for (let attributeContext of ctx.attribute_list()) { // 获取看板的所有属性
            await this.visitObjectAttribute(attributeContext, this.form)
        }
        await this.visitContent(ctx.content())
    }
    visitFieldElement = async (ctx: ElementContext) => {
        const field: any = {}
        for (let attributeContext of ctx.attribute_list()) {
            await this.visitObjectAttribute(attributeContext, field)
        }
        return field
    }
    visitObjectAttribute = async (ctx: AttributeContext, obj: any) => {
        const str = ctx.STRING().getText().replaceAll("\"", "");
        obj[(ctx.Name().getText())] = str
        return obj
    }

    visitChardata = async (ctx: ChardataContext) => {
        if (ctx.TEXT()) {
            this.kanban.template += ctx.TEXT()
        }
    }

    visitContent = async (ctx: ContentContext) => {
        for (let chardataContext of ctx.chardata_list()) {
            await this.visitChardata(chardataContext)
        }

        for (let elementContext of ctx.element_list()) {
            await this.visitElement(elementContext)
        }
    }
}