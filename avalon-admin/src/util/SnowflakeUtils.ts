/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/13 9:03
 */

class Snowflake {
    // ====== 配置字段 ======
    private epoch: bigint; // 自定义起始时间戳（毫秒）
    private datacenterIdBits: number = 5; // 数据中心 ID 的位数
    private machineIdBits: number = 5; // 机器 ID 的位数
    private sequenceBits: number = 12; // 序列号的位数

    // ====== 最大值 ======
    private maxDatacenterId: bigint; // 数据中心 ID 的最大值
    private maxMachineId: bigint; // 机器 ID 的最大值
    private maxSequence: bigint; // 序列号的最大值

    // ====== 位移偏移量 ======
    private machineIdShift: bigint; // 机器 ID 的位移量
    private datacenterIdShift: bigint; // 数据中心 ID 的位移量
    private timestampLeftShift: bigint; // 时间戳的位移量

    // ====== 动态变量 ======
    private datacenterId: bigint; // 当前实例的数据中心 ID
    private machineId: bigint; // 当前实例的机器 ID
    private sequence: bigint = BigInt(0); // 当前毫秒内的序列号
    private lastTimestamp: bigint = BigInt(-1); // 上次生成 ID 的时间戳

    constructor({
                    datacenterId = 0,
                    machineId = 0,
                    epoch = 1577836800000, // 默认起始时间为 2020-01-01 00:00:00 UTC
                } = {}) {
        // 初始化常量字段
        this.epoch = BigInt(epoch);

        // 最大值计算
        this.maxDatacenterId = BigInt(-1) ^ (BigInt(-1) << BigInt(this.datacenterIdBits)); // 最大数据中心 ID
        this.maxMachineId = BigInt(-1) ^ (BigInt(-1) << BigInt(this.machineIdBits)); // 最大机器 ID
        this.maxSequence = BigInt(-1) ^ (BigInt(-1) << BigInt(this.sequenceBits)); // 最大序列号

        // 位移偏移量计算
        this.machineIdShift = BigInt(this.sequenceBits); // 机器 ID 的偏移量
        this.datacenterIdShift = BigInt(this.sequenceBits + this.machineIdBits); // 数据中心 ID 的偏移量
        this.timestampLeftShift = BigInt(this.sequenceBits + this.machineIdBits + this.datacenterIdBits); // 时间戳的偏移量

        // 初始化动态变量
        this.datacenterId = BigInt(datacenterId); // 数据中心 ID
        this.machineId = BigInt(machineId); // 机器 ID

        // 参数合法性检查
        if (this.datacenterId > this.maxDatacenterId || this.datacenterId < BigInt(0)) {
            throw new Error(
                `datacenterId must be between 0 and ${this.maxDatacenterId}`
            );
        }
        if (this.machineId > this.maxMachineId || this.machineId < BigInt(0)) {
            throw new Error(`machineId must be between 0 and ${this.maxMachineId}`);
        }
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    private currentTime(): bigint {
        return BigInt(Date.now());
    }

    /**
     * 等待下一毫秒（如果当前毫秒内的序列号超出最大值）
     */
    private waitForNextMillis(lastTimestamp: bigint): bigint {
        let timestamp = this.currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = this.currentTime();
        }
        return timestamp;
    }

    /**
     * 生成唯一 ID
     */
    public nextId(): string {
        let timestamp = this.currentTime();

        // 如果当前时间小于上一次生成 ID 的时间，说明时钟回拨，抛出错误
        if (timestamp < this.lastTimestamp) {
            throw new Error(
                `Clock moved backwards. Refusing to generate id for ${
                    this.lastTimestamp - timestamp
                } milliseconds`
            );
        }

        // 如果在同一毫秒内
        if (timestamp === this.lastTimestamp) {
            this.sequence = (this.sequence + BigInt(1)) & this.maxSequence; // 序列号自增
            if (this.sequence === BigInt(0)) {
                // 如果序列号用完，则等待下一毫秒
                timestamp = this.waitForNextMillis(this.lastTimestamp);
            }
        } else {
            // 如果是新的毫秒，重置序列号
            this.sequence = BigInt(0);
        }

        // 记录最后生成 ID 的时间戳
        this.lastTimestamp = timestamp;

        // 生成 ID
        const id =
            ((timestamp - this.epoch) << this.timestampLeftShift) | // 时间戳部分
            (this.datacenterId << this.datacenterIdShift) | // 数据中心部分
            (this.machineId << this.machineIdShift) | // 机器 ID 部分
            this.sequence; // 序列号部分

        // 返回 ID 的字符串形式（BigInt 转为字符串，避免负数）
        return id.toString();
    }

    /**
     * 获取当前实例的配置信息
     */
    public getConfig(): object {
        return {
            epoch: this.epoch.toString(),
            datacenterId: this.datacenterId.toString(),
            machineId: this.machineId.toString(),
            maxDatacenterId: this.maxDatacenterId.toString(),
            maxMachineId: this.maxMachineId.toString(),
            maxSequence: this.maxSequence.toString(),
        };
    }
}

// 示例：使用 Snowflake 类生成唯一 ID
const snowflake = new Snowflake({datacenterId: 1, machineId: 1});

export function nextId(): string {
    return snowflake.nextId();
}