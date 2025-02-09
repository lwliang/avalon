/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import XMLParserVisitor from "./XMLParserVisitor.ts";
import {AttributeContext, ChardataContext, ContentContext, DocumentContext, ElementContext} from "./XMLParser.ts";
import Field from "../../model/Field.ts";
import {FieldTypeEnum} from "../../model/enum-type/FieldTypeEnum.ts";
import {useGlobalFieldDataStore} from "../../global/store/fieldStore.ts";
import {useGlobalServiceDataStore} from "../../global/store/serviceStore.ts";
import {useUserInfoStore} from "../../global/store/userInfoStore.ts";
import {ParserField} from "../model/ParserField.ts";
import {dotToUnderscore, getJoinFirstField, hasJoin} from "../../util/fieldUtils.ts";

const useService = useGlobalServiceDataStore();
const useFieldDataStore = useGlobalFieldDataStore()


export class FormXMLParserVisitor extends XMLParserVisitor<any> {
    fields: any[] // 保存原始字段
    fullFields: any[] // 全显示字段，包括 one2many,one2one字段 真正用于查询数据库的字段
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

    private _field_stack: any[] = [] // 记录识别字段的深度
    private _push_field_stack = (el: any) => {
        this._field_stack.push(el)
    }
    private _pop_field_stack = () => {
        this._field_stack.pop()
    }
    private _get_top_field_stack = () => {
        return this._field_stack[this._field_stack.length - 1]
    }
    private _contain_field_stack = (): boolean => {
        return !!this._field_stack.length
    }

