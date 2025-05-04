/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */
import XTreeXml from "./XTreeXml.ts";

export interface XMLParserResult {
    viewMode: string;
    fields: any[]
    fullFields: any[],
    one2ManyFields?: string[],
    kanban: any,
    tree: any,
    form: any,
    search: any,
    xtree: XTreeXml,
    header: any,
}

export function getTemplate(parseResult: XMLParserResult) {
    if (parseResult.viewMode === 'kanban') {
        return parseResult.kanban.template
    }
    if (parseResult.viewMode === 'form') {
        return parseResult.form.template
    }
    if (parseResult.viewMode === 'search') {
        return parseResult.search.template
    }
    if (parseResult.viewMode === 'xtree') {
        return parseResult.xtree.template
    }
    if (parseResult.viewMode === 'tree') {
        return parseResult.tree.template
    } else {
        return ""
    }
}