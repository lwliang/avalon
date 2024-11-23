/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {Ref} from "@vue/reactivity";
import {InjectionKey} from "@vue/runtime-dom";

export interface MyTabPanelContext {
    uid: number | undefined,
    label: string | undefined,
    active: Ref<boolean> | boolean,
}

export interface MyTabsContext {
    activeTab: Ref<MyTabPanelContext | undefined>,
    tabs: Ref<MyTabPanelContext[]>,
    registerTab: (tabPanel: MyTabPanelContext) => void;
    tabClick: (tabPanel: MyTabPanelContext) => void;
}

export const tabsRootContextKey: InjectionKey<MyTabsContext> =
    Symbol('tabsRootContextKey')