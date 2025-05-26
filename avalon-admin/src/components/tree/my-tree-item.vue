<script setup lang="ts">
import { TreeData, TreeProps, TreeEmits } from "./tree-props.ts";
import FormField from "../../model/FormField.ts";
import MyCheckBox from "../checkbox/my-check-box.vue";
import { PropType, ref } from "vue";
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
  draggable: false,
});

const emits = defineEmits<TreeEmits>();

const check = ref(new FormField(false));
const indeterminate = ref(false);

const showChildren = ref(false);
const loadFlag = ref(false);
const showChildrenClick = async () => {
  if (props.lazy && !loadFlag.value) {
    if (props.load) {
      const children = await props.load(props.data);
      loadFlag.value = true;
      if (children && props.data) {
        props.data[props.childrenField].push(...children);
      }
    }
  }
  showChildren.value = !showChildren.value;
  if (props.data) {
    emits("expandChild", props.data);
  }
};

const expandChildHandler = (data: TreeData) => {
  emits("expandChild", data);
};

const children = ref<any>([]);
const selectChange = () => {
  if (check.value) {
    if (props.data) {
      emits("nodeSelect", [props.data]);
    }
  } else {
    emits("nodeSelect", []);
  }

  indeterminate.value = false;
  if (!children.value) {
    return;
  }
  children.value.map((item: any) => {
    item.setCheck(check.value.value);
  });
};
const setCheck = (value: boolean) => {
  check.value.value = value;
};

const getCheck = () => {
  return check.value.value;
};
const setMyTreeRef = (el: any) => {
  if (el && !children.value.includes(el)) {
    children.value.push(el);
  }
};
const childSelectValueChange = (nodes: []) => {
  if (!children.value) {
    return;
  }
  let allCheck = true;
  let checkCount = 0;
  for (let valueElement of children.value as any) {
    if (!valueElement.getCheck()) {
      allCheck = false;
      continue;
    }
    checkCount++;
  }
  if (allCheck) {
    check.value.value = true;
    indeterminate.value = false;
  } else {
    indeterminate.value = checkCount != 0;
    if (checkCount == 0) {
      check.value.value = false;
    }
  }
  if (check.value.value) {
    if (props.data) {
      emits("nodeSelect", [...nodes, props.data]);
    }
  } else {
    emits("nodeSelect", [...nodes]);
  }
};

const nodeSelectClick = () => {
  if (props.data) {
    emits("nodeClick", props.data);
  }
};
const nodeClick = (node: TreeData) => {
  if (node) {
    emits("nodeClick", node);
  }
};
const getCheckedNodes = () => {
  if (!indeterminate.value && !check.value.value) {
    // 未选
    return [];
  }

  if (!indeterminate.value && check.value.value) {
    // 全选 可能是叶子节点或父节点
    return [props.data];
  }

  // 部分选
  const checkedNodes: any[] = [];
  if (children.value) {
    children.value.map((item: any) => {
      checkedNodes.push(...item.getCheckedNodes());
    });
  }
  return checkedNodes;
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

const ondragenter = (element: TreeData) => {
  // console.log("my tree item ondragenter", element, props.data)
};
const ondragleave = (element: TreeData) => {
  // console.log("my tree item ondragleave", element, props.data)
};
defineExpose({ setCheck, getCheck, getCheckedNodes });
</script>

<template>
  <div class="">
    <!--父节点-->
    <div
      class="flex items-center gap-1 cursor-pointer hover:bg-gray-200"
      @click="nodeSelectClick"
    >
      <MyIcon
        v-if="(lazy && !loadFlag) || (data && data[childrenField].length)"
        :class="['transition-all', { 'rotate-90': showChildren }]"
        icon="angle-right"
        type="fas"
        @click.stop="showChildrenClick"
      />
      <div class="w-[10px] h-4" v-else></div>
      <MyCheckBox
        v-if="showSelect"
        :indeterminate="indeterminate"
        v-model="check"
        @click.stop="void 0"
        @change="selectChange"
      />
      <div class="flex items-center gap-1">
        <my-avatar :src="avatar" size="small" />
        <div>{{ data ? data["" + props.nameField] : "" }}</div>
      </div>
    </div>
    <!--子节点-->
    <div v-show="showChildren" class="pl-4">
      <draggable
        v-if="data && data[childrenField]"
        :list="data[childrenField]"
        item-key="id"
        :component-data="data"
        group="trees"
        :sort="false"
        @start="onDragStart"
        @end="onDragEnd"
        :disabled="!draggable"
      >
        <template #item="{ element }">
          <my-tree-item
            @selectValue="childSelectValueChange"
            :data="element"
            :ref="setMyTreeRef"
            :nameField="nameField"
            :parentField="parentField"
            :lazy="lazy"
            :load="load"
            :childrenField="childrenField"
            :show-select="showSelect"
            :draggable="draggable"
            @nodeMove="onNodeMove"
            @dragenter="ondragenter(element)"
            @dragleave="ondragleave(element)"
            @expandChild="expandChildHandler(element)"
            @nodeClick="nodeClick"
          />
        </template>
      </draggable>
    </div>
  </div>
</template>

<style scoped></style>
