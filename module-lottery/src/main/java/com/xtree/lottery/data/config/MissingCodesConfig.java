package com.xtree.lottery.data.config;

import static com.xtree.lottery.rule.betting.Matchers.jssmAlias;
import static com.xtree.lottery.rule.betting.Matchers.pk10Alias;
import static com.xtree.lottery.rule.betting.Matchers.sscAlias;

import java.util.*;

/**
 * 遗漏配置
 */
public class MissingCodesConfig {
    public static final List<MissingCode> MISSING_CODES = Arrays.asList(
            new MissingCode(
                    Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"),
                    filterAliases(sscAlias, "ssc"),  // 这里需要定义 filterAliases 方法
                    Arrays.asList(2180, 2184, 56, 52, 2227, 75, 73, 451, 101102, 101603, 322384, 322388, 367004, 322405, 101101, 101301, 101501, 325180, 325184, 232, 229, 325227, 245, 247, 326101, 326301, 326501, 335180, 335184, 2269, 335227, 2276, 2283, 2287, 336101, 336301, 336501, 19016, 19025, 19077, 19032, 19041, 19053, 19055, 311101, 311301, 311501) // 这里省略部分数据
            ),
            new MissingCode(
                    Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10"),
                    concatAliases(pk10Alias, jssmAlias),  // 这里需要定义 concatAliases 方法
                    Arrays.asList(313018, 313019, 313021, 313032, 313034, 313023, 313024, 313169, 313218, 313219, 313221, 313232, 313234, 313269, 313223, 313224, 399006, 399007, 399009, 399020, 399022, 399036, 399011, 399012, 407006, 407007, 407009, 407020, 407022, 407036, 407011, 407012, 410006, 410007, 410009, 410020, 410022, 410036, 410011, 410012, 410506, 410507, 410509, 410520, 410522, 410536, 410511, 410512)
            )
    );

    // Lodash _.filter(sscAlias, (alias) => alias !== 'ssc')
    private static List<String> filterAliases(List<String> aliases, String exclude) {
        List<String> result = new ArrayList<>();
        for (String alias : aliases) {
            if (!alias.equals(exclude)) {
                result.add(alias);
            }
        }
        return result;
    }

    // Lodash _.concat(pk10Alias, jssmAlias)
    private static List<String> concatAliases(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }

    public static class MissingCode {
        public List<String> codes;
        public List<String> alias;
        public List<Integer> menuids;

        public MissingCode(List<String> codes, List<String> alias, List<Integer> menuids) {
            this.codes = codes;
            this.alias = alias;
            this.menuids = menuids;
        }
    }
}
