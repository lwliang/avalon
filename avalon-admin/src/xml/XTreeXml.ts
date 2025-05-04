/**
 * @author lwlianghehe@gmail.com
 * @date 2025/15/02
 */

export default interface XTreeXml extends Record<string, any> {
    parentField: string;
    nameField: string;
    childrenField: string;
    template: string;
}