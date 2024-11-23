/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

export default class Snowflake {
    machineId: number;
    sequence: number;
    lastTimestamp: bigint;

    constructor(machineId = 1) {
        this.machineId = machineId & 0x1f; // 5 bits for machine ID
        this.sequence = 0;
        this.lastTimestamp = BigInt(-1);
    }

    // 获取当前时间戳
    getCurrentTimestamp() {
        return BigInt(Date.now());
    }

    // 生成下一个 ID
    nextId() {
        let timestamp = this.getCurrentTimestamp();

        if (timestamp === this.lastTimestamp) {
            // 同一毫秒内，增加序列号
            this.sequence = (this.sequence + 1) & 0xfff; // 12 bits for sequence
            if (this.sequence === 0) {
                // 序列号用完，等待下一毫秒
                while (timestamp <= this.lastTimestamp) {
                    timestamp = this.getCurrentTimestamp();
                }
            }
        } else {
            // 不同毫秒，重置序列号
            this.sequence = 0;
        }

        this.lastTimestamp = timestamp;

        // 组合 ID
        return (timestamp << 22n) | (BigInt(this.machineId) << 12n) | BigInt(this.sequence);
    }


    static _Snowflake: any = null

    static getNextId() {
        if (!Snowflake._Snowflake) {
            Snowflake._Snowflake = new Snowflake(1)
        }
        return Snowflake._Snowflake.nextId();
    }
}