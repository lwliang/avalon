import {CharStreams, CommonTokenStream, ParseTreeWalker} from "antlr4";
import XMLLexer from '../src/xml/parser/XMLLexer'
import XMLParser from "../src/xml/parser/XMLParser";
import {expect, test} from "vitest";
import MyParserListener from "../src/xml/parser/MyParserListener";
import {FormXMLParserVisitor} from "../src/xml/parser/FormXMLParserVisitor";


test('测试识别字符串', () => {
    const input = `<kanban>
                <field name="name"/>
                <field name="label"/>
                <field name="description"/>
                <field name="isInstall"/>
                <field name="icon"/>
                <template>
                    <div class="pr-4 flex justify-center items-center">
                        <MyImage width="50" height="50" :src="getModuleIcon(name,icon)"/>
                    </div>
                    <div class="pr-4">
                        <div>
                            <div class="pb-0.5">{{ label }}</div>
                            <div class="text-gray-400">{{ description }}</div>
                        </div>
                        <div class="pt-4">
                            <MyButton :rounded="true" type="primary">{{ isInstall ? '升级' : '安装' }}</MyButton>
                            <MyButton :rounded="true" class="ml-2" type="danger" v-if="isInstall">卸载</MyButton>
                        </div>
                    </div>
                </template>
            </kanban>`

    const chars = CharStreams.fromString(input);
    const lexer = new XMLLexer(chars);
    const tokens = new CommonTokenStream(lexer);
    const parser = new XMLParser(tokens);
    const tree = parser.document();
    const result = tree.getText()
    expect(true).toBe(true)
})


test('识别field字段', () => {
    const input = `<kanban>
                <field name="name"/>
                <field name="label"/>
                <field name="description"/>
                <field name="isInstall"/>
                <field name="icon"/>
            </kanban>`

    const chars = CharStreams.fromString(input);
    const lexer = new XMLLexer(chars);
    const tokens = new CommonTokenStream(lexer);
    const parser = new XMLParser(tokens);
    const tree = parser.document();

    const walker = new ParseTreeWalker();
    const listener = new MyParserListener("org.base", []);
    walker.walk(listener, tree);
    expect(listener.fields).toStrictEqual(['name', 'label', 'description', 'isInstall', 'icon'])
})


test('异步识别xml', () => {
    const input = `<kanban>
                <field name="name"/>
                <field name="label"/>
                <field name="description"/>
                <field name="isInstall"/>
                <field name="icon"/>
                <template>
                    <div class="pr-4 flex justify-center items-center mr-4">
                        <MyImage width="50" height="50" :src="getModuleIcon(name,icon)"/>
                    </div>
                    <div class="pr-4">
                        <div>
                            <div class="pb-0.5">{{ label }}</div>
                            <div class="text-gray-400">{{ description }}</div>
                        </div>
                        <div class="pt-4">
                            <MyButton :rounded="true" type="primary" :action="isInstall ? 'upgrade':'install'"
                                      action-type="object">{{ isInstall ? '升级' : '安装' }}
                            </MyButton>
                            <MyButton :rounded="true" class="ml-2" type="danger" v-if="isInstall" action="uninstall"
                                      action-type="object">卸载
                            </MyButton>
                        </div>
                    </div>
                </template>
            </kanban>`

    const chars = CharStreams.fromString(input);
    const lexer = new XMLLexer(chars);
    const tokens = new CommonTokenStream(lexer);
    const parser = new XMLParser(tokens);
    const tree = parser.document();

    const visitor = new FormXMLParserVisitor("base.user")
    visitor.visitDocument(tree).then(data => {
        expect(data.fields.map(x=>x.name)).toStrictEqual(['name', 'label', 'description', 'isInstall', 'icon'])
    })
})

test('识别template字段', () => {
    const input = `<kanban>
                <field name="name"/>
                <field name="label"/>
                <field name="description"/>
                <field name="isInstall"/>
                <field name="icon"/>
                <template>
                    <div class="pr-4 flex justify-center items-center">
                        <MyImage width="50" height="50" :src="getModuleIcon(name,icon)"/>
                    </div>
                    <div class="pr-4">
                        <div>
                            <div class="pb-0.5">{{ label }}</div>
                            <div class="text-gray-400">{{ description }}</div>
                        </div>
                        <div class="pt-4">
                            <MyButton :rounded="true" type="primary">{{ isInstall ? '升级' : '安装' }}</MyButton>
                            <MyButton :rounded="true" class="ml-2" type="danger" v-if="isInstall">卸载</MyButton>
                        </div>
                    </div>
                </template>
            </kanban>`

    const chars = CharStreams.fromString(input);
    const lexer = new XMLLexer(chars);
    const tokens = new CommonTokenStream(lexer);
    const parser = new XMLParser(tokens);
    const tree = parser.document();

    const walker = new ParseTreeWalker();
    const listener = new MyParserListener("service.base", []);
    walker.walk(listener, tree);
    expect(listener.template).toStrictEqual(`<div class="pr-4 flex justify-center items-center"><MyImage width="50" height="50" :src="getModuleIcon(name,icon)"/></div><div class="pr-4"><div><div class="pb-0.5">{{ label }}</div><div class="text-gray-400">{{ description }}</div></div><div class="pt-4"><MyButton :rounded="true" type="primary" @click=btnClickHandler>{{ isInstall ? '升级' : '安装' }}</MyButton><MyButton :rounded="true" class="ml-2" type="danger" v-if="isInstall" @click=btnClickHandler>卸载</MyButton></div></div>`)
})