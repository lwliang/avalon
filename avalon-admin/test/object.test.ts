import {expect, test} from "vitest";
import {getDifference} from "../src/util/ObjectUtils";
import Snowflake from "../src/model/Snowflake";

test('测试difference', () => {
    const obj1 = {id: 1, name: "2", age: 21}
    const obj2 = {id: 1, name: "3", age: 20}
    const result = getDifference(obj1, obj2)
    expect(result).toStrictEqual({ name: "3", age: 20})
})

test("测试雪花算法",()=>{
    const nextId = Snowflake.getNextId();
    const next2Id = Snowflake.getNextId();
    expect(nextId).not.toBe(next2Id)
})