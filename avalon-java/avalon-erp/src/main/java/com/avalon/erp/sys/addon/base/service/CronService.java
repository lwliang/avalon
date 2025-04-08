package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ScheduledFuture;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/03 11:06
 */
@Service
@Slf4j
public class CronService extends AbstractService {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Override
    public boolean needCheckPermission() {
        return false;
    }

    @Override
    public boolean needCheckRecordRule() {
        return false;
    }

    @Override
    public String getServiceName() {
        return "base.cron";
    }

    @Override
    public String getLabel() {
        return "定时任务";
    }

    public Field userId = Fields.createMany2one("用户", "base.user");
    public Field serviceId = Fields.createMany2one("模型", "base.service");
    public Field method = Fields.createString("方法名");
    public Field cron = Fields.createString("cron表达式");
    public Field status = Fields.createBoolean("启用", true, true);
    public Field runNumber = Fields.createInteger("执行次数", -1); // 剩余执行次数，-1 表示无限次数
    public Field nextRunTime = Fields.createDateTime("下次执行时间");
    public Field doAll = Fields.createBoolean("执行所有"); // 服务器停机时间，也会执行

    private Hashtable<Integer, ScheduledFuture<?>> scheduledFutureHashtable = new Hashtable<>();

    public void startTask(RecordRow row) {
        String cron = row.getString(this.cron);
        Integer userId = row.getInteger(this.userId);
        Integer id = row.getInteger("id");
        String serviceId = row.getString(this.serviceId);
        final Date[] nextRunTime = {row.getDate(this.nextRunTime)};
        final Integer[] runNumber = {row.getInteger(this.runNumber)};
        String db = getContext().getBaseName();
        if (runNumber[0] != null && runNumber[0] == 0) {
            return;
        }
        String serviceName = getServiceBean("base.service").getFieldValue("name",
                Condition.equalCondition("id", serviceId)).getString();
        String method = row.getString(this.method);
        Trigger trigger = triggerContext -> {
            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
            if (lastActualExecutionTime == null) {
                // 如果任务未曾执行过，返回 startTime
                return nextRunTime[0];
            }
            // 使用 CronTrigger 计算下次执行时间
            CronTrigger cronTrigger = new CronTrigger(cron);
            return cronTrigger.nextExecutionTime(triggerContext);
        };
        ScheduledFuture<?> schedule = taskScheduler.schedule(() -> {
            log.info("定时任务执行：serviceName:{},method:{}", serviceName, method);
            getContext().init(db);
            getContext().setUserId(userId);
            invokeMethod(serviceName, method);
            RecordRow updateRow = RecordRow.build();
            updateRow.put("id", id);
            if (runNumber[0] > 0) {
                runNumber[0]--;
                updateRow.put("runNumber", runNumber[0]);
                if (runNumber[0] == 0) {
                    updateRow.put("status", false);
                }
            }
            nextRunTime[0] = DateTimeUtils.getCronNextTime(nextRunTime[0], cron);
            updateRow.put("nextRunTime", nextRunTime[0]);
            update(updateRow);

        }, trigger);
        scheduledFutureHashtable.put(id, schedule);
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        Integer delete = super.delete(row);
        Integer id = row.getInteger("id");
        stopTask(id);
        return delete;
    }

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);
        if (recordRow.containsKey(this.status) && recordRow.getBoolean(this.status)) {
            startTask(recordRow);
        }
    }

    @Override
    protected void checkAfterUpdate(RecordRow recordRow) throws AvalonException {
        super.checkAfterUpdate(recordRow);
        if (recordRow.containsKey(this.status)) {
            if (recordRow.getBoolean(this.status)) {
                Record select = select(Condition.equalCondition("id", recordRow.getInteger("id")),
                        getAllFieldName().toArray(new String[0]));
                if (!select.isEmpty()) {
                    startTask(select.get(0));
                }
            } else {
                stopTask(recordRow.getInteger("id"));
            }
        }
    }

    public void stopTask(Integer id) {
        if (scheduledFutureHashtable.containsKey(id)) {
            log.info("删除定时任务：{}", id);
            scheduledFutureHashtable.get(id).cancel(false);
            scheduledFutureHashtable.remove(id);
        } else {
            log.warn("定时任务不存在：{}", id);
        }
    }

    @TemporaryElevate({ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
    public void startAllTask() {
        Record select = select(Condition.equalCondition(this.status, "true"), getAllFieldName().toArray(new String[0]));
        select.forEach(this::startTask);
    }

    @TemporaryElevate({ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
    public void stopAllTask() {
        Record select = select(Condition.equalCondition(this.status, "true"), "id");
        select.forEach(row -> {
            stopTask(row.getInteger("id"));
        });
    }
}
