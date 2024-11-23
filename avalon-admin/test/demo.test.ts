import {isString} from '../src/util/typeUtils'
import {expect, test} from "vitest";

test('测试字符串', () => {
    expect(isString('1')).toBe(true)
})

type Value<T> = T[keyof T]

type Example = {
    name: string,
    age: number,
    sayHello:()=>{}
}

test('keyof测试', () => {
    type exampleValues = Value<Example>
    const a: exampleValues = ''
})