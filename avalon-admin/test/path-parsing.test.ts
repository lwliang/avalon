import { testPathParsing } from '../src/router/index'

// 测试路径解析功能
console.log('=== 路径解析测试 ===')

// 测试用例1：基本路径
console.log('\n测试1: /model/base/base.module/window')
testPathParsing('/model/base/base.module/window')

// 测试用例2：带额外路径
console.log('\n测试2: /model/base/base.module/window/form')
testPathParsing('/model/base/base.module/window/form')

// 测试用例3：带更深层路径
console.log('\n测试3: /model/base/base.module/window/tree/detail')
testPathParsing('/model/base/base.module/window/tree/detail')

// 测试用例4：不匹配的路径
console.log('\n测试4: /model/base/window')
testPathParsing('/model/base/window')

// 测试用例5：完全不同的路径
console.log('\n测试5: /home')
testPathParsing('/home') 