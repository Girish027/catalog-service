package com.tfs.dp2.catalog.cob.onboarder.impl.decorators;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.cob.onboarder.impl.ClientViewMapper;
import com.tfs.dp2.catalog.cob.request.entity.ValidateRequestBody;
import com.tfs.dp2.catalog.cob.response.entity.COBExceptionResponse;
import com.tfs.dp2.catalog.viewoutputgran.ClientViewMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientViewMapperCronDecorator extends ClientViewMapperDefaultDecorator {

    public ClientViewMapperCronDecorator(ClientViewMapper clientViewMapper) {
        super(clientViewMapper);
    }

    @Override
    public Optional<ClientViewMapping> map(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) throws COBInternalException {
        log.info(String.format("Enriching cron expression for client:%s and view:%s mapping for product",provisionRequestBody.getClientName(),defClientViewMap.getViewName(),getProduct(provisionRequestBody)));
        Optional<ClientViewMapping> optionalClientViewMapping = getClientViewMappingFromParent(provisionRequestBody, defClientViewMap);
        if (optionalClientViewMapping.isPresent()) {
            ClientViewMapping clientViewMapping = optionalClientViewMapping.get();
            enrichCronExpression(provisionRequestBody, clientViewMapping);
        }
        return optionalClientViewMapping;
    }

    protected Optional<ClientViewMapping> getClientViewMappingFromParent(ValidateRequestBody provisionRequestBody, DefClientViewMap defClientViewMap) throws COBInternalException {
        return super.map(provisionRequestBody, defClientViewMap);
    }

    protected void enrichCronExpression(ValidateRequestBody provisionRequestBody, ClientViewMapping clientViewMapping) throws COBInternalException {
        String granularity = clientViewMapping.getGranularity();
        String timeZone = provisionRequestBody.getCobValidateRequestBody().get().getAccountProperties().getTimezone();
        TimeZoneOffset timeZoneOffset = getOffSet(timeZone);
        OozieCron oozieCron = parseCronExpression(clientViewMapping.getCronExpression());
        try {
            NonHourlyGrains nonHourlyGrains = NonHourlyGrains.valueOf(granularity);
            nonHourlyGrains.updateCronToClientTimeZone(oozieCron, timeZoneOffset);
            clientViewMapping.setCronExpression(oozieCron.toString());
        } catch (IllegalArgumentException e) {
            log.info("Hourly granularity.");
        }
    }

    OozieCron parseCronExpression(String cronExpression) {
        String[] split = cronExpression.split(" ");
        return new OozieCron(split[0], split[1], split[2], split[3], split[4]);
    }

    TimeZoneOffset getOffSet(String timeZone) {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        return new TimeZoneOffset(hours, minutes);
    }

    static class TimeZoneOffset {
        private long hours;
        private long minutes;

        public TimeZoneOffset(long hours, long minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        public long getHours() {
            return hours;
        }

        public long getMinutes() {
            return minutes;
        }
    }


    enum NonHourlyGrains {
        Daily {
            void updateCronToClientTimeZone(OozieCron oozieCron, TimeZoneOffset timeZoneOffset) throws COBInternalException {
                validateHourCron(oozieCron);
                updateHourAndMinuteCron(oozieCron, timeZoneOffset);

            }
        },
        Weekly {
            void updateCronToClientTimeZone(OozieCron oozieCron, TimeZoneOffset timeZoneOffset) throws COBInternalException {
                validateDayOfWeekCron(oozieCron);
                validateHourCron(oozieCron);
                int dayMovement = updateHourAndMinuteCron(oozieCron, timeZoneOffset);
                updateDayOfWeekCron(oozieCron, timeZoneOffset, dayMovement);

            }
        };

        private static int MAX_WEEK = 7;
        private static int MIN_WEEK = 1;

        private static long MIN_MINUTE = 0l;
        private static long MIN_HOUR = 0l;
        private static long MAX_MINUTE = 60l;
        private static long MAX_HOUR = 24l;

        static String HOUR_VALIDATION_MESSAGE = "Cron expression should contain a valid hour of the day. Allowed values [0-23].";
        static String DAY_OF_WEEK_VALIDATION_MESSAGE = "Cron expression should contain a valid day of week. Allowed values [1-7].";

        private static void updateDayOfWeekCron(OozieCron oozieCron, TimeZoneOffset timeZoneOffset, int dayMovement) {

            long effectiveDayOfWeek = Long.valueOf(oozieCron.dayOfWeek) + dayMovement;
            if (effectiveDayOfWeek > MAX_WEEK)
                effectiveDayOfWeek -= MAX_WEEK;
            else {

                if (effectiveDayOfWeek < MIN_WEEK)
                    effectiveDayOfWeek += MAX_WEEK;
            }

            oozieCron.dayOfWeek = String.valueOf(effectiveDayOfWeek);
        }

        abstract void updateCronToClientTimeZone(OozieCron oozieCron, TimeZoneOffset timeZoneOffset) throws COBInternalException;

        private static void validateHourCron(OozieCron oozieCron) throws COBInternalException {
            if (oozieCron.hour.equals("*")) {
                log.error(HOUR_VALIDATION_MESSAGE);
                COBExceptionResponse cobExceptionResponse = new COBExceptionResponse("Unable to process provisioning request.", COBExceptionResponse.Code.NO_RETRY.toString());
                throw new COBInternalException(cobExceptionResponse);
            }
        }

        private static void validateDayOfWeekCron(OozieCron oozieCron) throws COBInternalException {
            if (oozieCron.dayOfWeek.equals("*")) {
                log.error(DAY_OF_WEEK_VALIDATION_MESSAGE);
                COBExceptionResponse cobExceptionResponse = new COBExceptionResponse("Unable to process provisioning request.", COBExceptionResponse.Code.NO_RETRY.toString());
                throw new COBInternalException(cobExceptionResponse);
            }
        }

        private static int updateHourAndMinuteCron(OozieCron oozieCron, TimeZoneOffset timeZoneOffset) {

            long effectiveMinute = MIN_MINUTE;
            long effectiveHour = MIN_HOUR;
            int dayMovement = 0;

            if (timeZoneOffset.getHours() > 0) {
                effectiveMinute = Long.valueOf(oozieCron.minute.equals("*") ? "0" : oozieCron.minute) - timeZoneOffset.getMinutes();
                effectiveHour = Long.valueOf(oozieCron.hour) - timeZoneOffset.getHours();
                if (effectiveMinute < MIN_MINUTE) {
                    effectiveMinute += MAX_MINUTE;
                    effectiveHour -= 1l;
                }
                if (effectiveHour < MIN_HOUR) {
                    effectiveHour = MAX_HOUR + effectiveHour;
                    dayMovement = -1;
                }
            } else {
                effectiveMinute = Long.valueOf(oozieCron.minute.equals("*") ? "0" : oozieCron.minute) + timeZoneOffset.getMinutes();
                effectiveHour = Long.valueOf(oozieCron.hour) - timeZoneOffset.getHours();
                if (effectiveMinute >= MAX_MINUTE) {
                    effectiveMinute -= MAX_MINUTE;
                    effectiveHour += 1l;
                }
                if (effectiveHour >= MAX_HOUR) {
                    effectiveHour = effectiveHour - MAX_HOUR;
                    dayMovement = 1;
                }
            }

            oozieCron.minute = String.valueOf(effectiveMinute);
            oozieCron.hour = String.valueOf(effectiveHour);
            return dayMovement;
        }
    }

    class OozieCron {
        public String minute;
        public String hour;
        public String dayOfMonth;
        public String month;
        public String dayOfWeek;

        public OozieCron(String minute, String hour, String dayOfMonth, String month, String dayOfWeek) {
            this.minute = minute;
            this.hour = hour;
            this.dayOfMonth = dayOfMonth;
            this.month = month;
            this.dayOfWeek = dayOfWeek;
        }

        @Override
        public String toString() {
            return minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek;
        }
    }
}
