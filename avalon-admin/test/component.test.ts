import {mount} from "@vue/test-utils";
import {test} from "vitest";
import MySelectionSelect from "../src/components/select/selection-select/my-selection-select.vue";

test("下拉选项", () => {
    const wrapper = mount(MySelectionSelect, {
        props: {
            htmlId: "org_type",
            htmlName: "org_type",
            required: true,
            readonly: false,
            service: "base.org",
            field: "type"
        }
    })

    wrapper.isVisible();
})