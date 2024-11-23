/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import _ from 'lodash';

export function arrayToTree(arrays: any[]) {
    const grouped = _.groupBy(arrays, 'parentId');

    function buildTree(parentId: any) {
        return _.map(grouped[parentId] || [], (item): any => ({
            ...item,
            children: buildTree(item.id)
        }));
    }

    return buildTree(null);
}