/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/06 10:17
 */

// registry.ts
export class Registry {
    private categories: Map<string, Map<string, any>>;

    constructor() {
        this.categories = new Map();
    }

    /**
     * 注册服务到指定分类
     * @param category 分类名称
     * @param name 服务名称
     * @param service 服务实现
     */
    register<T>(category: string, name: string, service: T): void {
        if (!this.categories.has(category)) {
            this.categories.set(category, new Map());
        }
        const categoryMap = this.categories.get(category)!;
        if (categoryMap.has(name)) {
            console.warn(`Service "${name}" already exists in category "${category}".`);
        }
        categoryMap.set(name, service);
    }

    /**
     * 获取指定分类中的服务
     * @param category 分类名称
     * @param name 服务名称
     * @returns 服务实例或 null
     */
    get<T>(category: string, name: string): T | null {
        const categoryMap = this.categories.get(category);
        if (!categoryMap) {
            console.warn(`Category "${category}" does not exist.`);
            return null;
        }
        return (categoryMap.get(name) as T) || null;
    }

    /**
     * 获取分类中的所有服务
     * @param category 分类名称
     * @returns 服务实例的 Map
     */
    getAll<T>(category: string): Map<string, T> {
        return (this.categories.get(category) as Map<string, T>) || new Map();
    }
}