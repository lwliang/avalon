/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/12 18:40
 */

export interface TreeData extends Record<string, any> {
  id: number;
  name: string;
  parentId: TreeData;
  childIds: TreeData[];
}

export interface TreeProps {
  idField?: string;
  nameField?: string;
  parentField?: string;
  childrenField?: string;
  showSelect?: boolean;
  indent?: number;
  avatar?: string;
  lazy?: boolean;
  load?: (...args: any[]) => any;
  border?: boolean;
  data?: TreeData;
  draggable?: boolean;
}

export interface TreeEmits {
  (e: "nodeClick", data: TreeData): void;
  (e: "nodeSelect", data: TreeData[]): void;
  (e: "nodeMove", data: { from: TreeData; to: TreeData }): void;
  (e: "expandChild", data: TreeData): void;
}
