package zalex.person.com.frameworklib.route;

/**
 * Created by zhouweinan on 2018/4/16.
 */

public class RouterSchema {

    private static final String SCHEMA = "/";

    private static final String GROUP_MAIN = "main";

    private static final String GROUP_CITY = "city";

    public static final String SCHEMA_SELECT_CITIES = SCHEMA + GROUP_CITY + SCHEMA + "select";

    public static final String SCHEMA_MAIN_PAGE = SCHEMA + GROUP_MAIN + SCHEMA + "main";

}
