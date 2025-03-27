package dev.buzzverse.buzzcore.utils;

import java.time.Instant;

public final class InfluxUtils {

    private InfluxUtils() {}

    public static String buildFluxQuery(String measurement,
                                        Instant startTime,
                                        Instant endTime,
                                        String deviceEui,
                                        boolean latest) {
        StringBuilder flux = new StringBuilder("""
        from(bucket: "BuzzNode")
          |> range(start: %s, stop: %s)
          |> filter(fn: (r) => r["_measurement"] == "%s"
        """.formatted(startTime, endTime, measurement));

        if (deviceEui != null && !deviceEui.isBlank()) {
            flux.append(" and r[\"deviceEui\"] == \"%s\"".formatted(deviceEui));
        }

        flux.append(")\n");

        flux.append("""
        |> pivot(
            rowKey:["_time"],
            columnKey: ["_field"],
            valueColumn: "_value"
        )
        """);

        flux.append("""
        |> sort(columns: ["_time"], desc: true)
        """);

        if (latest) {
            flux.append("""
            |> limit(n:1)
            """);
        }

        return flux.toString();
    }

    public static Integer safeCastInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number number) {
            return number.intValue();
        }
        return null;
    }

    public static Float safeCastFloat(Object value) {
        if (value == null) return null;
        if (value instanceof Number number) {
            return number.floatValue();
        }
        return null;
    }

    public static Boolean safeCastBoolean(Object value) {
        if (value == null) return null;
        if (value instanceof Boolean bool) {
            return bool;
        } else if (value instanceof Number num) {
            return num.intValue() != 0;
        } else if (value instanceof String s) {
            return Boolean.parseBoolean(s);
        }
        return null;
    }
}