    private _field_sub_stack: ParserField[] = [] // 第二tree内行字段的值
    private _push_field_sub_stack = (el: ParserField) => {
        this._field_sub_stack.push(el)
    }
    private _pop_field_sub_stack = () => {
        this._field_sub_stack.pop()
    }
    private _get_top_field_sub_stack = () => {
        return this._field_sub_stack[this._field_stack.length - 1]
    }
    private _contain_field_sub_stack = (): boolean => {
        return !!this._field_sub_stack.length
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
        if (field.includes(".")) {
            const i = field.indexOf(".");
            const prefixField = field.substring(0, i);
            const lastField = field.substring(i + 1);
            const find = serviceFields.find(x => x.name == prefixField);
            if (find) {
                return await this.loadServiceField(find.relativeServiceName, lastField);
            }
            return undefined;
        }
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
        const attributeContexts = ctx.attribute_list();
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
                if (!this._contain_field_stack()) { // 是列表视图
                    this.viewMode = 'tree'
                    this.getTemplate().template = ""
                    await this.visitTreeElement(ctx)
                    const key = await this.appendServiceKeyField()
                    if (key) {
                        this.addFields({name: key.name})
                    }
                } else { // form视图下的tree视图
                    this.getTemplate().template += `<MySubTree`
                    this.visitorTagAttribute(ctx);
                    const field = this._get_top_field_stack()
                    const serviceField = await this.loadServiceField(this.service, field.name) as Field
                    const relativeServiceName = serviceField.relativeServiceName;
                    const service = await useService.getServiceByNameAsync(relativeServiceName)
                    this.getTemplate().template += ` ref="${field.name}_input" title="${serviceField.label}" :delegate="false" parentService="${this.service}" service="${service.name}" field="${serviceField.name}"
                                :record="${field.name}" fieldType=${serviceField.type}`
                    await this.visitContent(ctx.content())
                    this.addFields(field);
                    if (this._contain_field_sub_stack()) { // 有字段
                        let fields = ""
                        for (let fieldSubStackElement of this._field_sub_stack) {
                            const subServiceField = await this.loadServiceField(relativeServiceName, fieldSubStackElement.name) as Field
                            const subService = await useService.getServiceByNameAsync(relativeServiceName)
                            if (subServiceField.type == FieldTypeEnum.Many2oneField || subServiceField.type == FieldTypeEnum.Many2manyField) {
                                this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${fieldSubStackElement.name}`})
                                this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${fieldSubStackElement.name}.${subService.keyField}`})
                                this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${fieldSubStackElement.name}.${subService.nameField}`})
                            } else {
                                this.fullFields.push({name: `${field.name}.${serviceField.relativeForeignKeyName}.${fieldSubStackElement.name}`})
                            }

                            if (!fields) {
                                fields = fieldSubStackElement.name
                            } else {
                                fields = `${fields},${fieldSubStackElement.name}`
                            }
                        }
                        this.fullFields.push({name: `${field.name}.id`})
                        fields = `${fields},id`
                        this.getTemplate().template += ` fields="${fields}" `;
                    }
                    this.getTemplate().template += ">"
                    this.getTemplate().template += `</MySubTree>`
                    this._field_sub_stack.splice(0, this._field_sub_stack.length)
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
            } else if (tagName.trim().toLowerCase() == 'field') {
                const field = await this.visitFieldElement(ctx)
                this._push_field_stack(field);
                await this.visitContent(ctx.content())
                this._pop_field_stack();
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
                if (this._contain_field_stack()) { // 第二级 tree
                    this._push_field_sub_stack(field)
                    return
                }
                if (hasJoin(field.name)) { // 可能是委托
                    const first = getJoinFirstField(field.name)
                    const firstField = await this.loadServiceField(this.service, first) as Field
                    const firstService = await useService.getServiceByNameAsync(firstField.relativeServiceName);
                    if (!this.fullFields.includes(`${first}.${firstService.keyField}`)) {
                        this.fullFields.push({name: `${first}.${firstService.keyField}`})
                    }
                }
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

                        const useInfoStore = useUserInfoStore();
                        let htmlFor = field.name;
                        if (hasJoin(field.name)) {
                            htmlFor = dotToUnderscore(field.name);
                        }
                        if (useInfoStore.user.debug) {
                            this.getTemplate().template += `<my-label htmlFor="${htmlFor}">${serviceField?.label}`
                            this.getTemplate().template += ` <my-debug service="${this.service}" field="${field.name}"></my-debug>`
                            this.getTemplate().template += `</my-label>`;
                        } else {
                            this.getTemplate().template += `<my-label htmlFor="${htmlFor}">${serviceField?.label}</my-label>`
                        }
                    }

                    if (serviceField.type == FieldTypeEnum.One2manyField) { // 1对多
                        this.one2ManyFields.push(field.name)
                    }

                    const templateXml = await this.createComponentName(field, serviceField)
                    if (templateXml) {
                        this.getTemplate().template += templateXml;
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


    private async createComponentName(field: ParserField, serviceField: Field) {
        let componentName = '';
        let htmlName = field.name;
        let delegate = false;
        if (hasJoin(field.name)) {
            htmlName = dotToUnderscore(field.name)
            delegate = true;
        }
        let htmlRef = field.name;
        if (hasJoin(field.name)) {
            htmlRef = dotToUnderscore(field.name)
        }


        if (field.widget) {
            if (field.widget == 'xml') {
                componentName = `MyXmlViewer`
            } else if (field.widget == 'chat') {
                componentName = `ChatWindow`
            }
        }

        if (serviceField.type == FieldTypeEnum.One2manyField) { // 多对多
            if (!componentName) {
                const relativeServiceName = serviceField.relativeServiceName;
                const service = await useService.getServiceByNameAsync(relativeServiceName)
                return `<MySubTree ref="${htmlRef}_input" title="${serviceField.label}" :delegate="${delegate}" parentService="${this.service}" service="${service.name}" field="${serviceField.name}"
                                :record="${field.name}" fieldType=${serviceField.type}></MySubTree>`
            } else {
                return `<${componentName} v-model="${field.name}" />`
            }
        } else if (serviceField.type == FieldTypeEnum.SelectionField) {
            return `<MySelectionSelect border="bottom" ref="${htmlRef}_input" field="${field.name}" service="${this.service}" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MySelectionSelect>`
        } else if (serviceField.type == FieldTypeEnum.FieldSelectionField) {
            return `<MySelectionSelect border="bottom" ref="${htmlRef}_input" field="${field.name}" service="${this.service}" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MySelectionSelect>`
        } else if (serviceField.type == FieldTypeEnum.Many2oneField) {
            return `<MyIdSelect border="bottom" ref="${htmlRef}_input" service="${serviceField.relativeServiceName}" field="${field.name}" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyIdSelect>`
        } else if (serviceField.type == FieldTypeEnum.ImageField) {
            return `<MyImageUpload ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyImageUpload>`
        } else if (serviceField.type == FieldTypeEnum.VideoField) {
            return `<MyVideoUpload ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyVideoUpload>`
        } else if (serviceField.type == FieldTypeEnum.PasswordField) {
            return `<MyPassword ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}" border="bottom"></MyPassword>`
        } else if (serviceField.type == FieldTypeEnum.DateField) {
            return `<my-date ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></my-date>`
        } else if (serviceField.type == FieldTypeEnum.BooleanField) {
            return `<MyCheck ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyCheck>`
        } else if (serviceField.type == FieldTypeEnum.HtmlField) {
            return `<MyTextarea border="bottom" ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyTextarea>`
        } else if (serviceField.type == FieldTypeEnum.TextField) {
            if (!componentName) {
                componentName = `MyTextarea`
            }
            return `<${componentName}  border="bottom" ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></${componentName}>`
        } else if (serviceField.type == FieldTypeEnum.Many2manyField) {
            return `<MyMany2manySelect border="bottom" ref="${htmlRef}_input" service="${serviceField.relativeServiceName}" field="${field.name}" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyMany2manySelect>`
        } else if (serviceField.type == FieldTypeEnum.TimeField) {
            return `<MyTime ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyTime>`
        } else if (serviceField.type == FieldTypeEnum.DateTimeField) {
            return `<MyDatetime border="bottom" ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}"></MyDatetime>`
        } else {
            return `<MyInput ref="${htmlRef}_input" v-model="${field.name}" htmlId="${field.name}" htmlName="${htmlName}" border="bottom"></MyInput>`
        }
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
        return field as ParserField
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