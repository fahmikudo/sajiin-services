package id.sajiin.sajiinservices.shared.util;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static String toCommaSeparated(Collection<? extends Number> numbers) {
        StringJoiner joiner = new StringJoiner(",");
        for (Number number : numbers) {
            joiner.add(String.valueOf(number));
        }

        return joiner.toString();
    }

    public static Collection<String> convertCommaSeparatedString(String data) {
        if(StringUtil.isNullOrEmpty(data)) return new ArrayList<>();
        return Arrays.asList(data.split(","));
    }

    public static Collection<Integer> convertCommaSeparatedInteger(String data) {
        return convertCommaSeparatedString(data).stream().map(Integer::parseInt).collect(Collectors.toSet());
    }

    public static <T> boolean isNullOrEmpty(T[] list){
        return list == null || list.length == 0;
    }

    public static <T> boolean isNotNullAndNotEmpty(T[] list){
        return list != null && list.length > 0;
    }

    public static <T> boolean isNullOrEmpty(Collection<T> list){
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotNullAndNotEmpty(Collection<T> list){
        return list != null && !list.isEmpty();
    }

    public static <T> List<List<T>> splitList(List<T> list, int chunkSize) {
        List<List<T>> data = new ArrayList<>();
        int numChunks = (list.size() + chunkSize - 1) / chunkSize;
        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, list.size());
            data.add(new ArrayList<>(list.subList(start, end)));
        }
        return data;
    }

}
