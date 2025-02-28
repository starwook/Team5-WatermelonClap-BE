package com.watermelon.server.common.constants;

public class PathConstants {

    public static String LOTTERIES_INFO = "/event/lotteries/info";
    public static String LOTTERIES_RANK = "/event/lotteries/rank";
    public static String LOTTERIES = "/event/lotteries";
    public static String LOTTERIES_REWARD = "/event/lotteries/reward/{rank}";

    public static String PARTS = "/event/parts";
    public static String PARTS_EQUIP = "/event/parts/{partsId}";
    public static String PARTS_REMAIN = "/event/parts/remain";

    public static String ADMIN_PARTS_WINNER = "/admin/event/parts";
    public static String ADMIN_PARTS_WINNER_CHECK = "/admin/event/parts/{uid}/done";
    public static String ADMIN_LOTTERIES_APPLIER = "/admin/event/applier";
    public static String ADMIN_LOTTERIES_WINNER = "/admin/event/lotteries";
    public static String ADMIN_EXPECTATIONS = "/admin/expectations";
    public static String ADMIN_EXPECTATIONS_CHECK = "/admin/expectations/:expectationId/toggle";

    public static String EXPECTATIONS = "/expectations";
    public static String EXPECTATIONS_CHECK = "/expectations/check";
    public static String LOGIN = "/event/lotteries/login";

    public static String MY_LINK = "/link";

    public static String PARTS_LINK_LIST = "/event/parts/link/{link_key}";

    public static String SHORTED_LINK = "/link/{shortedUri}";

}
