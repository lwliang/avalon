<script setup lang="ts">
import { TreeData, TreeProps, TreeEmits } from "./tree-props.ts";
import FormField from "../../model/FormField.ts";
import MyTreeItem from "./my-tree-item.vue";
import { ref } from "vue";
import draggable from "vuedraggable";

const props = withDefaults(defineProps<TreeProps>(), {
  idField: "id",
  nameField: "name",
  parentField: "parentId",
  childrenField: "childIds",
  showSelect: false,
  lazy: false,
  border: false,
  indent: 20,
  avatar: "",
});

const formField = defineModel({
  type: FormField,
  required: true,
});
const emits = defineEmits<TreeEmits>();
const expandChildHandler = (data: TreeData) => {
  emits("expandChild", data);
};
const nodeClickValue = (data: TreeData) => {
  emits("nodeClick", data);
};

const nodeSelect = (data: TreeData[]) => {
  emits("nodeSelect", data);
};
const children = ref<any>([]);
const getCheckedNodes = () => {
  const checkedNodes: any[] = [];
  if (children.value) {
    children.value.map((item: any) => {
      checkedNodes.push(...item.getCheckedNodes());
    });
  }
  return checkedNodes;
};
const clearCheckedNodes = () => {
  if (children.value) {
    children.value.map((item: any) => {
      item.setCheck(false);
    });
  }
};
const setMyTreeRef = (el: any) => {
  if (el && !children.value.includes(el)) {
    children.value.push(el);
  }
};

const dragging = ref(false);

const onDragEnd = (evt: any) => {
  dragging.value = false;
  if (!evt.to) return; // 未移动

  const fromElement = evt.item.__draggable_context.element;
  const toElement = evt.to.__draggable_component__.componentData;

  emits("nodeMove", { from: fromElement, to: toElement });
};
const onNodeMove = (data: any) => {
  emits("nodeMove", data);
};
const onDragStart = (evt: any) => {
  dragging.value = true;
};
const dragIntoItem = ref(0);

const ondragenter = (element: TreeData) => {
  // console.log("my tree  ondragenter", element, props.data)
};
const ondragleave = (element: TreeData) => {
  // console.log("my tree  ondragleave", element, props.data)
};
defineExpose({ getCheckedNodes, clearCheckedNodes });
</script>

<template>
  <div :class="[{ border: border }, 'w-full h-full p-2']">
    <template v-if="formField.value && formField.value.length">
      <draggable
        :list="formField.value"
        item-key="id"
        group="trees"
        :sort="false"
        @start="onDragStart"
        @end="onDragEnd"
      >
        <template #item="{ element }">
          <my-tree-item
            :data="element"
            :nameField="nameField"
            :parentField="parentField"
            :childrenField="childrenField"
            :show-select="showSelect"
            :indent="indent"
            :avatar="avatar"
            :idField="idField"
            :ref="setMyTreeRef"
            @nodeSelect="nodeSelect"
            @dragenter="ondragenter(element)"
            @dragleave="ondragleave(element)"
            @nodeMove="onNodeMove"
            :lazy="lazy"
            :load="load"
            @expandChild="expandChildHandler(element)"
            @nodeClick="nodeClickValue"
          />
        </template>
      </draggable>
    </template>
    <template v-else>
      <div>无数据</div>
    </template>
  </div>
</template>

<style scoped></style>
